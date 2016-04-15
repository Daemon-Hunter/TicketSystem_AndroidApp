package com.example.aneurinc.prcs_app.bookings;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Tickets.Ticket;
import com.example.aneurinc.prcs_app.utilities.Observer.IObserver;

import java.util.Date;
import java.util.LinkedList;

import static com.example.aneurinc.prcs_app.Utility.Validator.quantityValidator;

/**
 * Created by Dominic on 14/04/2016.
 */
public abstract class Booking implements IBooking {

    protected DatabaseTable table;
    protected Integer bookingID;
    protected Ticket  ticket;
    protected Integer ticketQuantity;
    protected Date    bookingDateTime;
    protected LinkedList<IObserver> observers;

    /**
     * Use this constructor when creating a new object.
     * @param newTicket
     * @param ticketQty
     * @param dateTime
     */
    public Booking(Ticket newTicket,  Integer ticketQty, Date dateTime) {
        this.bookingID = 0;
        this.ticket = newTicket;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = (Date) dateTime.clone();
    }

    /**
     * Use this constructor when creating an object from the database.
     * @param ID
     * @param newTicket
     * @param ticketQty
     * @param dateTime
     */
    public Booking(Integer ID, Ticket newTicket, Integer ticketQty, Date dateTime) {
        this.bookingID = ID;
        this.ticket = newTicket;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = (Date) dateTime.clone();
    }

    @Override
    public Integer getBookingID() {
        if (bookingID == null) {
            throw new NullPointerException("Null booking ID");
        } else {
            return bookingID;
        }
    }

    @Override
    public Ticket getTicket() {
        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            return ticket;
        }
    }
    @Override
    public Boolean setTicket(Ticket ticket) {
        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = ticket;
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
            Boolean valid = quantityValidator(qty);
            if (valid) {
                ticketQuantity = qty;
                notifyObservers();
            }
            return valid;
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
            notifyObservers();
            return true;
        }
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public void notifyObservers() {
        for (IObserver o : observers) {
            o.update(this);
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
