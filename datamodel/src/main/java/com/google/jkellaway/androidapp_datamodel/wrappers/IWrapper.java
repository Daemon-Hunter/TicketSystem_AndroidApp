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
 * The interface Wrapper.
 *
 * @author 10467841
 */
public interface IWrapper {

    /**
     * Gets parent events.
     *
     * @return the parent events
     * @throws IOException the io exception
     */
    LinkedList          getParentEvents() throws IOException;

    /**
     * Load more parent events list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IParentEvent>  loadMoreParentEvents() throws IOException;

    /**
     * Gets parent event.
     *
     * @param id the id
     * @return the parent event
     * @throws IOException the io exception
     */
    IParentEvent        getParentEvent(Integer id) throws IOException;

    /**
     * Add parent event boolean.
     *
     * @param parentEvent the parent event
     * @return the boolean
     */
    Boolean             addParentEvent(IParentEvent parentEvent);

    /**
     * Remove parent event boolean.
     *
     * @param pEvent the p event
     * @return the boolean
     */
    Boolean             removeParentEvent(IParentEvent pEvent);

    /**
     * Refresh parent events list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IParentEvent>  refreshParentEvents() throws IOException;

    /**
     * Search parent events list.
     *
     * @param string the string
     * @return the list
     * @throws IOException the io exception
     */
    List<IParentEvent>  searchParentEvents(String string) throws IOException;


    /**
     * Gets venues.
     *
     * @return the venues
     * @throws IOException the io exception
     */
    List<IVenue>    getVenues() throws IOException;

    /**
     * Gets venue.
     *
     * @param id the id
     * @return the venue
     * @throws IOException the io exception
     */
    IVenue          getVenue(Integer id) throws IOException;

    /**
     * Load more venues list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IVenue>    loadMoreVenues() throws IOException;

    /**
     * Add venue boolean.
     *
     * @param venue the venue
     * @return the boolean
     */
    Boolean         addVenue(IVenue venue);

    /**
     * Remove venue boolean.
     *
     * @param venue the venue
     * @return the boolean
     */
    Boolean         removeVenue(IVenue venue);

    /**
     * Refresh venues list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IVenue>    refreshVenues() throws IOException;

    /**
     * Search venues list.
     *
     * @param string the string
     * @return the list
     * @throws IOException the io exception
     */
    List<IVenue>    searchVenues(String string) throws IOException;


    /**
     * Gets artists.
     *
     * @return the artists
     * @throws IOException the io exception
     */
    List<IArtist>   getArtists() throws IOException;

    /**
     * Load more artists list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IArtist>   loadMoreArtists() throws IOException;

    /**
     * Gets artist.
     *
     * @param id the id
     * @return the artist
     * @throws IOException the io exception
     */
    IArtist         getArtist(Integer id) throws IOException;

    /**
     * Add artist boolean.
     *
     * @param artist the artist
     * @return the boolean
     */
    Boolean         addArtist(IArtist artist);

    /**
     * Remove artist boolean.
     *
     * @param artist the artist
     * @return the boolean
     */
    Boolean         removeArtist(IArtist artist);

    /**
     * Refresh artists list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IArtist>   refreshArtists() throws IOException;

    /**
     * Search artists list.
     *
     * @param string the string
     * @return the list
     * @throws IOException the io exception
     */
    List<IArtist>   searchArtists(String string) throws IOException;

    /**
     * Sets amount to load.
     *
     * @param amountToLoad the amount to load
     * @return the amount to load
     */
    Boolean setAmountToLoad(Integer amountToLoad);

    /**
     * Create new object object.
     *
     * @param object the object
     * @param table  the table
     * @return the object
     * @throws IOException the io exception
     */
    Object createNewObject(Object object, DatabaseTable table) throws IOException;

    /**
     * Update object object.
     *
     * @param object the object
     * @param table  the table
     * @return the object
     * @throws IOException the io exception
     */
    Object updateObject(Object object, DatabaseTable table) throws IOException;

    /**
     * Make guest bookings list.
     *
     * @param guestBookings the guest bookings
     * @return the list
     * @throws IOException the io exception
     */
    List<GuestBooking> makeGuestBookings(List<GuestBooking> guestBookings) throws IOException;
}
