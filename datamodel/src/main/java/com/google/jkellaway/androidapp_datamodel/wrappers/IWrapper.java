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
     * Gets all parent events.
     *
     * @return A list of all current parent events.
     * @throws IOException Thrown if connection to the database fails.
     */
    LinkedList getParentEvents() throws IOException;

    /**
     * Load more parent events list.
     *
     * @return A list of up to date parent events.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IParentEvent> loadMoreParentEvents() throws IOException;

    /**
     * Gets a parent event by its ID.
     *
     * @param id The ID of the parent event.
     * @return The parent event.
     * @throws IOException Thrown if connection to the database fails.
     */
    IParentEvent getParentEvent(Integer id) throws IOException;

    /**
     * Add parent event.
     *
     * @param parentEvent The parent event to be added.
     * @return Boolean to indicated outcome of outcome.
     */
    Boolean addParentEvent(IParentEvent parentEvent);

    /**
     * x
     * Remove parent event boolean.
     *
     * @param pEvent the p event
     * @return the boolean
     */
    Boolean removeParentEvent(IParentEvent pEvent);

    /**
     * Refresh parent events list.
     *
     * @return Up to date list of parent events.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IParentEvent> refreshParentEvents() throws IOException;

    /**
     * Search parent events list.
     *
     * @param string Query string.
     * @return List matching query.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IParentEvent> searchParentEvents(String string) throws IOException;


    /**
     * Gets all venues.
     *
     * @return A list of all venues.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IVenue> getVenues() throws IOException;

    /**
     * Gets all venues.
     *
     * @param id The ID of the venue to be returned.
     * @return The venue matching the ID.
     * @throws IOException Thrown if connection to the database fails.
     */
    IVenue getVenue(Integer id) throws IOException;

    /**
     * Loads more venue into the venues list.
     *
     * @return An updated list of all venues.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IVenue> loadMoreVenues() throws IOException;

    /**
     * Adds a venue to the venue list.
     *
     * @param venue The venue to be added.
     * @return Boolean indicating outcome of add operation.
     */
    Boolean addVenue(IVenue venue);

    /**
     * Remove venue from the venue list.
     *
     * @param venue The venue to be removed.
     * @return Boolean to indicate outcome of removal operation.
     */
    Boolean removeVenue(IVenue venue);

    /**
     * Refresh venues list.
     *
     * @return An up to date list of all venues.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IVenue> refreshVenues() throws IOException;

    /**
     * Search venues list of for a query.
     *
     * @param string The string query to search for.
     * @return A list of venues that match the query.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IVenue> searchVenues(String string) throws IOException;


    /**
     * Gets artists.
     *
     * @return A list of all artists.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IArtist> getArtists() throws IOException;

    /**
     * Loads more artists into the artist list.
     *
     * @return Returns an up to date list of venues.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IArtist> loadMoreArtists() throws IOException;

    /**
     * Gets artist.
     *
     * @param id The ID of the artist to be searched.
     * @return The artist matching the ID.
     * @throws IOException Thrown if connection to the database fails.
     */
    IArtist getArtist(Integer id) throws IOException;

    /**
     * Add an artist to the artist list.
     *
     * @param artist The artist to be added.
     * @return Boolean to indicate success of add operation.
     */
    Boolean addArtist(IArtist artist);

    /**
     * Remove artist from the artist list..
     *
     * @param artist The artist to remove.
     * @return Boolean to indicate removal.
     */
    Boolean removeArtist(IArtist artist);

    /**
     * Refresh artists list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IArtist> refreshArtists() throws IOException;

    /**
     * Search artists list using a query string.
     *
     * @param string The query string.
     * @return A list of artist that match the query.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IArtist> searchArtists(String string) throws IOException;

    /**
     * Sets amount to load from the database.
     *
     * @param amountToLoad the amount to load
     * @return Boolean indicating success.
     */
    Boolean setAmountToLoad(Integer amountToLoad);

    /**
     * Create new object.
     *
     * @param object The object to be created.
     * @param table  The table the object is to be inserted in.
     * @return The object
     * @throws IOException Thrown if connection to the database fails.
     */
    Object createNewObject(Object object, DatabaseTable table) throws IOException;

    /**
     * Update object object.
     *
     * @param object The object to update.
     * @param table  The table to update the object in.
     * @return The updated object.
     * @throws IOException Thrown if connection to the database fails.
     */
    Object updateObject(Object object, DatabaseTable table) throws IOException;

    /**
     * Make a list of guest bookings.
     *
     * @param guestBookings The guest bookings list.
     * @return The list of guest bookings.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<GuestBooking> makeGuestBookings(List<GuestBooking> guestBookings) throws IOException;
}
