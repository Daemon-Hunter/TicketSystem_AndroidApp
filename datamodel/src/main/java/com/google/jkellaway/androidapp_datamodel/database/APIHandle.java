/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import android.renderscript.ScriptGroup;

import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertAdmin;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtist;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistReview;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertChildEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertParentEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertSocialMedia;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertVenue;

/**
 *
 */
public final class APIHandle {
    public static Object getSingle(int id, DatabaseTable table){
        Map<String, String> objMap = APIConnection.readSingle(id, table);
        switch (table){
            case ADMIN: MapToObject.ConvertAdmin(objMap);break;
            case ARTIST: MapToObject.ConvertArtist(objMap);break;
            //case BOOKING: MapToObject.ConvertCustomerBooking(objMap);break;
            case CHILD_EVENT: MapToObject.ConvertChildEvent(objMap);break;
            //case CUSTOMER: MapToObject.ConvertCustomer(objMap);break;
            //case GUEST_BOOKING: MapToObject.ConvertGuestBooking(objMap);break;
            //case ORDER: MapToObject.ConvertOrder(objMap);break;
            case PARENT_EVENT: MapToObject.ConvertParentEvent(objMap);break;
            case SOCIAL_MEDIA: MapToObject.ConvertSocialMedia(objMap);break;
            //case TICKET: MapToObject.ConvertTicket(objMap);break;
            case VENUE: MapToObject.ConvertVenue(objMap);break;
            default: throw new IllegalArgumentException("These tables are not supported");
        }
        return MapToObject.ConvertArtist(APIConnection.readSingle(id, table));
    }

    public static List<IAdmin> getAdmins() throws IOException {

        List<IAdmin> adminList = new LinkedList<>();
        List<Map<String, String>> adminMapList = APIConnection.readAll(DatabaseTable.ADMIN);
        for (Map<String, String> admin : adminMapList) {
            adminList.add(ConvertAdmin(admin));
        }
        return adminList;
    }

    public static List<IParentEvent> searchParentEvents(String search) throws IOException {
        List<IParentEvent> parentEventList = new LinkedList<>();
        List<Map<String, String>> parentEventMapList = APIConnection.search(search, DatabaseTable.PARENT_EVENT);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IParentEvent>> futures = new LinkedList<>();

        for (final Map<String, String> parentEvent : parentEventMapList){
            Callable<IParentEvent> callable = new Callable<IParentEvent>() {
                @Override
                public IParentEvent call() throws Exception {
                    return ConvertParentEvent(parentEvent);
                }
            };
            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IParentEvent> future : futures){
            try {
                parentEventList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return parentEventList;
    }

    public static List<IArtist> getArtistAmount(Integer amount, Integer lastID) throws IOException {

        List<IArtist> artistList = new LinkedList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IArtist>> futures = new LinkedList<>();

        List<Map<String, String>> artistMapList = APIConnection.readAmount(DatabaseTable.ARTIST, amount, lastID);


        for (final Map<String, String> artistMap : artistMapList) {
            Callable<IArtist> callable = new Callable<IArtist>() {
                @Override
                public IArtist call() throws Exception {
                    IArtist artist;
                    artist = ConvertArtist(artistMap);
                    artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                    return artist;
                }
            };

            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IArtist> future : futures){
            try {
                artistList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }


        return artistList;
    }

    public static List<IParentEvent> getParentAmount(Integer amount, Integer lastID) throws IOException {
        List<IParentEvent> parentEventList = new LinkedList<>();
        List<Map<String, String>> parentEventMapList = APIConnection.readAmount(DatabaseTable.PARENT_EVENT, amount, lastID);


        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IParentEvent>> futures = new LinkedList<>();

        for (final Map<String, String> parentEventMap : parentEventMapList) {
            Callable<IParentEvent> callable = new Callable<IParentEvent>() {
                @Override
                public IParentEvent call() throws Exception {
                    IParentEvent parentEvent;
                    parentEvent = ConvertParentEvent(parentEventMap);
                    parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                    parentEvent.addChildEventList(getChildEventFromParent(parentEvent.getParentEventID()));
                    return parentEvent;
                }
            };
            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IParentEvent> future : futures){
            try {
                parentEventList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return parentEventList;
    }


    private static List<IChildEvent> getChildEventFromParent(int parentID) throws IOException {
        List<Map<String, String>> childEventMapList = APIConnection.getChildEventsViaParent(parentID);
        List<IChildEvent> childEventList = new LinkedList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IChildEvent>> futures = new LinkedList<>();

        for (final Map<String, String> childEventMap : childEventMapList){
            Callable<IChildEvent> callable = new Callable<IChildEvent>() {
                @Override
                public IChildEvent call() throws Exception {
                    return ConvertChildEvent(childEventMap);
                }
            };
            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IChildEvent> future : futures){
            try {
                childEventList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return childEventList;
    }



    private static List<IReview> getObjectsReviews(Integer objectID, DatabaseTable table) throws IOException {
        List<IReview> reviewList = new LinkedList<>();
        List<Map<String, String>> reviewMapList = APIConnection.readObjectsReviews(table, objectID);
        for (Map<String, String> reviewsMap : reviewMapList){
            reviewList.add(ConvertArtistReview(reviewsMap));
        }
        return reviewList;
    }

    public static List<IVenue> getVenueAmount(Integer amount, Integer lastID) throws IOException {
        List<IVenue> venueList = new LinkedList<>();
        List<Map<String, String>> venueMapList = APIConnection.readAmount(DatabaseTable.VENUE, amount, lastID);


        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IVenue>> futures = new LinkedList<>();

        for (final Map<String, String> venueMap : venueMapList) {
            Callable<IVenue> callable = new Callable<IVenue>() {
                @Override
                public IVenue call() throws Exception {
                    IVenue venue;
                    venue = ConvertVenue(venueMap);
                    venue.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                    return venue;
                }
            };
            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IVenue> future : futures){
            try {
                venueList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return venueList;
    }

//    private static Customer populateCustomer(Map<String, String> cust, List<Order> order, List<Review> reviews){
//
//    }
    
    //Returns All artists from the database
    // Don't use in competed product!
//    public static List<Artist> getAllArtists()
//    {
//        List<Artist> listOfArtists = new LinkedList();
//
//        List<Map<String,String>> listOfMaps = APIConnection.readAll(DatabaseTable.ARTIST);
//
//        for(Map<String, String> currMap : listOfMaps)
//            listOfArtists.add(MapToObject.ConvertArtist(currMap));
//
//        return listOfArtists;
//    }
//
//
//    public static List<ParentEvent> getAllParentEvents()
//    {
//      List<ParentEvent> listOfEvents = new LinkedList();
//      List<Map<String,String>> listOfMaps = APIConnection.readAll(DatabaseTable.PARENT_EVENT);
//
//      for(Map<String,String> currEvent : listOfMaps)
//      {
//          listOfEvents.add(MapToObject.ConvertParentEvent(currEvent));
//      }


//      return listOfEvents;
//    }
//
//    public static List<Customer> getAllCustomers()
//    {
//        List<Customer> listOfCustomers = new LinkedList();
//        List<Map<String,String>> listOfMaps = APIConnection.readAll(DatabaseTable.CUSTOMER);
//
//        for(Map<String,String> currCustomer : listOfMaps)
//        {
//            listOfCustomers.add(MapToObject.ConvertCustomer(currCustomer));
//        }
//
//        return listOfCustomers;
//    }
    
    
    
}
    
