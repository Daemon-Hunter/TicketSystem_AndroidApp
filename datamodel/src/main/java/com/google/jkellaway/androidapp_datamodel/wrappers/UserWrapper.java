/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.CustomerBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.bookings.Order;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.getObjectAmount;
import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.pushObjectToDatabase;

/**
 *
 * @author 10467841
 */
public class UserWrapper implements IUserWrapper {

    private static UserWrapper wrapper;

    private static Integer amountToLoad = 9;

    private List<IParentEvent> parentEventList;
    private List<IVenue> venueList;
    private List<IArtist> artistList;

    private List<IParentEvent> parentEventSearchList;
    private List<IVenue> venueSearchList;
    private List<IArtist> artistSearchList;

    private IUser currentUser;

    private UserWrapper() {}

    public static UserWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new UserWrapper();
        }
        return wrapper;
    }

    @Override
    public Boolean loginUser(String email, String password) throws IOException, IllegalArgumentException {

        currentUser = (IUser) APIHandle.isPasswordTrue(email, password, DatabaseTable.CUSTOMER);
        // if the passwords are incorrect then the returning Customer has an id of -1
        return !currentUser.getID().equals(-1);

    }

    @Override
    public IUser getUser() {
        return this.currentUser;
    }

    @Override
    public IUser registerUser(IUser customer) throws IOException {
        return (IUser) APIHandle.pushObjectToDatabase(customer, DatabaseTable.CUSTOMER);
    }

    @Override
    public IOrder makeCustomerBooking(List<ITicket> tickets, List<Integer> quantities) throws IOException {
        IOrder order = (IOrder) pushObjectToDatabase(new Order(currentUser.getID()),DatabaseTable.ORDER);
        IBooking booking;
        int i = 0;
        for (ITicket ticket: tickets){
            booking = new CustomerBooking(order, ticket, quantities.get(i));
            booking = (IBooking) APIHandle.pushObjectToDatabase(booking, DatabaseTable.BOOKING);
            order.addBooking(booking);
            i++;
        }
        return order;

    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventList != null){
            return new LinkedList<>(parentEventList);
        } else {
            parentEventList = (List<IParentEvent>)(Object) getObjectAmount(amountToLoad, 0,
                    DatabaseTable.PARENT_EVENT);
            return new LinkedList<>(parentEventList);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 0;
        for (IParentEvent parentEvent : parentEventList){
            if (parentEvent.getID() < lowestID || lowestID == 0)
                lowestID = parentEvent.getID();
        }
        List<IParentEvent> newData = (List<IParentEvent>)(Object) getObjectAmount(amountToLoad, lowestID, DatabaseTable.PARENT_EVENT);
        parentEventList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) throws IOException {
        if (parentEventList != null) {
            for (IParentEvent parentEvent : parentEventList) {
                if (parentEvent.getID().equals(id))
                    return parentEvent;
            }
        }
        if (parentEventSearchList != null) {
            for (IParentEvent parentEvent : parentEventSearchList) {
                if (parentEvent.getID().equals(id))
                    return parentEvent;
            }
        }
        return (IParentEvent) APIHandle.getSingle(id, DatabaseTable.PARENT_EVENT);
    }

    @Override
    public Boolean addParentEvent(IParentEvent parentEvent) {
        if (parentEvent == null || parentEvent.getID() <= 0){
            throw new IllegalArgumentException("This parentEvent cannot be added, have to put it though createNewObject?");
        }
        return parentEventList.add(parentEvent);
    }

    @Override
    public Boolean removeParentEvent(IParentEvent pEvent) {
        if (pEvent == null){
            throw new IllegalArgumentException("Cannot remove null value.");
        }
        return parentEventList.remove(pEvent);
    }

    @Override
    public List<IParentEvent> refreshParentEvents() throws IOException {
        parentEventList = (List<IParentEvent>)(Object) getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT);
        return new LinkedList<>(parentEventList);
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        parentEventSearchList = (List<IParentEvent>)(Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.PARENT_EVENT);
        return parentEventSearchList;
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueList != null){
            return new LinkedList<>(venueList);
        } else {
            venueList = (List<IVenue>) (Object) getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE);
            return venueList;
        }
    }

    @Override
    public IVenue getVenue(Integer id) throws IOException {
        if (venueList != null) {
            for (IVenue venue : venueList) {
                if (venue.getID().equals(id))
                    return venue;
            }
        }
        if (venueSearchList != null) {
            for (IVenue venue : venueSearchList) {
                if (venue.getID().equals(id))
                    return venue;
            }
        }
        return (IVenue) APIHandle.getSingle(id, DatabaseTable.VENUE);
    }

    @Override
    public List<IVenue> loadMoreVenues() throws IOException {
        int lowestID = 0;
        for (IVenue venue : venueList){
            if (venue.getID() < lowestID || lowestID == 0)
                lowestID = venue.getID();
        }
        List<IVenue> newData = (List<IVenue>) (Object) getObjectAmount(amountToLoad, lowestID, DatabaseTable.VENUE);
        venueList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public Boolean addVenue(IVenue venue) {
        if (venue.getID() <= 0 || venue == null){
            throw new IllegalArgumentException("This venue cannot be added, have to put it though createNewObject?");
        }
        return venueList.add(venue);
    }

    @Override
    public Boolean removeVenue(IVenue venue) {
        if(venue == null){
            throw new IllegalArgumentException("Cannot remove a null venue.");
        }
        return venueList.remove(venue);
    }

    @Override
    public List<IVenue> refreshVenues() throws IOException {
        venueList = (List<IVenue>)(Object) getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE);
        return new LinkedList<>(venueList);
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        venueSearchList = (List<IVenue>)(Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.VENUE);
        return venueSearchList;
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistList != null){
            return new LinkedList<>(artistList);
        } else {
            artistList = (List<IArtist>)(Object) getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
            return new LinkedList<>(artistList);
        }
    }

    @Override
    public List<IArtist> loadMoreArtists() throws IOException {
        int lowestID = 0;
        for (IArtist artist : artistList){
            if (artist.getID() < lowestID || lowestID == 0)
                lowestID = artist.getID();
        }
        List<IArtist> newData = (List<IArtist>)(Object) getObjectAmount(amountToLoad, lowestID, DatabaseTable.ARTIST);
        artistList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IArtist getArtist(Integer id) throws IOException {
        if (artistList != null) {
            for (IArtist artist : artistList) {
                if (artist.getID().equals(id))
                    return artist;
            }
        }
        if (artistSearchList != null) {
            for (IArtist artist : artistSearchList) {
                if (artist.getID().equals(id))
                    return artist;
            }
        }
        return (IArtist) APIHandle.getSingle(id, DatabaseTable.ARTIST);
    }

    @Override
    public Boolean addArtist(IArtist artist) {
        if (artist.getID() <= 0 || artist == null){
            throw new IllegalArgumentException("This artist cannot be added, have to put it though createNewObject?");
        }
        return artistList.add(artist);
    }

    @Override
    public Boolean removeArtist(IArtist artist) {
        if (artist == null){
            throw new IllegalArgumentException("Cannot remove a null artist.");
        }
        return artistList.remove(artist);
    }

    @Override
    public List<IArtist> refreshArtists() throws IOException {
        artistList = (List<IArtist>)(Object) getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
        return new LinkedList<>(artistList);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        artistSearchList = (List<IArtist>) (Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.ARTIST);
        return artistSearchList;
    }

    @Override
    public Boolean setAmountToLoad(Integer amountToLoad) {
        UserWrapper.amountToLoad = amountToLoad;
        return amountToLoad == amountToLoad;
    }

    @Override
    public Object createNewObject(Object object, DatabaseTable table) throws IOException {
        return APIHandle.pushObjectToDatabase(object, table);
    }

    @Override
    public Object updateObject(Object object, DatabaseTable table) throws IOException {
        return APIHandle.updateObjectToDatabase(object, table);
    }


}
