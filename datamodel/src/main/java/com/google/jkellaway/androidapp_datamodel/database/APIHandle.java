/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import com.google.jkellaway.androidapp_datamodel.bookings.CustomerBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.GuestBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.events.SocialMedia;
import com.google.jkellaway.androidapp_datamodel.people.IPerson;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToAdmin;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToArtist;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToArtistReview;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToArtistType;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToChildEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToCustomer;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToCustomerBooking;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToGuestBooking;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToOrder;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToParentEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToSocialMedia;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToTicket;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.MapToVenue;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.artistToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.childEventToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.customerBookingToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.customerToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.guestBookingToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.orderToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.parentEventToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.socialMediaToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.ticketToMap;
import static com.google.jkellaway.androidapp_datamodel.database.ObjectToMap.venueToMap;
import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 *
 */
public final class APIHandle{

    public static List<IUser> getUsers() throws IOException {

           List<IUser> userList = new LinkedList<>();
           List<Map<String, String>> userMapList = APIConnection.readAll(DatabaseTable.CUSTOMER);
           for (Map<String, String> user : userMapList) {
                 userList.add(MapToCustomer(user));
            }
            return userList;
        }

    public static IPerson isPasswordTrue(String email, String password, DatabaseTable table) throws IOException, IllegalArgumentException {
        switch (table) {
            case CUSTOMER:
            Map<String, String> customer = APIConnection.comparePassword(email, Encrypt(password), DatabaseTable.CUSTOMER).get(0);
            if (Integer.parseInt(customer.get("CUSTOMER_ID")) != -1)
                return MapToCustomer(customer);
            case ADMIN:
                Map<String, String> admin = APIConnection.comparePassword(email, Encrypt(password), DatabaseTable.ADMIN).get(0);
                if (Integer.parseInt(admin.get("ADMIN_ID")) != -1)
                    return MapToAdmin(admin);
        }
        throw new IllegalArgumentException("Email or password is wrong");
    }

