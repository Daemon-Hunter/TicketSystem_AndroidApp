/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertAdmin;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtist;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistReview;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertChildEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertParentEvent;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertSocialMedia;

/**
 *
 * @author Dominic
 */
public final class APIHandle {

    // Artists and Child Events can request each other

    public static Object getSingle(int id, DatabaseTable table){
        return MapToObject.ConvertArtist(APIConnection.readSingle(id, table));
    }

    public static List<IAdmin> getAdmins() {

        List<IAdmin> adminList = new LinkedList<>();
        List<Map<String, String>> adminMapList = APIConnection.readAll(DatabaseTable.ADMIN);
        for (Map<String, String> admin : adminMapList) {
            adminList.add(ConvertAdmin(admin));
        }
        return adminList;
    }

    public static List<IArtist> getArtistAmount(Integer amount, Integer lastID) {
        List<IArtist> artistList = new LinkedList<>();
        List<Map<String, String>> artistMapList = APIConnection.readAmount(DatabaseTable.ARTIST, amount, lastID);
        IArtist artist;

        for (Map<String, String> artistMap : artistMapList) {
            artist = ConvertArtist(artistMap);
            artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
            artistList.add(artist);
        }
        return artistList;
    }

    public static List<IParentEvent> getParentAmount(Integer amount, Integer lastID) {
        List<IParentEvent> parentEventList = new LinkedList<>();
        List<Map<String, String>> parentEventMapList = APIConnection.readAmount(DatabaseTable.PARENT_EVENT, amount, lastID);
        IParentEvent parentEvent;

        for (Map<String, String> parentEventMap : parentEventMapList) {
            parentEvent = ConvertParentEvent(parentEventMap);
            parentEvent.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(parentEvent.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
            parentEvent.addChildEventList(getChildEventFromParent(parentEvent.getParentEventID()));

            parentEventList.add(parentEvent);
        }
        return parentEventList;
    }

    private static List<IChildEvent> getChildEventFromParent(int parentID){
        List<Map<String, String>> childEventMapList = APIConnection.getChildEventsViaParent(parentID);
        List<IChildEvent> childEventList = new LinkedList<>();

        for (Map<String, String> childEventMap : childEventMapList){
            childEventList.add(ConvertChildEvent(childEventMap));
        }
        return childEventList;
    }

    private static List<IReview> getObjectsReviews(Integer objectID, DatabaseTable table){
        List<IReview> reviewList = new LinkedList<>();
        List<Map<String, String>> reviewMapList = APIConnection.readObjectsReviews(table, objectID);
        for (Map<String, String> reviewsMap : reviewMapList){
            reviewList.add(ConvertArtistReview(reviewsMap));
        }
        return reviewList;
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
    
