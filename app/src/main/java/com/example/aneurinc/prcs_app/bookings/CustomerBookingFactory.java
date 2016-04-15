package com.example.aneurinc.prcs_app.bookings;

import com.example.aneurinc.prcs_app.Tickets.Ticket;

/**
 * Created by Dominic on 14/04/2016.
 */
public class CustomerBookingFactory {

    public IBooking createBooking(Ticket ticket, IOrder order, Integer quantity) {
        return new CustomerBooking(0, ticket, quantity, null, order);
    }
}

