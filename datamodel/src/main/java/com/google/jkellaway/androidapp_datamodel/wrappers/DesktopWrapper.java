/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.CustomerBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.GuestBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.bookings.Order;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.getStats;
import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.pushObjectToDatabase;

/**
 * The DesktopWrapper class provides a facade for a Admin centered application.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class DesktopWrapper implements IDesktopWrapper {

    private static DesktopWrapper wrapper;

    private Integer amountToLoad = 15;

    private List<IParentEvent> parentEventList;
    private List<IParentEvent> parentEventSearchList;
    private List<IVenue> venueList;
    private List<IVenue> venueSearchList;
    private List<IArtist> artistList;
    private List<IArtist> artistSearchList;
    private List<ICustomer> customerList;
    private List<ICustomer> customerSearchList;
    private List<GuestBooking> guestBookingList;
    private List<GuestBooking> guestBookingSearchList;
    private List<IAdmin> adminList;
    private IAdmin currentAdmin;

    private DesktopWrapper() {
    }

    /**
     * Get instance desktop wrapper.
     *
     * @return the desktop wrapper
     */
    public static DesktopWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new DesktopWrapper();
        }
        return wrapper;
    }

    @Override
    public LinkedList getParentEvents() throws IOException {
        if (parentEventList != null) {
            return new LinkedList<>(parentEventList);
        } else {//parentEventList = APIHandle.getParentAmount(amountToLoad, parentEventList.get(parentEventList.size()).getParentEventID());
            parentEventList = new LinkedList<>((List<IParentEvent>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
            return new LinkedList<>(parentEventList);
        }
    }

    @Override
    public List<IParentEvent> loadMoreParentEvents() throws IOException {
        int lowestID = 0;
        for (IParentEvent parentEvent : parentEventList) {
            if (parentEvent.getID() < lowestID || lowestID == 0) lowestID = parentEvent.getID();
        }
        List<IParentEvent> newData = (List<IParentEvent>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.PARENT_EVENT);
        parentEventList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IParentEvent getParentEvent(Integer id) throws IOException {
        if (parentEventList != null) {
            for (IParentEvent parentEvent : parentEventList) {
                if (parentEvent.getID().equals(id)) return parentEvent;
            }
        }
        if (parentEventSearchList != null) {
            for (IParentEvent parentEvent : parentEventSearchList) {
                if (parentEvent.getID().equals(id)) return parentEvent;
            }
        }
        return (IParentEvent) APIHandle.getSingle(id, DatabaseTable.PARENT_EVENT);
    }

    @Override
    public Boolean addParentEvent(IParentEvent parentEvent) {
        if (parentEvent == null || parentEvent.getID() <= 0) {
            throw new IllegalArgumentException("This parentEvent cannot be added, have to put it though createNewObject?");
        }
        return parentEventList.add(parentEvent);
    }

    @Override
    public Boolean removeParentEvent(IParentEvent pEvent) {
        if (pEvent == null) {
            throw new IllegalArgumentException("Cannot remove null value.");
        }
        return parentEventList.remove(pEvent);
    }

    @Override
    public List<IParentEvent> refreshParentEvents() throws IOException {
        parentEventList = new LinkedList<>((List<IParentEvent>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.PARENT_EVENT));
        return parentEventList;
    }

    @Override
    public List<IParentEvent> searchParentEvents(String searchString) throws IOException {
        parentEventSearchList = (List<IParentEvent>) (Object) APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.PARENT_EVENT);
        return parentEventSearchList;
    }

    @Override
    public List<IVenue> getVenues() throws IOException {
        if (venueList != null) {
            return new LinkedList<>(venueList);
        } else {
            //venueList = APIHandle.getVenueAmount(amountToLoad, venueList.get(venueList.size()).getVenueID());
            venueList = new LinkedList<>((List<IVenue>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
            return new LinkedList<>(venueList);
        }
    }

    @Override
    public IVenue getVenue(Integer id) throws IOException {
        if (venueList != null) {
            for (IVenue venue : venueList) {
                if (venue.getID().equals(id)) return venue;
            }
        }
        if (venueSearchList != null) {
            for (IVenue venue : venueSearchList) {
                if (venue.getID().equals(id)) return venue;
            }
        }
        return (IVenue) APIHandle.getSingle(id, DatabaseTable.VENUE);
    }

    @Override
    public List<IVenue> loadMoreVenues() throws IOException {
        int lowestID = 0;
        for (IVenue venue : venueList) {
            if (venue.getID() < lowestID || lowestID == 0) lowestID = venue.getID();
        }
        List<IVenue> newData = (List<IVenue>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.VENUE);
        venueList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public Boolean addVenue(IVenue venue) {
        if (venue.getID() <= 0 || venue == null) {
            throw new IllegalArgumentException("This venue cannot be added, have to put it though createNewObject?");
        }
        return venueList.add(venue);
    }

    @Override
    public Boolean removeVenue(IVenue venue) {
        if (venue == null) {
            throw new IllegalArgumentException("Cannot remove a null venue.");
        }
        return venueList.remove(venue);
    }

    @Override
    public List<IVenue> refreshVenues() throws IOException {
        venueList = new LinkedList<>((List<IVenue>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.VENUE));
        return venueList;
    }

    @Override
    public List<IVenue> searchVenues(String searchString) throws IOException {
        venueSearchList = (List<IVenue>) (Object) APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.VENUE);
        return venueSearchList;
    }

    @Override
    public List<IArtist> getArtists() throws IOException {
        if (artistList != null) {
            return new LinkedList<>(artistList);
        } else {
            //artistList = APIHandle.getArtistAmount(amountToLoad, artistList.get(artistList.size() - 1).getArtistID());
            artistList = (List<IArtist>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
            return new LinkedList<>(artistList);
        }
    }

    @Override
    public List<IArtist> loadMoreArtists() throws IOException {
        int lowestID = 0;
        for (IArtist artist : artistList) {
            if (artist.getID() < lowestID || lowestID == 0) lowestID = artist.getID();
        }
        List<IArtist> newData = (List<IArtist>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.ARTIST);
        artistList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public IArtist getArtist(Integer id) throws IOException {
        if (artistList != null) {
            for (IArtist artist : artistList) {
                if (artist.getID().equals(id)) return artist;
            }
        }
        if (artistSearchList != null) {
            for (IArtist artist : artistSearchList) {
                if (artist.getID().equals(id)) return artist;
            }
        }
        return (IArtist) APIHandle.getSingle(id, DatabaseTable.ARTIST);
    }

    @Override
    public Boolean addArtist(IArtist artist) {
        if (artist.getID() <= 0 || artist == null) {
            throw new IllegalArgumentException("This artist cannot be added, have to put it though createNewObject?");
        }
        return artistList.add(artist);
    }

    @Override
    public Boolean removeArtist(IArtist artist) {
        if (artist == null) {
            throw new IllegalArgumentException("Cannot remove a null artist.");
        }
        return artistList.remove(artist);
    }

    @Override
    public List<IArtist> refreshArtists() throws IOException {
        artistList = (List<IArtist>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ARTIST);
        return new LinkedList<>(artistList);
    }

    @Override
    public List<IArtist> searchArtists(String searchString) throws IOException {
        artistSearchList = (List<IArtist>) (Object) APIHandle.searchObjects(searchString, amountToLoad, DatabaseTable.ARTIST);
        return artistSearchList;
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
        customerList = (List<ICustomer>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return new LinkedList<>(customerList);
    }

    @Override
    public ICustomer getCustomer(Integer index) throws IOException {
        for (ICustomer customer : customerList) {
            if (customer.getID().equals(index)) return customer;
        }
        return (ICustomer) APIHandle.getSingle(index, DatabaseTable.CUSTOMER);
    }

    @Override
    public Boolean removeCustomer(ICustomer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Cannot remove a null customer.");
        }
        return customerList.remove(customer);
    }

    @Override
    public List<ICustomer> loadMoreCustomers() throws IOException {
        int lowestID = 0;
        for (ICustomer customer : customerList) {
            if (customer.getID() < lowestID || lowestID == 0) lowestID = customer.getID();
        }
        List<ICustomer> newData = (List<ICustomer>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.CUSTOMER);
        customerList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<ICustomer> refreshCustomers() throws IOException {
        customerList = (List<ICustomer>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return new LinkedList<>(customerList);
    }

    @Override
    public List<ICustomer> searchCustomers(String search) throws IOException {
        customerSearchList = (List<ICustomer>) (Object) APIHandle.searchObjects(search, amountToLoad, DatabaseTable.CUSTOMER);
        return customerSearchList;
    }

    @Override
    public Boolean addAdmin(IAdmin admin) {
        if (admin == null || admin.getID() <= 0) {
            throw new IllegalArgumentException("This admin cannot be added, have to put it though createNewObject?");
        }
        return adminList.add(admin);
    }

    @Override
    public IAdmin getAdmin(Integer index) throws IOException {
        for (IAdmin admin : adminList) {
            if (admin.getID().equals(index)) return admin;
        }
        return (IAdmin) APIHandle.getSingle(index, DatabaseTable.ADMIN);
    }

    @Override
    public List<IAdmin> getAdmins() throws IOException {
        if (adminList != null) {
            return new LinkedList<>(adminList);
        }
        adminList = (List<IAdmin>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ADMIN);
        return new LinkedList<>(adminList);
    }

    @Override
    public Boolean removeAdmin(IAdmin admin) {
        if (admin == null) throw new IllegalArgumentException("Cannot remove a null admin.");
        return customerList.remove(admin);
    }

    @Override
    public List<IAdmin> loadMoreAdmins() throws IOException {
        int lowestID = 0;
        for (IAdmin admin : adminList) {
            if (admin.getID() < lowestID || lowestID == 0) lowestID = admin.getID();
        }
        List<IAdmin> newData = (List<IAdmin>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.ADMIN);
        adminList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<IAdmin> refreshAdmins() throws IOException {
        adminList = (List<IAdmin>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.ADMIN);
        return new LinkedList<>(adminList);
    }

    @Override
    public Boolean addGuestBooking(GuestBooking guestBooking) {
        if (guestBooking == null || guestBooking.getGuest().getID() <= 0)
            throw new IllegalArgumentException("This guest cannot be added, have to put it though createNewObject?");
        return guestBookingList.add(guestBooking);
    }

    @Override
    public List<GuestBooking> getGuestBookings() throws IOException {
        if (guestBookingList != null) {
            return new LinkedList<>(guestBookingList);
        }
        guestBookingList = (List<GuestBooking>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.GUEST_BOOKING);
        return new LinkedList<>(guestBookingList);
    }

    @Override
    public GuestBooking getGuestBooking(Integer index) throws IOException {
        for (GuestBooking guestBooking : guestBookingList) {
            if (guestBooking.getGuest().getID().equals(index)) return guestBooking;
        }
        return (GuestBooking) APIHandle.getSingle(index, DatabaseTable.GUEST_BOOKING);
    }

    @Override
    public Boolean removeGuestBooking(GuestBooking guestBooking) {
        if (guestBooking == null) {
            throw new IllegalArgumentException("Cannot remove a null guestBooking.");
        }
        return guestBookingList.remove(guestBooking);
    }

    @Override
    public List<GuestBooking> loadMoreGuestBookings() throws IOException {
        int lowestID = 0;
        for (GuestBooking guestBooking : guestBookingList) {
            if (guestBooking.getBookingID() < lowestID || lowestID == 0)
                lowestID = guestBooking.getBookingID();
        }
        List<GuestBooking> newData = (List<GuestBooking>) (Object) APIHandle.getObjectAmount(amountToLoad, lowestID, DatabaseTable.GUEST_BOOKING);
        guestBookingList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public List<GuestBooking> refreshGuestBookings() throws IOException {
        guestBookingList = (List<GuestBooking>) (Object) APIHandle.getObjectAmount(amountToLoad, 0, DatabaseTable.CUSTOMER);
        return new LinkedList<>(guestBookingList);
    }

    @Override
    public Boolean loginAdmin(String email, String password) throws IOException, IllegalArgumentException {
        currentAdmin = (IAdmin) APIHandle.isPasswordTrue(email, password, DatabaseTable.ADMIN);
        return !currentAdmin.getID().equals(0);
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

    @Override
    public List<GuestBooking> makeGuestBookings(List<GuestBooking> guestBookings) throws IOException {
        List<GuestBooking> madeBookings = new LinkedList<>();
        for (GuestBooking guestBooking : guestBookings) {
            madeBookings.add((GuestBooking) pushObjectToDatabase(guestBooking, DatabaseTable.GUEST_BOOKING));
        }
        return madeBookings;
    }

    @Override
    public IOrder makeCustomerBooking(ICustomer customer, ITicket ticket, Integer quantity) throws IOException {
        IOrder order = (IOrder) pushObjectToDatabase(new Order(customer.getID()), DatabaseTable.ORDER);
        IBooking booking;
        booking = new CustomerBooking(order, ticket, quantity);
        booking = (IBooking) APIHandle.pushObjectToDatabase(booking, DatabaseTable.BOOKING);
        order.addBooking(booking);
        return order;
    }

    @Override
    public Boolean checkAdminPassword(String email, String password) throws IOException {
        return !((IAdmin) APIHandle.isPasswordTrue(email, password, DatabaseTable.ADMIN)).getID().equals(-1);
    }

    @Override
    public List<GuestBooking> searchGuestBookings(String string) throws IOException {
        guestBookingSearchList = (List<GuestBooking>) (Object) APIHandle.searchObjects(string, amountToLoad, DatabaseTable.GUEST_BOOKING);
        return guestBookingSearchList;
    }

    @Override
    public List<IBooking> getThisMonthsSales() throws IOException {
        List<IBooking> sales = new LinkedList();
        sales.addAll((List<CustomerBooking>) (Object) getStats("thismonthscustomersales"));
        sales.addAll((List<GuestBooking>) (Object) getStats("thismonthsguestsales"));
        return sales;
    }

    @Override
    public List<IChildEvent> getSoldOutEvents() throws IOException {
        return (List<IChildEvent>) (Object) getStats("soldoutevents");
    }
}
