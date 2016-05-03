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
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.people.IGuest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author 10512691
 */
public class DesktopWrapper implements IDesktopWrapper {

    private static DesktopWrapper wrapper;

    private Integer amountToLoad = 9;

    private List<IParentEvent>  parentEventList;
    private List<IVenue>        venueList;
    private List<IArtist>       artistList;
    private List<ICustomer>     customerList;
    private List<IGuest>        guestList;
    private List<IAdmin>        adminList;
    private IAdmin              currentAdmin;

    private  DesktopWrapper(){}

    public static DesktopWrapper getInstance(){
        if (wrapper == null){
            wrapper = new DesktopWrapper();
        }
        return wrapper;
    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventList != null){
            return new LinkedList<>(parentEventList);
        } else {//parentEventList = APIHandle.getParentAmount(amountToLoad, parentEventList.get(parentEventList.size()).getParentEventID());
            parentEventList = new LinkedList<>((List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
            return new LinkedList<>(parentEventList);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 0;
        for (IParentEvent parentEvent : parentEventList){
            if (parentEvent.getID() < lowestID)
                lowestID = parentEvent.getID();
        }
        List<IParentEvent> newData = (List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.PARENT_EVENT);
        parentEventList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) throws IOException {
        for (IParentEvent parentEvent : parentEventList){
            if(parentEvent.getID().equals(id))
                return parentEvent;
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
        parentEventList = new LinkedList<>((List<IParentEvent>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
        return parentEventList;
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        return new LinkedList<>((List<IParentEvent>)(Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.PARENT_EVENT));
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueList != null){
            return new LinkedList<>(venueList);
        } else {
            //venueList = APIHandle.getVenueAmount(amountToLoad, venueList.get(venueList.size()).getVenueID());
            venueList = new LinkedList<>((List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
            return venueList;
        }
    }

    @Override
    public IVenue getVenue(Integer id) throws IOException {
        for (IVenue venue : venueList){
            if(venue.getID().equals(id))
                return venue;
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
        List<IVenue> newData = (List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.VENUE);
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
        venueList = new LinkedList<>((List<IVenue>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
        return venueList;
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        return (List<IVenue>)(Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.VENUE);
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistList != null){
            return new LinkedList<>(artistList);
        } else {
            //artistList = APIHandle.getArtistAmount(amountToLoad, artistList.get(artistList.size() - 1).getArtistID());
            artistList = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
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
        List<IArtist> newData = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.ARTIST);
        artistList.addAll(newData);
        return new LinkedList<IArtist>(newData);
    }

    @Override
    public IArtist getArtist(Integer id) throws IOException {
        for (IArtist artist : artistList){
            if(artist.getID().equals(id))
                return artist;
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
        artistList = (List<IArtist>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
        return new LinkedList<>(artistList);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        return (List<IArtist>)(Object)APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.ARTIST);
    }

    @Override
    public Boolean addCustomer(ICustomer customer) {
        if (customer == null || customer.getID() <= 0)
            throw new IllegalArgumentException("This customer cannot be added, have to put it though createNewObject?");
        return customerList.add(customer);
    }

    @Override
    public List<ICustomer> getCustomers() throws IOException {
        if (customerList != null) {
            return new LinkedList<>(customerList);
        }
        customerList = (List<ICustomer>) (Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return customerList;
    }

    @Override
    public ICustomer getCustomer(Integer index) throws IOException {
        for (ICustomer customer : customerList){
            if(customer.getID().equals(index))
                return customer;
        }
        return (ICustomer) APIHandle.getSingle(index, DatabaseTable.CUSTOMER);
    }

    @Override
    public Boolean removeCustomer(ICustomer customer) {
        if (customer == null){
            throw new IllegalArgumentException("Cannot remove a null customer.");
        }
        return customerList.remove(customer);
    }

    @Override
    public List<ICustomer> loadMoreCustomers() throws IOException {
        int lowestID = 0;
        for (ICustomer customer : customerList){
            if (customer.getID() < lowestID || lowestID == 0)
                lowestID = customer.getID();
        }
        List<ICustomer> newData = (List<ICustomer>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.CUSTOMER);
        customerList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<ICustomer> refreshCustomers() throws IOException {
        customerList = (List<ICustomer>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return new LinkedList<>(customerList);
    }

    @Override
    public Boolean addAdmin(IAdmin admin) {
        if (admin == null || admin.getID() <= 0){
            throw new IllegalArgumentException("This admin cannot be added, have to put it though createNewObject?");
        }
        return adminList.add(admin);
    }

    @Override
    public IAdmin getAdmin(Integer index) throws IOException {
        for (IAdmin admin : adminList){
            if(admin.getID().equals(index))
                return admin;
        }
        return (IAdmin) APIHandle.getSingle(index, DatabaseTable.ADMIN);
    }

    @Override
    public List<IAdmin> getAdmins() throws IOException {
        if (adminList != null) {
            return new LinkedList<>(adminList);
        }
        adminList = (List<IAdmin>) (Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ADMIN);
        return adminList;
    }

    @Override
    public Boolean removeAdmin(IAdmin admin) {
        if (admin == null) throw new IllegalArgumentException("Cannot remove a null admin.");
        return customerList.remove(admin);
    }

    @Override
    public List<IAdmin> loadMoreAdmins() throws IOException {
        int lowestID = 0;
        for (IAdmin admin : adminList){
            if (admin.getID() < lowestID || lowestID == 0)
                lowestID = admin.getID();
        }
        List<IAdmin> newData = (List<IAdmin>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.ADMIN);
        adminList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<IAdmin> refreshAdmins() throws IOException {
        adminList = (List<IAdmin>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ADMIN);
        return new LinkedList<>(adminList);
    }

    @Override
    public Boolean addGuest(IGuest guest) {
        if (guest == null || guest.getID() <= 0)
            throw new IllegalArgumentException("This guest cannot be added, have to put it though createNewObject?");
        return guestList.add(guest);
    }

    @Override
    public List<IGuest> getGuests() throws IOException {
        if (guestList != null) {
            return new LinkedList<>(guestList);
        }
        guestList = (List<IGuest>) (Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.GUEST_BOOKING);
        return guestList;
    }

    @Override
    public IGuest getGuest(Integer index) throws IOException {
        for (IGuest guest : guestList){
            if(guest.getID().equals(index))
                return guest;
        }
        return (IGuest) APIHandle.getSingle(index, DatabaseTable.GUEST_BOOKING);
    }

    @Override
    public Boolean removeGuest(IGuest guest) {
        if (guest == null){
            throw new IllegalArgumentException("Cannot remove a null customer.");
        }
        return guestList.remove(guest);
    }

    @Override
    public List<IGuest> loadMoreGuests() throws IOException {
        int lowestID = 0;
        for (IGuest guest : guestList){
            if (guest.getID() < lowestID || lowestID == 0)
                lowestID = guest.getID();
        }
        List<IGuest> newData = (List<IGuest>)(Object)APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.GUEST_BOOKING);
        guestList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<IGuest> refreshGuests() throws IOException {
        guestList = (List<IGuest>)(Object)APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return new LinkedList<>(guestList);
    }

    @Override
    public Boolean loginAdmin(String email, String password) throws IOException {
        currentAdmin = (IAdmin) APIHandle.isPasswordTrue(email, password, DatabaseTable.ADMIN);
        return !currentAdmin.getID().equals(-1);
    }

    @Override
    public IAdmin getCurrentAdmin() {
        return currentAdmin;
    }

    @Override
    public Boolean setAmountToLoad(Integer amountToLoad) {
        this.amountToLoad = amountToLoad;
        return true;
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