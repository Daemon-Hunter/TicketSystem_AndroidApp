package com.example.aneurinc.prcs_app.Bookings;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.People.IUser;
import com.example.aneurinc.prcs_app.Tickets.Ticket;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
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
    public CustomerBooking (Integer ID, Ticket ticket, Integer ticketQty, Date dateTime,
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
    public CustomerBooking (Ticket ticket, Integer ticketQty, Date dateTime,
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

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Boolean setUser(IUser user) {
        return null;
    }
}

