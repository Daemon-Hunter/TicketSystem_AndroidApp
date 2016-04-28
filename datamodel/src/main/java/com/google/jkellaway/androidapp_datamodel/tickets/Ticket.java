
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.jkellaway.androidapp_datamodel.tickets;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.events.ChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IDbSubject;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.utilities.Blacklist.contains;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.descriptionValidator;

/**
 *
 * @author 10512691
 */
public class Ticket implements ITicket, IDbSubject {

    private Integer         ticketID;
    private IChildEvent     childEvent;
    private Double          price;
    private String          description;
    private Integer         amountRemaining;
    private String          type;
    private List<IObserver> observers;
    private Integer         childEventID;
    private List<IBooking>  bookings;
    private DatabaseTable   table;

    /**
     * Use this constructor when creating a new ticket object.
     * Use the constructor when creating an object from the database.
     * @param ID Unique number for the ticket given by the database.
     * @param childEventID The event the ticket is for.
     * @param price Price of the ticket.
     * @param desc Description of the ticket.
     * @param remaining Number remaining (total number of tickets at time of construction).
     * @param type The ticket type (standing / seating / weekend etc.)
     */
    public Ticket(Integer ID, Integer childEventID, Double price, String desc,
                  Integer remaining, String type) {
        ticketID = ID;
        this.childEventID = childEventID;
        this.price = price;
        description = desc;
        amountRemaining = remaining;
        this.type = type;
        this.table = DatabaseTable.TICKET;
    }

    public Ticket(ChildEvent event, Double price, String desc, Integer remaining,
                  String type) {
        if (event == null) {
            throw new NullPointerException("Cannot make a ticket for a null event");
        } else {
            this.childEvent = event;
        }

        if (0 <= price) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Cannot set a price below 0!");
        }
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public void notifyObservers() throws IOException {
        if (observers == null) {
            observers = new LinkedList();
        } else {
            for (IObserver o : observers) {
                o.update(this, table);
            }
        }
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("cannot register null observer");
        } else {
            if (!observers.contains(o)) {
                return observers.add(o);
            } else {
                return false;
            }
        }
    }


    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("cannot remove null observer");
        } else {
            if (observers.contains(o)) {
                return observers.remove(o);
            } else {
                return false;
            }
        }
    }

    @Override
    public Integer getID() {
        return ticketID;
    }

    @Override
    public Integer getChildEventID() {
        return childEventID;
    }

    @Override
    public IChildEvent getChildEvent() throws IOException {
        childEvent = (IChildEvent) APIHandle.getSingle(this.childEventID, DatabaseTable.CHILD_EVENT);
        childEventID = childEvent.getID();
        return childEvent;
    }

    @Override
    public Boolean setChildEvent(IChildEvent event) {
        if (event == null) {
            throw new NullPointerException("Cannot set event to null");
        } else {
            this.childEvent = event;
            return this.childEvent.equals(event);
        }
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Boolean setPrice(Double price) {
        if (0 <= price) {
            this.price = price;
            return this.price.equals(price);
        } else return false;
    }

    @Override
    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            throw new NullPointerException("description is null!");
        }
    }

    @Override
    public Boolean setDescription(String description) {
        if (descriptionValidator(description)) {
            this.description = description;
            return this.description.equals(description);
        } else {
            return false;
        }
    }

    @Override
    public Integer getRemaining() {
        return amountRemaining;
    }

    @Override
    public Boolean setRemaining(Integer remaining) {
        if (0 <= remaining) {
            amountRemaining = remaining;
            return amountRemaining.equals(remaining);
        } else return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Boolean setType(String type) {
        if (!contains(type)) {
            this.type = type;
            return this.type.equals(type);
        } else {
            return false;
        }
    }

    @Override
    public List<IBooking> getBookings() throws IOException {
        bookings = (List<IBooking>)(Object) APIHandle.getObjectsFromObject(this.ticketID, DatabaseTable.BOOKING, DatabaseTable.TICKET);
        return bookings;
    }

    @Override
    public Boolean addBooking(IBooking booking) {
        if (booking == null)
            throw new IllegalArgumentException("Cannot add a null booking");
        return bookings.add(booking);
    }

    @Override
    public Boolean removeBooking(IBooking booking) {
        if (booking == null)
            throw new IllegalArgumentException("Cannot remove a null booking");
        return bookings.remove(booking);
    }

}

