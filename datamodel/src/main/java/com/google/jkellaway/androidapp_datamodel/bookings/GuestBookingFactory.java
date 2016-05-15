/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.Ticket;

/**
 * The type Guest booking factory.
 *
 * @author 10512691
 */
public class GuestBookingFactory {

    /**
     * Create booking booking.
     *
     * @param ticket   the ticket
     * @param guest    the guest
     * @param quantity the quantity
     * @return the booking
     */
    public IBooking createBooking(Ticket ticket, IUser guest, Integer quantity) {
        return new GuestBooking(ticket, quantity, null, guest);
    }
}
