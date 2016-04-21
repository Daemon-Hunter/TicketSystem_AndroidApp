/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.Artist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

/**
 *
 * @author 10512691
 */
public class UserWrapper implements IUserWrapper {

    private static UserWrapper wrapper;

    private Integer amountToLoad = 12;

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
    public List<IParentEvent> getParentEvents() {
        if (parentEventArray != null){
            return new ArrayList(parentEventArray);
        } else {
            //parentEventArray = APIHandle.getParentAmount(amountToLoad, parentEventArray.get(parentEventArray.size()).getParentEventID());
            parentEventArray = APIHandle.getParentAmount(amountToLoad, 0);
            return new ArrayList(parentEventArray);
        }
    }

    @Override
    public Boolean removeParentEvent(IParentEvent pEvent) {
        if (pEvent == null){
            throw new IllegalArgumentException("Cannot remove null value.");
        }
        return parentEventArray.remove(pEvent);
    }

    @Override
    public List<IVenue> getVenues() {
        if (venueArray != null){
            return new ArrayList(venueArray);
        } else {
            //venueArray = APIHandle.getVenueAmount(amountToLoad, venueArray.get(venueArray.size()).getVenueID());
            venueArray = APIHandle.getVenueAmount(amountToLoad, 0);
            return new ArrayList(venueArray);
        }
    }

    @Override
    public Boolean removeVenue(IVenue venue) {
        if(venue == null){
            throw new IllegalArgumentException("Cannot remove a null venue.");
        }
        return venueArray.remove(venue);
    }

    @Override
    public List<IArtist> getArtists() {
        if (artistArray != null){
            return new LinkedList(artistArray);
        } else {
            //artistArray = APIHandle.getArtistAmount(amountToLoad, artistArray.get(artistArray.size() - 1).getArtistID());
            artistArray = APIHandle.getArtistAmount(amountToLoad, 0);
            return new LinkedList<>(artistArray);
        }
    }

    @Override
    public Boolean removeArtist(IArtist artist) {
        if (artist == null){
            throw new IllegalArgumentException("Cannot remove a null artist.");
        }
        return artistArray.remove(artist);
    }
}
