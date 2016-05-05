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
import java.util.Date;

/**
 *
 * @author 10512691
 */
public class GuestBooking implements IBooking {

    protected ITicket ticket;

    protected Integer ticketID;
    protected DatabaseTable table;
    protected Integer bookingID;
    protected Integer ticketQuantity;
    protected Date    bookingDateTime;
    private IUser guest;
    
    /**
     * Use this constructor when creating object from the database.
     * ID is known.
     * @param ID
     * @param ticketQty
     * @param dateTime
     * @param guest 
     */
    public GuestBooking (Integer ID, Integer ticketID, Integer ticketQty, Date dateTime,
                         IUser guest)
    {
        this.bookingID = ID;
        this.ticketID = ticketID;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = (Date) dateTime.clone();
        if (guest != null) {
            this.guest = guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
        table = DatabaseTable.GUEST_BOOKING;
    }
    
    /**
     * Use this constructor when creating a new GuestBooking.
     * ID is unknown.
     * @param ticket
     * @param ticketQty
     * @param dateTime 
     * @param guest 
     */
    public GuestBooking (ITicket ticket, Integer ticketQty, Date dateTime,
            IUser guest) 
    {
        // Set ID as 0. Database will create one using sequence.
        this.bookingID = 0;
        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = ticket;

            if (!Validator.quantityValidator(ticketQty)) {
                throw new IllegalArgumentException("Invalid ticket quantity");
            } else {
                this.ticketQuantity = ticketQty;

                // Store a copy of the time, as the variable could be externally changed
                // after construction -> externally mutable object
                this.bookingDateTime = (Date) dateTime.clone();
            }
        }
        if (guest != null) {
            this.guest = guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
        table = DatabaseTable.GUEST_BOOKING;
    } 

    public IUser getGuest() {
        if (guest != null) {
            return guest;
        } else {
            throw new NullPointerException("Cannot create a booking for a null guest.");
        }
    }

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
    public Boolean setQuantity(Integer qty) {
        if (qty == null) {
            throw new NullPointerException("Null quantity");
        } else {
            if (Validator.quantityValidator(qty)) {
                ticketQuantity = qty;
                return true;
            }
            return false;
        }
    }

    @Override
    public Date getBookingTime() {
        if (bookingDateTime == null) {
            throw new NullPointerException("Null booking date / time");
        } else {
            return (Date) bookingDateTime.clone();
        }
    }
    @Override
    public Boolean setBookingTime(Date time) {
        if (time == null) {
            throw new NullPointerException("Null date / time");
        } else {
            // Store a copy of the time, as the variable could be externally changed
            // after construction -> externally mutable object
            bookingDateTime = (Date) time.clone();
            return true;
        }
    }

    public DatabaseTable getTable() {
        return table;
    }
}
