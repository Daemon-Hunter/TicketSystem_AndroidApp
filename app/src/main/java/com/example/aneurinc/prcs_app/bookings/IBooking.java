package com.example.aneurinc.prcs_app.bookings;

import com.example.aneurinc.prcs_app.People.IUser;
import com.example.aneurinc.prcs_app.Tickets.Ticket;
import com.example.aneurinc.prcs_app.utilities.Observer.IDbSubject;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
 */

public interface IBooking extends IDbSubject {
    public Integer getBookingID();

    public Ticket  getTicket();
    public Boolean setTicket(Ticket ticket);

    public Integer getQuantity();
    public Boolean setQuantity(Integer qty);

    public Date    getBookingTime();
    public Boolean setBookingTime(Date time);

    public IUser    getUser();
    public Boolean setUser(IUser user);

}
