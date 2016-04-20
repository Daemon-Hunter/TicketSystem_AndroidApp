/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import android.util.Log;

import com.google.jkellaway.androidapp_datamodel.bookings.Order;
import com.google.jkellaway.androidapp_datamodel.datamodel.Artist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.ParentEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.jkellaway.androidapp_datamodel.datamodel.SocialMedia;
import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.reviews.Review;

import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertAdmin;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtist;
import static com.google.jkellaway.androidapp_datamodel.database.MapToObject.ConvertArtistReview;
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
        Log.d("API Handle","getArtists");
        List<IArtist> artistList = new LinkedList<>();
        List<Map<String, String>> artistMapList = APIConnection.readAmount(DatabaseTable.ARTIST, amount, lastID);
        IArtist artist;
        Log.d("API Handle",artistMapList.toString());

        for (Map<String, String> artistMap : artistMapList) {
            Log.d("API Handle", "In loop");
            artist = ConvertArtist(artistMap);
            Log.d("API Handle", artist.toString());
            artist.setSocialMedia(ConvertSocialMedia(APIConnection.readSingle(artist.getSocialId(), DatabaseTable.SOCIAL_MEDIA)));
            artistList.add(artist);
            Log.d("API Handle", artistList.toString());
        }

        Log.d("Handle after loop", "%d "+ artistList.size());

        return artistList;
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
    
