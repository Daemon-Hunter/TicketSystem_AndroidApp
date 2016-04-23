/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
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
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistType;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistReview;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertChildEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertCustomer;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertParentEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertSocialMedia;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertVenue;

/**
 *
 */
public final class APIHandle {

    public static List<IChildEvent> getChildEventsOfVenue(Integer venueID) throws IOException {
        List<IChildEvent> childEventList = new LinkedList<>();
        List<Map<String, String>> childEventMapList = APIConnection.getChildeventIDsForVenue(venueID);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IChildEvent>> futures = new LinkedList<>();

        for (final Map<String, String> childEvent : childEventMapList){
            Callable<IChildEvent> callable = new Callable<IChildEvent>() {
                @Override
                public IChildEvent call() throws Exception {
                    return ConvertChildEvent(childEvent);
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

    public static IUser isPasswordtrue(String email, String password) throws IOException, IllegalArgumentException {
        Map<String, String> customer = APIConnection.comparePassword(email, password).get(0);
        if (customer != null)
            return ConvertCustomer(customer);
        else
            throw new IllegalArgumentException("Email or password is wrong");
    }

    public static Object getSingle(int id, DatabaseTable table){
        Map<String, String> objMap = APIConnection.readSingle(id, table);
        switch (table){
            case ADMIN: MapToObject.ConvertAdmin(objMap);break;
            case ARTIST: MapToObject.ConvertArtist(objMap);break;
            //case BOOKING: MapToObject.ConvertCustomerBooking(objMap);break;
            case CHILD_EVENT: MapToObject.ConvertChildEvent(objMap);break;
            case CUSTOMER: MapToObject.ConvertCustomer(objMap);break;
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

    public static List<IVenue> searchVenues(String search) throws IOException {
        List<IVenue> venueList = new LinkedList<>();
        List<Map<String, String>> venueMapList = APIConnection.search(search, DatabaseTable.VENUE);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IVenue>> futures = new LinkedList<>();

        for (final Map<String, String> venue : venueMapList){
            Callable<IVenue> callable = new Callable<IVenue>() {
                @Override
                public IVenue call() throws Exception {
                    return ConvertVenue(venue);
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

    public static List<IArtist> searchArtists(String search) throws IOException {
        List<IArtist> artistList = new LinkedList<>();
        List<Map<String, String>> artistMapList = APIConnection.search(search, DatabaseTable.ARTIST);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IArtist>> futures = new LinkedList<>();

        for (final Map<String, String> artistMap : artistMapList){
            Callable<IArtist> callable = new Callable<IArtist>() {
                @Override
                public IArtist call() throws Exception {
                    IArtist artist = ConvertArtist(artistMap);
                    artist.setType(ConvertArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
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
                    parentEvent.addChildEventList(getChildEventFromParent(parentEvent.getID()));
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

    public static List<IChildEvent> getChildEventsViaContract(Integer artistID) throws IOException {
        List<IChildEvent> childEventList = new LinkedList<>();
        List<Map<String, String>> childEventMapList = APIConnection.getChildEventsViaContract(artistID);


        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IChildEvent>> futures = new LinkedList<>();

        for (final Map<String, String> childEventMap : childEventMapList) {
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

    public static List<IArtist> getArtistsViaContract(Integer childEventID) throws IOException {
        List<IArtist> artistList = new LinkedList<>();
        List<Map<String, String>> artistMapList = APIConnection.getChildEventsViaContract(childEventID);


        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IArtist>> futures = new LinkedList<>();

        for (final Map<String, String> artistMap : artistMapList) {
            Callable<IArtist> callable = new Callable<IArtist>() {
                @Override
                public IArtist call() throws Exception {
                    return ConvertArtist(artistMap);
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
}