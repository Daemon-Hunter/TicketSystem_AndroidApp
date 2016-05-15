/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

/**
 * The interface Booking factory.
 *
 * @author 10467841
 */
public interface IBookingFactory {
     /**
      * Create booking booking.
      *
      * @param ticket   the ticket
      * @param order    the order
      * @param quantity the quantity
      * @return the booking
      */
     IBooking createBooking(ITicket ticket, IOrder order, Integer quantity);
}
