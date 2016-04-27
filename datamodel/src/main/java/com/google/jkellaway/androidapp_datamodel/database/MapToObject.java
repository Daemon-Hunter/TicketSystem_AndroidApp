/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.jkellaway.androidapp_datamodel.bookings.CustomerBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.GuestBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.bookings.Order;
import com.google.jkellaway.androidapp_datamodel.events.Artist;
import com.google.jkellaway.androidapp_datamodel.events.ChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.events.ParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.SocialMedia;
import com.google.jkellaway.androidapp_datamodel.events.Venue;
import com.google.jkellaway.androidapp_datamodel.people.Admin;
import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.Guest;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.reviews.ArtistReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.reviews.IReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.ParentEventReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.VenueReviewFactory;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.tickets.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 *
 *
 */
final class MapToObject {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    public static IUser ConvertCustomer(Map<String, String> custMap) {
        String firstName, lastName, address, email, postcode;
        int ID = Integer.parseInt(custMap.get("CUSTOMER_ID"));

        firstName = custMap.get("CUSTOMER_FIRST_NAME");
        lastName = custMap.get("CUSTOMER_LAST_NAME");
        address = custMap.get("CUSTOMER_ADDRESS");
        email = custMap.get("CUSTOMER_EMAIL");
        postcode = custMap.get("CUSTOMER_POSTCODE");

        return new Customer(ID, firstName, lastName, email, address, postcode);
    }

    public static Artist ConvertArtist(Map<String, String> artistMap) {
        List<Integer> childEventIDs = new LinkedList<>();

        Integer ID = Integer.parseInt(artistMap.get("ARTIST_ID"));
        String name = artistMap.get("ARTIST_NAME");
        String tags = artistMap.get("ARTIST_TAGS");
        String[] tempArr = tags.split("#");
        String description = artistMap.get("ARTIST_DESCRIPTION");
        Integer socialID = Integer.parseInt(artistMap.get("SOCIAL_MEDIA_ID"));
        Integer type = Integer.parseInt(artistMap.get("ARTIST_TYPE_ID"));

        LinkedList<String> listOfTags = new LinkedList<>();
        listOfTags.addAll(Arrays.asList(tempArr));

        return new Artist(ID, name, description, listOfTags, socialID, type);
    }

    public static String ConvertArtistType(Map<String, String> artistTypeMap) {
        return artistTypeMap.get("ARTIST_TYPE1");
    }

