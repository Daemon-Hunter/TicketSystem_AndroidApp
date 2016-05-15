/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.Date;

/**
 * This interface should be implemented bu all classes which are a type of booking.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IBooking {
    /**
     * Gets booking id.
     *
     * @return an Integer which represents the primary id of a booking.
     */
    Integer getBookingID();

    /**
     * Gets ticket id.
     *
     * @return an Integer which represents the primary key of a ticket.
     */
    Integer getTicketID();

    /**
     * Gets ticket.
     *
     * @return an (@code ITicket) object.
     * @throws IOException if the method needs to request data from an external source.
     */
    ITicket getTicket() throws IOException;

    /**
     * This method sets the ticket referenced by the booking.
     *
     * @param ticket reference to the ticket to be set in the booking.
     * @return a Boolean value representing if the method was successful.
     */
    Boolean setTicket(ITicket ticket);

    /**
     * Gets quantity.
     *
     * @return an Integer which represents the quantity of tickets booked.
     */
    Integer getQuantity();

    /**
     * This method sets the quantity in the implementing method.
     *
     * @param qty the Integer to be set as the quantity.
     * @return a Boolean value representing if the method was successful.
     * @throws IllegalArgumentException if the input violates any constraints.
     */
    Boolean setQuantity(Integer qty) throws IllegalArgumentException;

    /**
     * Gets booking time.
     *
     * @return a Date type containing the set dateTime of the booking.
     */
    Date getBookingTime();

    /**
     * This method sets the DateTime of the booking.
     *
     * @param time the date/time which will be set in the booking.
     * @return a Boolean value representing if the method was successful.
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setBookingTime(Date time) throws IllegalArgumentException;
}
