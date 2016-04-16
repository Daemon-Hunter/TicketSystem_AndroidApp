package com.example.aneurinc.prcs_app.Bookings;

import com.example.aneurinc.prcs_app.People.User;
import com.example.aneurinc.prcs_app.Tickets.Ticket;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IBookingFactory {
    public IBooking createBooking(Ticket ticket, User user, Integer quantity);

}
