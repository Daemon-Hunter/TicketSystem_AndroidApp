/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.Guest;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The class Guest booking is used to represent the GuestBooking entity in the database.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class GuestBooking implements IBooking {

    /**
     * The Ticket for the booking
     */
    protected ITicket ticket;

    /**
     * The Ticket id.
     */
    protected Integer ticketID;
    /**
     * The Table.
     */
    protected DatabaseTable table;
    /**
     * The Booking id.
     */
    protected Integer bookingID;
    /**
     * The Ticket quantity.
     */
    protected Integer ticketQuantity;
    /**
     * The dateTime of when the booking is made.
     */
    protected String bookingDateTime;
    private IUser guest;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    /**
     * Use this constructor when creating object from the database.
     * ID is known.
     *
     * @param ID        the primary ID of th ebooking
     * @param ticketID  the ticket id
     * @param ticketQty the amount of tickets for the booking
     * @param dateTime  the time at which the booking was made
     * @param guest     the guest object that is making the booking
     */
    public GuestBooking(Integer ID, Integer ticketID, Integer ticketQty, String dateTime, IUser guest) {
        this.bookingID = ID;
        this.ticketID = ticketID;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = dateTime;
        if (guest != null) {
            this.guest = guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
        table = DatabaseTable.GUEST_BOOKING;
    }

    /**
     * Use this constructor when creating a new GuestBooking.
     * ID guest booking is not yet set in the database.
     *
     * @param ticket    the ticket
     * @param ticketQty the amount of tickets purchased
     * @param dateTime  the date time
     * @param guest     the guest
     */
    public GuestBooking(ITicket ticket, Integer ticketQty, Date dateTime, IUser guest) {
        // Set ID as 0. Database will create one using sequence.
        this.bookingID = 0;
        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = ticket;
            this.ticketID = ticket.getID();

            Validator.quantityValidator(ticketQty);
            this.ticketQuantity = ticketQty;

            // Store a copy of the time, as the variable could be externally changed
            // after construction -> externally mutable object
            this.bookingDateTime = formatter.format(dateTime);

        }
        if (guest != null) {
            this.guest = guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
        table = DatabaseTable.GUEST_BOOKING;
    }

    /**
     * Gets the guest object that the booking is made for
     *
     * @return IUSER the guest object
     * @throws NullPointerException if there is no guest for the booking
     */
    public IUser getGuest() {
        if (guest != null) {
            return guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
    }

    /**
     * Sets guest.
     *
     * @param guest the guest that you wish to set the booking to
     * @return Boolean whether or not the guest was successfully added
     */
    public Boolean setGuest(Guest guest) {
        if (guest == null) {
            throw new NullPointerException("Cannot set user to null");
        } else {
            this.guest = guest;
            return true;
        }
    }

    /**
     * @return the unique ID of the booking.
     */
    @Override
    public Integer getBookingID() {
        if (bookingID == null) {
            throw new NullPointerException("Null booking ID");
        } else {
            return bookingID;
        }
    }

    @Override
    public Integer getTicketID() {
        return ticketID;
    }

    @Override
    public ITicket getTicket() throws IOException {
        if (ticket == null) {
            ticket = (ITicket) APIHandle.getSingle(this.ticketID, DatabaseTable.TICKET);
            ticketID = ticket.getID();
        }
        return ticket;
    }

    @Override
    public Boolean setTicket(ITicket ticket) {
        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = ticket;
            this.ticketID = ticket.getID();
            return true;
        }
    }

    @Override
    public Integer getQuantity() {
        if (ticketQuantity == null) {
            throw new NullPointerException("Null quantity");
        } else {
            return ticketQuantity;
        }
    }

    @Override
    public Boolean setQuantity(Integer qty) throws IllegalArgumentException {
        if (qty == null) throw new IllegalArgumentException("Enter a quantity");
        Validator.quantityValidator(qty);
        ticketQuantity = qty;
        return this.ticketQuantity.equals(qty);
    }


    @Override
    public Date getBookingTime() {
        if (bookingDateTime == null) {
            throw new NullPointerException("Null booking date / time");
        } else {
            try {
                return formatter.parse(bookingDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Boolean setBookingTime(Date time) throws IllegalArgumentException {
        if (time == null) {
            throw new IllegalArgumentException("Enter a date and time.");
        } else {
            // Store a copy of the time, as the variable could be externally changed
            // after construction -> externally mutable object
            this.bookingDateTime = formatter.format(time);
            return true;
        }
    }

    /**
     * Gets the table that the object is represented by in the database
     *
     * @return the given table for the object
     */
    public DatabaseTable getTable() {
        return table;
    }
}
