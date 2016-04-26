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
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author 10512691
 */
public class DesktopWrapper implements IDesktopWrapper {

    private static DesktopWrapper wrapper;

    private Integer amountToLoad = 9;

    private List<IParentEvent>  parentEventArray;
    private List<IVenue>        venueArray;
    private List<IArtist>       artistArray;
    private List<IUser>         userArray;
    private List<IAdmin>        adminArray;

    private  DesktopWrapper(){}

    public static DesktopWrapper getInstance(){
        if (wrapper == null){
            wrapper = new DesktopWrapper();
        }
        return wrapper;
    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventArray != null){
            return new LinkedList(parentEventArray);
        } else {
            //parentEventArray = APIHandle.getParentAmount(amountToLoad, parentEventArray.get(parentEventArray.size()).getParentEventID());
            parentEventArray = new LinkedList((List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
            return new LinkedList(parentEventArray);
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
        return new ArrayList(newData);
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
        parentEventArray = new LinkedList<>((List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
        return parentEventArray;
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        return new LinkedList<>((List<IParentEvent>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.PARENT_EVENT));
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueArray != null){
            return new LinkedList(venueArray);
        } else {
            //venueArray = APIHandle.getVenueAmount(amountToLoad, venueArray.get(venueArray.size()).getVenueID());
            venueArray = new LinkedList<>((List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
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
        venueArray = new LinkedList<>((List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
        return venueArray;
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        return (List<IVenue>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.VENUE);
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistArray != null){
            return new LinkedList(artistArray);
        } else {
            //artistArray = APIHandle.getArtistAmount(amountToLoad, artistArray.get(artistArray.size() - 1).getArtistID());
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
        return new ArrayList(newData);
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
        return new LinkedList<>(artistArray);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        return (List<IArtist>)(Object)APIHandle.searchObjects(searchString, DatabaseTable.ARTIST);
    }

    @Override
    public Boolean addUser(IUser user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IUser> getUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IUser getUser(Integer index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean removeUser(IUser user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean addAdmin(IAdmin admin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IAdmin getAdmin(Integer index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IAdmin> getAdmins() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean removeAdmin(IAdmin admin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean setAmountToLoad(Integer amountToLoad) {
        this.amountToLoad = amountToLoad;
        return this.amountToLoad == amountToLoad;
    }

    @Override
    public IParentEvent getParentEventSearch(Integer id) {
        return null;
    }

    @Override
    public IArtist getArtistSearch(Integer id) {
        return null;
    }

    @Override
    public IVenue getVenueSearch(Integer id) {
        return null;
    }
}