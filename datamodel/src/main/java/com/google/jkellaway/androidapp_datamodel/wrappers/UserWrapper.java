/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10467841
 */
public class UserWrapper implements IUserWrapper {

    private static UserWrapper wrapper;

    private static Integer amountListView = 10;
    private static Integer amountGridView = 18;

    private List<IParentEvent> parentEventList;
    private List<IVenue> venueList;
    private List<IArtist> artistList;

    private List<IParentEvent> parentEventSearchList;
    private List<IVenue> venueSearchList;
    private List<IArtist> artistSearchList;

    private IUser currentUser;

    private UserWrapper() {
    }

    private UserWrapper(IUser user) {
        this.currentUser = user;
    }

    public static UserWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new UserWrapper();
        }
        return wrapper;
    }

    @Override
    public Boolean loginUser(String email, String password) throws IOException, IllegalArgumentException {

        currentUser = APIHandle.isPasswordTrue(email, password);
        // if the passwords are incorrect then the returning Customer has an id of -1
        if(currentUser.getID().equals(-1)) { return false; }  else{ return true; }

    }

    @Override
    public IUser getUser() {
        return this.currentUser;
    }

    @Override
    public Integer registerUser(IUser customer, String password) throws IOException {
        return APIHandle.registerUser(customer, password);
    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventList != null){
            return new LinkedList<>(parentEventList);
        } else {
            parentEventList = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountGridView, 0,
                    DatabaseTable.PARENT_EVENT);
            return new LinkedList<>(parentEventList);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 99999999;
        for (IParentEvent parentEvent : parentEventList){
            if (parentEvent.getID() < lowestID)
                lowestID = parentEvent.getID();
        }
        List<IParentEvent> newData = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountGridView, lowestID, DatabaseTable.PARENT_EVENT);
        parentEventList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) {
        for (IParentEvent parentEvent : parentEventList){
            if(parentEvent.getID().equals(id))
            return parentEvent;
        }
        for (IParentEvent parentEvent : parentEventSearchList){
            if(parentEvent.getID().equals(id))
                return parentEvent;
        }
        throw new NullPointerException("No item in the list has this id :/.");
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
        parentEventList = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountGridView, 0, DatabaseTable.PARENT_EVENT);
        return new LinkedList<>(parentEventList);
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        parentEventSearchList = (List<IParentEvent>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.PARENT_EVENT);
        return parentEventSearchList;
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueList != null){
            return new LinkedList<>(venueList);
        } else {
            venueList = (List<IVenue>) (Object) APIHandle.getObjectAmount(amountListView, 0, DatabaseTable.VENUE);
            return venueList;
        }
    }

    @Override
    public IVenue getVenue(Integer id) {
        for (IVenue venue : venueList){
            if(venue.getID().equals(id))
                return venue;
        }
        for (IVenue venue : venueSearchList){
            if(venue.getID().equals(id))
                return venue;
        }
        throw new NullPointerException("No item in the list has this id :/.");
    }

    @Override
    public List<IVenue> loadMoreVenues() throws IOException {
        int lowestID = 0;
        for (IVenue venue : venueList){
            if (venue.getID() < lowestID || lowestID == 0)
                lowestID = venue.getID();
        }
        List<IVenue> newData = (List<IVenue>) (Object) APIHandle.getObjectAmount(amountListView, lowestID,
                DatabaseTable.VENUE);
        venueList.addAll(newData);
        return new LinkedList<>(newData);
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
        venueList = (List<IVenue>)(Object)APIHandle.getObjectAmount(amountGridView, 0, DatabaseTable.VENUE);
        return new LinkedList<>(venueList);
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        venueSearchList = (List<IVenue>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.VENUE);
        return venueSearchList;
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistList != null){
            return new LinkedList<>(artistList);
        } else {
            artistList = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountGridView, 0, DatabaseTable.ARTIST);
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
        List<IArtist> newData = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountGridView, lowestID, DatabaseTable.ARTIST);
        artistList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IArtist getArtist(Integer id) {
        for (IArtist artist : artistList){
            if(artist.getID().equals(id))
            return artist;
        }
        for (IArtist artist : artistSearchList){
            if(artist.getID().equals(id))
                return artist;
        }
        throw new NullPointerException("No item in the list has this id :/.");
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
        artistList = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountGridView, 0, DatabaseTable.ARTIST);
        return new LinkedList<>(artistList);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        artistSearchList = (List<IArtist>) (Object)APIHandle.searchObjects(searchString, DatabaseTable.ARTIST);
        return artistSearchList;
    }

    @Override
    public Boolean setAmountToLoad(Integer amountToLoad) {
        this.amountGridView = amountToLoad;
        return amountToLoad == amountToLoad;
    }
}
