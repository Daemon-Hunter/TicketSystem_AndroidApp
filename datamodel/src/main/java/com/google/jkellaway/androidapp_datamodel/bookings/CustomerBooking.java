/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.quantityValidator;

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
    private String  bookingDateTime;
    private IOrder order;
    private Integer orderID;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    
    /**
     * Use this constructor when creating a booking object from the database.
     * @param ID
     * @param ticketQty
     * @param dateTime
     */
    public CustomerBooking (Integer ID, Integer ticketID, Integer orderID, Integer ticketQty, Date dateTime) throws IllegalArgumentException {
        this.bookingID = ID;
        this.ticketID = ticketID;
        this.ticketQuantity = ticketQty;
        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object
        this.bookingDateTime = formatter.format(dateTime);
        this.table = DatabaseTable.BOOKING;
        this.orderID = orderID;
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
        this.bookingDateTime = formatter.format(Calendar.getInstance().getTime());

        if (ticket == null)
            throw new IllegalArgumentException("Null ticket");
        this.ticket = ticket;
        this.ticketID = ticket.getID();

        quantityValidator(ticketQty);
        this.ticketQuantity = ticketQty;

        // Store a copy of the time, as the variable could be externally changed
        // after construction -> externally mutable object


        table = DatabaseTable.BOOKING;
    }

    public IOrder getOrder() throws IOException {
        if (order == null) {
            order = (IOrder) APIHandle.getSingle(orderID, DatabaseTable.ORDER);
            orderID = order.getOrderID();
        }
        return order;
    }

    public Integer getOrderID(){
        return orderID;
    }

    public Boolean setOrder(IOrder order) {
        if (order == null) {
            throw new NullPointerException("Cannot set user to null");
        } else {
            this.order = order;
            this.orderID = order.getOrderID();
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
        if (qty == null)
            throw new NullPointerException("Null quantity");
        quantityValidator(qty);
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
            throw new IllegalArgumentException("Enter a Date and Time.");
        } else {
            // Store a copy of the time, as the variable could be externally changed
            // after construction -> externally mutable object
            bookingDateTime = formatter.format(time);
            return true;
        }
    }

    public DatabaseTable getTable() {
        return table;
    }
}
