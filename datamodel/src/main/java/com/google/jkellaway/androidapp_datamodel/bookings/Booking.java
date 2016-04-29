/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author 10512691
 */
public abstract class Booking implements IBooking {

    protected ITicket ticket;
    protected IOrder  order;

    protected Integer ticketID;
    protected DatabaseTable table;
    protected Integer bookingID;
    protected Integer ticketQuantity;
    protected Date    bookingDateTime;
    protected LinkedList<IObserver> observers;
    
    /**
     * Use this constructor when creating a new object.
     * @param newTicket Ticket which the booking is tied to
     * @param ticketQty Quantity of tickets
     * @param dateTime The date / time of the booking
     */
    public Booking(ITicket newTicket,  Integer ticketQty, Date dateTime) {
        // Set ID as 0. Database will create one using sequence.
        this.bookingID = 0;
        if (newTicket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = newTicket;

            if (!Validator.quantityValidator(ticketQty)) {
                throw new IllegalArgumentException("Invalid ticket quantity");
            } else {
                this.ticketQuantity = ticketQty;

                // Store a copy of the time, as the variable could be externally changed
                // after construction -> externally mutable object
                this.bookingDateTime = (Date) dateTime.clone();
            }
        }
    }
    
    /**
     * Use this constructor when creating an object from the database.
     * @param ID is known.
     * @param ticketQty is valid.
     * @param dateTime date / time the booking was made.
     */
    public Booking(Integer ID, Integer ticketID,  Integer ticketQty, Date dateTime) {
        this.bookingID = ID;
        this.ticketID = ticketID;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = (Date) dateTime.clone();
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
    
    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public void notifyObservers() throws IOException {
        for (IObserver o : observers) {
                o.update(this, table);
            }
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (observers == null) {
            observers = new LinkedList<>();
            observers.add(o);
            return observers.contains(o);
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already exists in list");
        } else {
            observers.add(o);
            return observers.contains(o);
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer doesn't exist in list");
        } else {
            observers.remove(o);
            return !observers.contains(o);
        }
    }
}
