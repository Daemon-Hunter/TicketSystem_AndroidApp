/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.Guest;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.util.Date;

/**
 *
 * @author 10512691
 */
public class GuestBooking extends Booking {
    
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
        super(ID, ticketID, ticketQty, dateTime);
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
        super(ticket, ticketQty, dateTime);
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
}
