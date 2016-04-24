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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10467841
 */
public class UserWrapper implements IUserWrapper {

    private static UserWrapper wrapper;

    private Integer amountToLoad = 9;

    private List<IParentEvent>  parentEventArray;
    private List<IVenue>        venueArray;
    private List<IArtist>       artistArray;
    private IUser               currentUser;

    
    
    private UserWrapper(){}

    private UserWrapper(IUser user){
        this.currentUser = user;
    }

    public static UserWrapper getInstance(){
        if (wrapper == null){
            wrapper = new UserWrapper();
        }
        return wrapper;
    }

    @Override
    public IUser loginUser(String email, String password) throws IOException, IllegalArgumentException {
        currentUser = APIHandle.isPasswordTrue(email, password);
        return currentUser;
    }

    @Override
    public IUser getUser() {
        return this.currentUser;
    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventArray != null){
            return new LinkedList<>(parentEventArray);
        } else {
            parentEventArray = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT);
            return new LinkedList<>(parentEventArray);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 99999999;
        for (IParentEvent parentEvent : parentEventArray){
            if (parentEvent.getID() < lowestID)
                lowestID = parentEvent.getID();
        }
        List<IParentEvent> newData = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.PARENT_EVENT);
        parentEventArray.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) {
        for (IParentEvent parentEvent : parentEventArray){
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
        return parentEventArray.remove(pEvent);
    }

    @Override
    public List<IParentEvent> refreshParentEvents() throws IOException {
        parentEventArray = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT);
        return new LinkedList<>(parentEventArray);
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        return (List<IParentEvent>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.PARENT_EVENT);
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueArray != null){
            return new ArrayList<IVenue>(venueArray);
        } else {
            venueArray = (List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE);
            return venueArray;
        }
    }

    @Override
    public IVenue getVenue(Integer id) {
        for (IVenue venue : venueArray){
            if(venue.getID().equals(id))
                return venue;
        }
        throw new NullPointerException("No item in the list has this id :/.");
    }

    @Override
    public List<IVenue> loadMoreVenues() throws IOException {
        int lowestID = 0;
        for (IVenue venue : venueArray){
            if (venue.getID() < lowestID || lowestID == 0)
                lowestID = venue.getID();
        }
        List<IVenue> newData = (List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.VENUE);
        venueArray.addAll(newData);
        return new ArrayList<IVenue>(newData);
    }

    @Override
    public Boolean removeVenue(IVenue venue) {
        if(venue == null){
            throw new IllegalArgumentException("Cannot remove a null venue.");
        }
        return venueArray.remove(venue);
    }

    @Override
    public List<IVenue> refreshVenues() throws IOException {
        venueArray = (List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE);
        return new LinkedList<>(venueArray);
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        return (List<IVenue>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.VENUE);
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistArray != null){
            return new LinkedList<IArtist>(artistArray);
        } else {
            artistArray = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
            return new ArrayList<>(artistArray);
        }
    }

    @Override
    public List<IArtist> loadMoreArtists() throws IOException {
        int lowestID = 0;
        for (IArtist artist : artistArray){
            if (artist.getID() < lowestID || lowestID == 0)
                lowestID = artist.getID();
        }
        List<IArtist> newData = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.ARTIST);
        artistArray.addAll(newData);
        return new ArrayList<IArtist>(newData);
    }

    @Override
    public IArtist getArtist(Integer id) {
        for (IArtist artist : artistArray){
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
        return artistArray.remove(artist);
    }

    @Override
    public List<IArtist> refreshArtists() throws IOException {
        artistArray = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
        return new ArrayList<>(artistArray);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        return (List<IArtist>) (Object)APIHandle.searchObjects(searchString, DatabaseTable.ARTIST);
    }

    @Override
    public Boolean setAmountToLoad(Integer amountToLoad) {
        this.amountToLoad = amountToLoad;
        return amountToLoad == amountToLoad;
    }
}
