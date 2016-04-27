/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import android.util.Log;

import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.utilities.HashString;

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
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistType;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertChildEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertCustomer;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertCustomerBooking;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertGuestBooking;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertOrder;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertParentEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertSocialMedia;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertTicket;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertVenue;
import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 *
 */
public final class APIHandle {

    public static IUser isPasswordTrue(String email, String password) throws IOException, IllegalArgumentException {
        Map<String, String> customer = APIConnection.comparePassword(email, Encrypt(password)).get(0);
        if (Integer.parseInt(customer.get("CUSTOMER_ID").toString()) != -1)
            return ConvertCustomer(customer);
        else
            throw new IllegalArgumentException("Email or password is wrong");
    }

    public static Object getSingle(int id, DatabaseTable table) throws IOException{
        Map<String, String> objMap = APIConnection.readSingle(id, table);
        switch (table){
            case ADMIN: return MapToObject.ConvertAdmin(objMap);
            case BOOKING: return MapToObject.ConvertCustomerBooking(objMap);
            case CUSTOMER: return MapToObject.ConvertCustomer(objMap);
            case GUEST_BOOKING: return MapToObject.ConvertGuestBooking(objMap);
            case ORDER: return MapToObject.ConvertOrder(objMap);
            case SOCIAL_MEDIA: return MapToObject.ConvertSocialMedia(objMap);
            case TICKET: return MapToObject.ConvertTicket(objMap);
            case PARENT_EVENT:
                IParentEvent parentEvent;
                parentEvent = ConvertParentEvent(objMap);
                parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return parentEvent;
            case VENUE:
                IVenue venue;
                venue = ConvertVenue(objMap);
                venue.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return venue;
            case ARTIST:
                IArtist artist = ConvertArtist(objMap);
                artist.setType(ConvertArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                return artist;
            default: throw new IllegalArgumentException("These tables are not supported");
        }
    }

    public static int registerUser(IUser newUser, String password) throws IOException {
        Map<String, String> customerMap = ObjectToMap.ConvertCustomer(newUser);
        customerMap.put("CUSTOMER_PASSWORD", Encrypt(password));
        return APIConnection.add(customerMap, DatabaseTable.CUSTOMER);
    }


    public static List<IAdmin> getAdmins() throws IOException {

        List<IAdmin> adminList = new LinkedList<>();
        List<Map<String, String>> adminMapList = APIConnection.readAll(DatabaseTable.ADMIN);
        for (Map<String, String> admin : adminMapList) {
            adminList.add(ConvertAdmin(admin));
        }
        return adminList;
    }

    public static List<Object> searchObjects(String search, final DatabaseTable table) throws IOException {
        List<Object> objectList = new LinkedList<>();
        List<Map<String, String>> objectMapList = APIConnection.search(search, table);

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
                            parentEvent = ConvertParentEvent(objectMap);
                            parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case VENUE:
                            IVenue venue;
                            venue = ConvertVenue(objectMap);
                            venue.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return venue;
                        case ARTIST:
                            IArtist artist = ConvertArtist(objectMap);
                            artist.setType(ConvertArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                            artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return artist;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
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

        List<Map<String, String>> objectMapList = APIConnection.readAmount(table, amount, lastID);


        for (final Map<String, String> objectMap : objectMapList) {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    switch (table){
                        case ARTIST:
                            IArtist artist;
                            artist = ConvertArtist(objectMap);
                            artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            artist.setType(ConvertArtistType(APIConnection.readSingle(artist.getTypeID(), DatabaseTable.ARTIST_TYPE)));
                            return artist;
                        case PARENT_EVENT:
                            IParentEvent parentEvent;
                            parentEvent = ConvertParentEvent(objectMap);
                            parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case VENUE:
                            IVenue venue;
                            venue = ConvertVenue(objectMap);
                            venue.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return venue;
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
                            IChildEvent childEvent = ConvertChildEvent(objectMap, parentID);
                            childEvent.getParentEvent();
                            return childEvent;
                        case ARTIST:
                            IArtist artist = ConvertArtist(objectMap);
                            artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return artist;
                        case BOOKING:
                            return ConvertCustomerBooking(objectMap);
                        case CUSTOMER:
                            return ConvertCustomer(objectMap);
                        case GUEST_BOOKING:
                            return ConvertGuestBooking(objectMap);
                        case PARENT_EVENT:
                            IParentEvent parentEvent =  ConvertParentEvent(objectMap);
                            parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                            return parentEvent;
                        case TICKET:
                            return ConvertTicket(objectMap);
                        case VENUE:
                            IVenue venue =  ConvertVenue(objectMap);
                            venue.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(venue.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
                        case ORDER:
                            return ConvertOrder(objectMap);
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
            reviewList.add(ConvertArtistReview(reviewsMap));
        }
        return reviewList;
    }
}