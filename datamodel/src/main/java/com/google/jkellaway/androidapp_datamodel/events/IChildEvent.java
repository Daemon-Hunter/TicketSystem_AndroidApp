/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicketFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * The interface Child event is implemented by any class wishing to represent an child event type.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IChildEvent extends ISocial {

    /**
     * Gets id.
     *
     * @return the id
     */
    Integer getID();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets start date time.
     *
     * @return the start date time
     */
    Date getStartDateTime();

    /**
     * Gets end date time.
     *
     * @return the end date time
     */
    Date getEndDateTime();

    /**
     * Gets cancelled.
     *
     * @return the cancelled
     */
    Boolean getCancelled();

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setName(String name) throws IllegalArgumentException;

    /**
     * Sets description.
     *
     * @param description the description
     * @return the description
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setDescription(String description) throws IllegalArgumentException;

    /**
     * Sets start date time.
     *
     * @param startDateTime the start date time
     * @return the start date time
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setStartDateTime(Date startDateTime) throws IllegalArgumentException;

    /**
     * Sets end date time.
     *
     * @param endDateTime the end date time
     * @return the end date time
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setEndDateTime(Date endDateTime) throws IllegalArgumentException;

    /**
     * Sets cancelled.
     *
     * @param cancelled the cancelled
     * @return the cancelled
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setCancelled(Boolean cancelled) throws IllegalArgumentException;

    /**
     * Gets venue id.
     *
     * @return the venue id
     */
    Integer getVenueID();

    /**
     * Sets venue.
     *
     * @param venue the venue
     * @return the venue
     */
    Boolean setVenue(IVenue venue);

    /**
     * Gets venue.
     *
     * @return the venue
     */
    IVenue getVenue();

    /**
     * Gets artist list.
     *
     * @return the artist list
     * @throws IOException the io exception
     */
    List<IArtist> getArtistList() throws IOException;

    /**
     * Gets parent event id.
     *
     * @return the parent event id
     */
    Integer getParentEventID();

    /**
     * Gets parent event.
     *
     * @return the parent event
     * @throws IOException the io exception
     */
    IParentEvent getParentEvent() throws IOException;

    /**
     * Sets parent event.
     *
     * @param event the event
     * @return the parent event
     */
    Boolean setParentEvent(IParentEvent event);

    /**
     * Gets ticket factory.
     *
     * @return the ticket factory
     */
    ITicketFactory getTicketFactory();

    /**
     * Gets ticket.
     *
     * @param id the id
     * @return the ticket
     * @throws IOException the io exception
     */
    ITicket getTicket(Integer id) throws IOException;

    /**
     * Gets tickets.
     *
     * @return the tickets
     * @throws IOException the io exception
     */
    List<ITicket> getTickets() throws IOException;

    /**
     * Add ticket boolean.
     *
     * @param ticket the ticket
     * @return the boolean
     */
    Boolean addTicket(ITicket ticket);

    /**
     * Remove ticket boolean.
     *
     * @param ticket the ticket
     * @return the boolean
     */
    Boolean removeTicket(ITicket ticket);


    /**
     * Sets venue id.
     *
     * @param venue the venue
     */
    void setVenueID(Integer venue);

    /**
     * Sets social media.
     *
     * @param socialMedia the social media
     */
    void setSocialMedia(SocialMedia socialMedia);

    /**
     * New contract boolean.
     *
     * @param artist the artist
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean newContract(IArtist artist) throws IOException;
}