    public static Object getSingle(int id, DatabaseTable table) throws IOException{
        Map<String, String> objMap = APIConnection.readSingle(id, table);
        switch (table){
            case ADMIN: return MapToObject.MapToAdmin(objMap);
            case BOOKING: return MapToObject.MapToCustomerBooking(objMap);
            case CUSTOMER: return MapToObject.MapToCustomer(objMap);
            case GUEST_BOOKING: return MapToObject.MapToGuestBooking(objMap);
            case ORDER: return MapToObject.MapToOrder(objMap);
            case SOCIAL_MEDIA: return MapToObject.MapToSocialMedia(objMap);
            case TICKET: return MapToObject.MapToTicket(objMap);
            case ARTIST_TYPE: return MapToObject.MapToArtistType(objMap);
            case CHILD_EVENT: return MapToObject.MapToChildEvent(objMap);

            case PARENT_EVENT:
                IParentEvent parentEvent;
                parentEvent = MapToParentEvent(objMap);
                parentEvent.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return parentEvent;
            case VENUE:
                IVenue venue;
                venue = MapToVenue(objMap);
                venue.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return venue;
            case ARTIST:
                IArtist artist = MapToArtist(objMap);
                artist.setType(MapToArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                artist.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return artist;
            default: throw new IllegalArgumentException("These tables are not supported");
        }
    }

    public static List<Object> searchObjects(String search, Integer amount, final DatabaseTable table) throws IOException {
        List<Object> objectList = new LinkedList<>();
        List<Map<String, String>> objectMapList = APIConnection.search(search, amount, table);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<Object>> futures = new LinkedList<>();

        for (final Map<String, String> objectMap : objectMapList){
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    switch (table){
                        case PARENT_EVENT:
                            IParentEvent parentEvent;
                            parentEvent = MapToParentEvent(objectMap);
                            parentEvent.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case VENUE:
                            IVenue venue;
                            venue = MapToVenue(objectMap);
                            venue.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return venue;
                        case ARTIST:
                            IArtist artist = MapToArtist(objectMap);
                            artist.setType(MapToArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                            artist.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return artist;
                        case CUSTOMER:
                            return MapToCustomer(objectMap);
                        default: throw new IllegalArgumentException();
                    }
                }
            };
            futures.add(service.submit(callable));
        }
        service.shutdown();

        for (Future<Object> future : futures){
            try {
                objectList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    public static List<IBooking> getBookingAmount(Integer orderID, Integer amount, Integer lowestID) throws IOException {
        List<IBooking> objectList = new LinkedList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<IBooking>> futures = new LinkedList<>();
        List<Map<String, String>> objectMapList;

        objectMapList = APIConnection.readTicketAmount(orderID, amount, lowestID);

        for (final Map<String, String> objectMap : objectMapList) {
            Callable<IBooking> callable = new Callable<IBooking>() {
                @Override
                public IBooking call() throws Exception {
                    return MapToCustomerBooking(objectMap);
                }
            };

            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<IBooking> future : futures){
            try {
                objectList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    public static List<Object> getObjectAmount(Integer amount, Integer lastID, final DatabaseTable table) throws IOException {

        List<Object> objectList = new LinkedList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<Object>> futures = new LinkedList<>();
        List<Map<String, String>> objectMapList;

        objectMapList = APIConnection.readAmount(table, amount, lastID);


        for (final Map<String, String> objectMap : objectMapList) {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    switch (table){
                        case ARTIST:
                            IArtist artist;
                            artist = MapToArtist(objectMap);
                            artist.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            artist.setType(MapToArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                            return artist;
                        case PARENT_EVENT:
                            IParentEvent parentEvent;
                            parentEvent = MapToParentEvent(objectMap);
                            parentEvent.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case VENUE:
                            IVenue venue;
                            venue = MapToVenue(objectMap);
                            venue.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return venue;
                        case CUSTOMER:
                            return MapToCustomer(objectMap);
                        case GUEST_BOOKING:
                            return MapToGuestBooking(objectMap);
                        case ADMIN:
                            return MapToAdmin(objectMap);
                        default: throw new IllegalArgumentException();
                    }

                }
            };

            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<Object> future : futures){
            try {
                objectList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    public static List<Object> getObjectsFromObject(final int parentID, final DatabaseTable objectsToGet, DatabaseTable object) throws IOException {
        List<Map<String, String>> objectMapList = APIConnection.getObjectsOfObject(parentID, objectsToGet, object);

        List<Object> objectList = new LinkedList<>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<Object>> futures = new LinkedList<>();

        for (final Map<String, String> objectMap : objectMapList){
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    switch (objectsToGet){
                        case CHILD_EVENT:
                            IChildEvent childEvent = MapToChildEvent(objectMap);
                            childEvent.getParentEvent();
                            return childEvent;
                        case ARTIST:
                            IArtist artist = MapToArtist(objectMap);
                            artist.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return artist;
                        case BOOKING:
                            return MapToCustomerBooking(objectMap);
                        case CUSTOMER:
                            return MapToCustomer(objectMap);
                        case GUEST_BOOKING:
                            return MapToGuestBooking(objectMap);
                        case PARENT_EVENT:
                            IParentEvent parentEvent =  MapToParentEvent(objectMap);
                            parentEvent.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case TICKET:
                            return MapToTicket(objectMap);
                        case VENUE:
                            IVenue venue =  MapToVenue(objectMap);
                            venue.setSocialMedia(MapToSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return venue;
                        case ORDER:
                            return MapToOrder(objectMap);
                        default: throw new IllegalArgumentException();
                    }
                }
            };
            futures.add(service.submit(callable));
        }

        service.shutdown();

        for (Future<Object> future : futures){
            try {
                objectList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return objectList;
    }

    private static List<IReview> getObjectsReviews(Integer objectID, DatabaseTable table) throws IOException {
        List<IReview> reviewList = new LinkedList<>();
        List<Map<String, String>> reviewMapList = APIConnection.readObjectsReviews(table, objectID);
        for (Map<String, String> reviewsMap : reviewMapList){
            reviewList.add(MapToArtistReview(reviewsMap));
        }
        return reviewList;
    }

    public static Object pushObjectToDatabase(Object object, DatabaseTable table) throws IOException{
        switch (table){
//            case ADMIN:
//                //objectMap = AdminToMap
//                break;
            case ARTIST:
                IArtist artist = (IArtist) object;
                artist.setSocialMedia((SocialMedia)pushObjectToDatabase(artist.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToArtist(APIConnection.add(artistToMap((IArtist) object), table));
//            case ARTIST_TYPE:
//                //objectMap = artistTypeToMap();
//            case ARTIST_REVIEW:
//                break;
            case BOOKING:
                return MapToCustomerBooking(APIConnection.add(customerBookingToMap((CustomerBooking) object), table));
            case CHILD_EVENT:
                return MapToChildEvent(APIConnection.add(childEventToMap((IChildEvent) object), table));
            case CUSTOMER:
                return MapToCustomer(APIConnection.add(customerToMap((IUser) object), table));
//            case PARENT_EVENT_REVIEW:
//                break;
            case GUEST_BOOKING:
                return MapToGuestBooking(APIConnection.add(guestBookingToMap((GuestBooking) object), table));
            case PARENT_EVENT:
                IParentEvent parentEvent = (IParentEvent) object;
                parentEvent.setSocialMedia((SocialMedia) pushObjectToDatabase(parentEvent.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToParentEvent(APIConnection.add(parentEventToMap((IParentEvent) object), table));
            case SOCIAL_MEDIA:
                return MapToSocialMedia(APIConnection.add(socialMediaToMap((SocialMedia) object), table));
            case TICKET:
                return MapToTicket(APIConnection.add(ticketToMap((ITicket) object), table));
            case VENUE:
                IVenue venue = (IVenue) object;
                venue.setSocialMedia((SocialMedia) pushObjectToDatabase(venue.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToVenue(APIConnection.add(venueToMap(venue), table));
//            case VENUE_REVIEW:
//                break;
            case ORDER:
                return MapToOrder(APIConnection.add(orderToMap((IOrder) object), table));
            default: throw new IllegalArgumentException("Not supported table.");
        }
    }

    public static Object updateObjectToDatabase(Object object, DatabaseTable table) throws IOException{
        switch (table){
//            case ADMIN:
//                //objectMap = AdminToMap
//                break;
            case ARTIST:
                IArtist artist = (IArtist) object;
                artist.setSocialMedia((SocialMedia) updateObjectToDatabase(artist.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToArtist(APIConnection.update(((IArtist) object).getID(), artistToMap((IArtist) object), table));
//            case ARTIST_TYPE:
//                //objectMap = artistTypeToMap();
//            case ARTIST_REVIEW:
//                break;
            case BOOKING:
                return MapToCustomerBooking(APIConnection.update(((CustomerBooking) object).getBookingID(),customerBookingToMap((CustomerBooking) object), table));
            case CHILD_EVENT:
                return MapToChildEvent(APIConnection.update(((IChildEvent) object).getID(), childEventToMap((IChildEvent) object), table));
            case CUSTOMER:
                return MapToCustomer(APIConnection.update(((IUser) object).getID(), customerToMap((IUser) object), table));
//            case PARENT_EVENT_REVIEW:
//                break;
            case GUEST_BOOKING:
                return MapToGuestBooking(APIConnection.update(((GuestBooking) object).getBookingID(), guestBookingToMap((GuestBooking) object), table));
            case PARENT_EVENT:
                IParentEvent parentEvent = (IParentEvent) object;
                parentEvent.setSocialMedia((SocialMedia)updateObjectToDatabase(parentEvent.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToParentEvent(APIConnection.update(((IParentEvent) object).getID(), parentEventToMap((IParentEvent) object), table));
            case SOCIAL_MEDIA:
                return MapToSocialMedia(APIConnection.update(((SocialMedia) object).getSocialId(), socialMediaToMap((SocialMedia) object), table));
            case TICKET:
                return MapToTicket(APIConnection.update(((ITicket) object).getID(), ticketToMap((ITicket) object), table));
            case VENUE:
                IVenue venue = (IVenue) object;
                venue.setSocialMedia((SocialMedia)updateObjectToDatabase(venue.getSocialMedia(), DatabaseTable.SOCIAL_MEDIA));
                return MapToVenue(APIConnection.update(((IVenue) object).getID(), venueToMap(venue), table));
//            case VENUE_REVIEW:
//                break;
            case ORDER:
                return MapToOrder(APIConnection.update(((IOrder) object).getOrderID(), orderToMap((IOrder) object), table));
            default: throw new IllegalArgumentException("Not supported table.");
        }
    }

    public static Boolean createContract(int artistID, int childEventID) throws IOException {
        return APIConnection.createContract(artistID, childEventID);
    }
}