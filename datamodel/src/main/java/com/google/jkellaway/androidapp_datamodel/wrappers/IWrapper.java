/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.GuestBooking;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IWrapper {

    LinkedList          getParentEvents() throws IOException;
    List<IParentEvent>  loadMoreParentEvents() throws IOException;
    IParentEvent        getParentEvent(Integer id) throws IOException;
    Boolean             addParentEvent(IParentEvent parentEvent);
    Boolean             removeParentEvent(IParentEvent pEvent);
    List<IParentEvent>  refreshParentEvents() throws IOException;
    List<IParentEvent>  searchParentEvents(String string) throws IOException;


    List<IVenue>    getVenues() throws IOException;
    IVenue          getVenue(Integer id) throws IOException;
    List<IVenue>    loadMoreVenues() throws IOException;
    Boolean         addVenue(IVenue venue);
    Boolean         removeVenue(IVenue venue);
    List<IVenue>    refreshVenues() throws IOException;
    List<IVenue>    searchVenues(String string) throws IOException;


    List<IArtist>   getArtists() throws IOException;
    List<IArtist>   loadMoreArtists() throws IOException;
    IArtist         getArtist(Integer id) throws IOException;
    Boolean         addArtist(IArtist artist);
    Boolean         removeArtist(IArtist artist);
    List<IArtist>   refreshArtists() throws IOException;
    List<IArtist>   searchArtists(String string) throws IOException;

    Boolean setAmountToLoad(Integer amountToLoad);

    Object createNewObject(Object object, DatabaseTable table) throws IOException;
    Object updateObject(Object object, DatabaseTable table) throws IOException;

    List<GuestBooking> makeGuestBookings(List<GuestBooking> guestBookings) throws IOException;
}
