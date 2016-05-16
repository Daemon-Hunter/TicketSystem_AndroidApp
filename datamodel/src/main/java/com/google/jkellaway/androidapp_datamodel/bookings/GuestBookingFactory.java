/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.Ticket;

/**
 * The type Guest booking factory is used to create GuestBooking objects.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class GuestBookingFactory {

    /**
     * Create booking booking.
     *
     * @param ticket   the ticket that you wish to attach the booking to
     * @param guest    the guest object that the booking is being made for
     * @param quantity the amount of tickets that have been purchased
     * @return the booking object that has been created
     */
    public IBooking createBooking(Ticket ticket, IUser guest, Integer quantity) {
        return new GuestBooking(ticket, quantity, null, guest);
    }
}
