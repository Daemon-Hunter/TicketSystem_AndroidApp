/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;

import java.io.IOException;
import java.util.List;

/**
 * The interface Ticket.
 *
 * @author 10512691
 */
public interface ITicket {

    /**
     * Gets id.
     *
     * @return the id
     */
    Integer getID();

    /**
     * Gets child event id.
     *
     * @return the child event id
     */
    Integer getChildEventID();

    /**
     * Gets child event.
     *
     * @return the child event
     * @throws IOException the io exception
     */
    IChildEvent getChildEvent() throws IOException;

    /**
     * Sets child event.
     *
     * @param event the event
     * @return the child event
     */
    Boolean setChildEvent(IChildEvent event);

    /**
     * Gets price.
     *
     * @return the price
     */
    Double getPrice();

    /**
     * Sets price.
     *
     * @param price the price
     * @return the price
     */
    Boolean setPrice(Double price);

    /**
     * Gets description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Sets description.
     *
     * @param description the description
     * @return the description
     */
    Boolean setDescription(String description);

    /**
     * Gets the amount of tickets of that ticket type remaining.
     * Child event can have multiple ticket types, so remaining
     * is stored in that ticket type.
     *
     * @return remaining
     */
    Integer getRemaining();

    /**
     * Sets remaining.
     *
     * @param remaining the remaining
     * @return the remaining
     */
    Boolean setRemaining(Integer remaining);

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Sets type.
     *
     * @param type the type
     * @return the type
     */
    Boolean setType(String type);

    /**
     * Gets bookings.
     *
     * @return the bookings
     * @throws IOException the io exception
     */
    List<IBooking> getBookings() throws IOException;

    /**
     * Add booking boolean.
     *
     * @param booking the booking
     * @return the boolean
     */
    Boolean addBooking(IBooking booking);

    /**
     * Remove booking boolean.
     *
     * @param booking the booking
     * @return the boolean
     */
    Boolean removeBooking(IBooking booking);
}