    public static SocialMedia ConvertSocialMedia(Map<String, String> socialMap) {
        Integer socialMediaID;
        String facebook, twitter, instagram, soundcloud, website, spotify;
        byte[] decodedBytes;
        List<Bitmap> images = new LinkedList<>();

        socialMediaID = Integer.parseInt(socialMap.get("SOCIAL_MEDIA_ID"));
        facebook = socialMap.get("FACEBOOK");
        twitter = socialMap.get("TWITTER");
        instagram = socialMap.get("INSTAGRAM");
        soundcloud = socialMap.get("SOUNDCLOUD");
        website = socialMap.get("WEBSITE");
        spotify = socialMap.get("SPOTIFY");
        decodedBytes = Base64.decode(socialMap.get("IMAGE"), 0);
        images.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        decodedBytes = Base64.decode(socialMap.get("IMAGE2"), 0);
        images.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        decodedBytes = Base64.decode(socialMap.get("IMAGE3"), 0);
        images.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        decodedBytes = Base64.decode(socialMap.get("IMAGE4"), 0);
        images.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        decodedBytes = Base64.decode(socialMap.get("IMAGE5"), 0);
        images.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));

        return new SocialMedia(socialMediaID, images, facebook, twitter, instagram, soundcloud, website, spotify);
    }

    public static IReview ConvertArtistReview(Map<String, String> reviewMap) {

        ArtistReviewFactory factory = new ArtistReviewFactory();

        Integer artistID = Integer.parseInt(reviewMap.get("ARTIST_ID"));
        Integer customerID = Integer.parseInt(reviewMap.get("CUSTOMER_ID"));
        Integer rating = Integer.parseInt(reviewMap.get("ARTIST_REVIEW_RATING"));
        String body = reviewMap.get("ARTIST_REVIEW_BODY");
        Date dateTime = new Date();
        Boolean verified = reviewMap.get("ARTIST_REVIEW_VERIFIED").equals("true");

        try {
            dateTime = formatter.parse(reviewMap.get("ARTIST_REVIEW_DATE_TIME"));
        } catch (ParseException ex) {
            System.err.println(ex.toString());
        }

        return factory.createReview(artistID, customerID, rating, dateTime, body, verified);
    }

    public static IReview ConvertVenueReview(Map<String, String> reviewMap, DatabaseTable table) {

        IReviewFactory factory;
        if (table == DatabaseTable.VENUE)
            factory = new VenueReviewFactory();
        else if (table == DatabaseTable.ARTIST)
            factory = new ArtistReviewFactory();
        else if (table == DatabaseTable.PARENT_EVENT_REVIEW)
            factory = new ParentEventReviewFactory();
        else
            throw new IllegalArgumentException(table.toString() + " is not a valid table.");

        String sTable = table.toString().toUpperCase();

        Integer ID = Integer.parseInt(reviewMap.get(sTable + "_ID"));
        Integer customerID = Integer.parseInt(reviewMap.get("CUSTOMER_ID"));
        Integer rating = Integer.parseInt(reviewMap.get(sTable + "_RATING"));
        String body = reviewMap.get(sTable + "_BODY");
        Date dateTime = new Date();
        Boolean verified;

        verified = reviewMap.get(sTable + "_VERIFIED").equals("true");

        try {
            dateTime = formatter.parse(reviewMap.get(sTable + "_DATE_TIME"));
        } catch (ParseException ex) {
            System.err.println(ex.toString());
        }

        return factory.createReview(ID, customerID, rating, dateTime, body, verified);

    }

    public static IReview ConvertEventReview(Map<String, String> reviewMap) {

        ParentEventReviewFactory factory = new ParentEventReviewFactory();

        Integer eventID = Integer.parseInt(reviewMap.get("PARENT_EVENT_ID"));
        Integer customerID = Integer.parseInt(reviewMap.get("CUSTOMER_ID"));
        Integer rating = Integer.parseInt(reviewMap.get("EVENT_REVIEW_RATING"));
        String body = reviewMap.get("EVENT_REVIEW_BODY");
        Date dateTime = new Date();
        Boolean verified;

        verified = reviewMap.get("EVENT_REVIEW_VERIFIED").equals("true");

        try {
            dateTime = formatter.parse(reviewMap.get("EVENT_REVIEW_DATE_TIME"));
        } catch (ParseException ex) {
            System.err.println(ex.toString());
        }

        return factory.createReview(eventID, customerID, rating, dateTime, body, verified);
    }

    public static IVenue ConvertVenue(Map<String, String> venueMap) {

        Integer venueID, capSeating, capStanding, parking, socialMediaID;
        String description, facilities, phoneNumber, email, address, postcode, name;
        Boolean disabledAccess;

        venueID = Integer.parseInt(venueMap.get("VENUE_ID"));
        description = venueMap.get("VENUE_DESCRIPTION");
        capSeating = Integer.parseInt(venueMap.get("VENUE_CAPACITY_SEATING"));
        capStanding = Integer.parseInt(venueMap.get("VENUE_CAPACITY_STANDING"));
        parking = Integer.parseInt(venueMap.get("VENUE_PARKING"));
        facilities = venueMap.get("VENUE_FACILITES");
        phoneNumber = venueMap.get("VENUE_PHONE_NUMBER");
        email = venueMap.get("VENUE_EMAIL");
        address = venueMap.get("VENUE_ADDRESS");
        postcode = venueMap.get("VENUE_POSTCODE");
        name = venueMap.get("VENUE_NAME");
        socialMediaID = Integer.parseInt(venueMap.get("SOCIAL_MEDIA_ID"));

        disabledAccess = venueMap.get("VENUE_DISABLED_ACCESS").equals("true");


        return new Venue(venueID, socialMediaID, description, capSeating, capStanding, disabledAccess,
                facilities, parking, phoneNumber, email, address, postcode, name);
    }

    public static ITicket ConvertTicket(Map<String, String> ticketMap) {
        Integer ticketID, remaining, childEventID;
        Double price;
        String desc, type;

        ticketID = Integer.parseInt(ticketMap.get("TICKET_ID"));
        price = Double.parseDouble(ticketMap.get("TICKET_PRICE"));
        remaining = Integer.parseInt(ticketMap.get("TICKET_AMOUNT_REMAINING"));
        type = ticketMap.get("TICKET_TYPE");
        desc = ticketMap.get("TICKET_DESCRIPTION");
        childEventID = Integer.parseInt(ticketMap.get("TICKET_ID"));

        return new Ticket(ticketID, childEventID, price, desc, remaining, type);
    }

    public static IChildEvent ConvertChildEvent(Map<String, String> eventMap, Integer parentEventID) {

        Integer eventID, venueID;
        String description, name;
        Date startTime = new Date();
        Date endTime = new Date();
        Boolean cancelled = false;


        eventID = Integer.parseInt(eventMap.get("CHILD_EVENT_ID"));
        venueID = Integer.parseInt(eventMap.get("VENUE_ID"));
        name = eventMap.get("CHILD_EVENT_NAME");
        description = eventMap.get("CHILD_EVENT_DESCRIPTION");

        if (eventMap.get("CHILD_EVENT_CANCELED").equals("true"))
            cancelled = true;
        try {
            startTime = formatter.parse(eventMap.get("START_DATE_TIME"));
            endTime = formatter.parse(eventMap.get("END_DATE_TIME"));
        } catch (ParseException e) {
            System.err.println(e.toString());
        }
        return new ChildEvent(eventID, name, description, startTime, endTime, cancelled, parentEventID);
    }

    public static IBooking ConvertCustomerBooking(Map<String, String> bookingMap) {
        Integer bookingID, quantity, ticketID, orderID;
        Date date = new Date();

        bookingID = Integer.parseInt(bookingMap.get("BOOKING_ID"));
        quantity = Integer.parseInt(bookingMap.get("BOOKING_QUANTITY"));
        ticketID = Integer.parseInt(bookingMap.get("TICKET_ID"));
        orderID = Integer.parseInt(bookingMap.get("ORDER_ID"));

        try {
            date = formatter.parse(bookingMap.get("BOOKING_DATE_TIME"));
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

        return new CustomerBooking(bookingID, ticketID, orderID, quantity, date);
    }

    public static IOrder ConvertOrder(Map<String, String> orderMap) {

        Integer orderID, userID;

        orderID = parseInt(orderMap.get("ORDER_ID"));
        userID = parseInt(orderMap.get("CUSTOMER_ID"));

        return new Order(orderID, userID);
    }

    public static IBooking ConvertGuestBooking(Map<String, String> bookingMap) {
        Integer bookingID, ticketID, quantity;
        String email, address, postcode;
        Date dateTime = new Date();

        bookingID = Integer.parseInt(bookingMap.get("GUEST_BOOKING_ID"));
        ticketID = Integer.parseInt(bookingMap.get("TICKET_ID"));
        quantity = Integer.parseInt(bookingMap.get("GUEST_BOOKING_QUANTITY"));
        email = bookingMap.get("GUEST_EMAIL");
        address = bookingMap.get("GUEST_ADDRESS");
        postcode = bookingMap.get("GUEST_POSTCODE");

        try {
            dateTime = formatter.parse(bookingMap.get("GUEST_BOOKING_DATE_TIME"));
        } catch (ParseException e) {
            System.err.println(e.toString());
        }

        return new GuestBooking(bookingID, ticketID, quantity, dateTime, new Guest("GUEST", "ACCOUNT", email, address, postcode));
    }

    public static IParentEvent ConvertParentEvent(Map<String, String> eventMap) {
        Integer eventID, socialMediaID;
        String name, description;

        eventID = Integer.parseInt(eventMap.get("PARENT_EVENT_ID"));
        name = eventMap.get("PARENT_EVENT_NAME");
        description = eventMap.get("PARENT_EVENT_DESCRIPTION");
        socialMediaID = Integer.parseInt(eventMap.get("SOCIAL_MEDIA_ID"));

        return new ParentEvent(eventID, socialMediaID, name, description);
    }

    public static IAdmin ConvertAdmin(Map<String, String> adminMap){
        Integer adminID = Integer.parseInt(adminMap.get("ADMIN_ID"));
        String email = adminMap.get("ADMIN_EMAIL");

        return new Admin(adminID, "ADMIN", "ADMIN", email);
    }

    public static Integer[] ConvertContract(Map<String, String> contractMap){
        Integer artistID = Integer.parseInt(contractMap.get("ADMIN_ID"));
        Integer child_event_id = Integer.parseInt(contractMap.get("CHILD_EVENT_ID"));
        return new Integer[] {artistID, child_event_id};
    }
}
