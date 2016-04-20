/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import java.util.Date;

import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.tickets.Ticket;

/**
 *
 * @author 10512691
 */
public class CustomerBooking extends Booking {
    
    private IOrder order;
    
    /**
     * Use this constructor when creating a booking object from the database.
     * @param ID
     * @param ticket
     * @param ticketQty
     * @param dateTime
     * @param order 
     */
    public CustomerBooking (Integer ID, ITicket ticket, Integer ticketQty, Date dateTime,
                            IOrder order) {
        super(ID, ticket, ticketQty, dateTime);
        this.order = order;
        table = DatabaseTable.BOOKING;
    }
    
    /**
     * Use this constructor when creating a new customer booking object.
     * @param ticket
     * @param ticketQty
     * @param dateTime
     * @param order 
     */
    public CustomerBooking (ITicket ticket, Integer ticketQty, Date dateTime,
            IOrder order) {
        super(ticket, ticketQty, dateTime);
        this.order = order;
        table = DatabaseTable.BOOKING;
    }

    public IOrder getOrder() {
        return order;
    }

    public Boolean setOrder(IOrder order) {
        if (order == null) {
            throw new NullPointerException("Cannot set user to null");
        } else {
            this.order = order;
            return this.order == order;
        }
    }
}
