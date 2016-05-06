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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author 10512691
 */
public class CustomerBooking implements IBooking {

    private ITicket ticket;

    private Integer ticketID;
    private DatabaseTable table;
    private Integer bookingID;
    private Integer ticketQuantity;
    private Date    bookingDateTime;
    private IOrder order;
    private Integer orderID;
    
    /**
     * Use this constructor when creating a booking object from the database.
     * @param ID
     * @param ticketQty
     * @param dateTime
     */
    public CustomerBooking (Integer ID, Integer ticketID, Integer orderID, Integer ticketQty, Date dateTime) {
        this.bookingID = ID;
        this.ticketID = ticketID;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = (Date) dateTime.clone();
        this.table = DatabaseTable.BOOKING;
        if (Validator.idValidator(orderID)) {
            this.orderID = orderID;
        } else {
            throw new IllegalArgumentException("Invalid order ID");
        }
    }
    
    /**
     * Use this constructor when creating a new customer booking object.
     * @param ticket
     * @param ticketQty
     */
    public CustomerBooking (IOrder order, ITicket ticket, Integer ticketQty) {
        // Set ID as 0. Database will create one using sequence.
        this.bookingID = 0;

        this.order = order;
        this.orderID = order.getOrderID();
        this.bookingDateTime = Calendar.getInstance().getTime();

        if (ticket == null) {
            throw new NullPointerException("Null ticket");
        } else {
            this.ticket = ticket;
            this.ticketID = ticket.getID();

            if (!Validator.quantityValidator(ticketQty)) {
                throw new IllegalArgumentException("Invalid ticket quantity");
            } else {
                this.ticketQuantity = ticketQty;

                // Store a copy of the time, as the variable could be externally changed
                // after construction -> externally mutable object
            }
        }
        table = DatabaseTable.BOOKING;
    }

    public IOrder getOrder() {
        if (order == null) {
            throw new NullPointerException("Null customer booking order");
        }
        return order;
    }

    public Boolean setOrder(IOrder order) {
        if (order == null) {
            throw new NullPointerException("Cannot set user to null");
        } else {
            this.order = order;
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
