/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10512691
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
    public Boolean setUser(IUser user) {
        if (user == null){
            throw new IllegalArgumentException("Cannot set user to null.");
        }
        this.currentUser = user;
        return (this.currentUser == user);
    }

    @Override
    public IUser getUser() {
        return this.currentUser;
    }

    @Override
    public List<IParentEvent> getParentEvents() throws IOException {
        if (parentEventArray != null){
            return new ArrayList(parentEventArray);
        } else {
            //parentEventArray = APIHandle.getParentAmount(amountToLoad, parentEventArray.get(parentEventArray.size()).getParentEventID());
            parentEventArray = new ArrayList<>(APIHandle.getParentAmount(amountToLoad, 0));
            return new ArrayList(parentEventArray);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 99999999;
        for (IParentEvent parentEvent : parentEventArray){
            if (parentEvent.getParentEventID() < lowestID)
                lowestID = parentEvent.getParentEventID();
        }
        List<IParentEvent> newData = APIHandle.getParentAmount(amountToLoad, lowestID);
        parentEventArray.addAll(newData);
        return new ArrayList(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) {
        for (IParentEvent parentEvent : parentEventArray){
            if(parentEvent.getParentEventID().equals(id));
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
        parentEventArray = new ArrayList<>(APIHandle.getParentAmount(amountToLoad, 0));
        return new ArrayList(parentEventArray);
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueArray != null){
            return new ArrayList(venueArray);
        } else {
            //venueArray = APIHandle.getVenueAmount(amountToLoad, venueArray.get(venueArray.size()).getVenueID());
            venueArray = new ArrayList<>(APIHandle.getVenueAmount(amountToLoad, 0));
            return new ArrayList(venueArray);
        }
    }

    @Override
    public IVenue getVenue(Integer id) {
        for (IVenue venue : venueArray){
            if(venue.getVenueID().equals(id));
                return venue;
        }
        throw new NullPointerException("No item in the list has this id :/.");
    }

    @Override
    public List<IVenue> loadMoreVenues() throws IOException {
        int lowestID = 0;
        for (IVenue venue : venueArray){
            if (venue.getVenueID() < lowestID || lowestID == 0)
                lowestID = venue.getVenueID();
        }
        List<IVenue> newData = APIHandle.getVenueAmount(amountToLoad, lowestID);
        venueArray.addAll(newData);
        return new ArrayList(newData);
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
        venueArray = new ArrayList<>(APIHandle.getVenueAmount(amountToLoad, 0));
        return new ArrayList(venueArray);
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistArray != null){
            return new LinkedList(artistArray);
        } else {
            //artistArray = APIHandle.getArtistAmount(amountToLoad, artistArray.get(artistArray.size() - 1).getArtistID());
            artistArray = APIHandle.getArtistAmount(amountToLoad, 0);
            return new ArrayList<>(artistArray);
        }
    }

    @Override
    public List<IArtist> loadMoreArtists() throws IOException {
        int lowestID = 0;
        for (IArtist artist : artistArray){
            if (artist.getArtistID() < lowestID || lowestID == 0)
                lowestID = artist.getArtistID();
        }
        List<IArtist> newData = APIHandle.getArtistAmount(amountToLoad, lowestID);
        artistArray.addAll(newData);
        return new ArrayList(newData);
    }

    @Override
    public IArtist getArtist(Integer id) {
        for (IArtist artist : artistArray){
            if(artist.getArtistID().equals(id));
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
        artistArray = APIHandle.getArtistAmount(amountToLoad, 0);
        return new ArrayList<>(artistArray);
    }
}
