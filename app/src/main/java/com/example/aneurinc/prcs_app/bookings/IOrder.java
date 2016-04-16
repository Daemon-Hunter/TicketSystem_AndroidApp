package com.example.aneurinc.prcs_app.Bookings;

import com.example.aneurinc.prcs_app.People.IUser;
import com.example.aneurinc.prcs_app.Utility.Observer.IDbSubject;

import java.util.List;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IOrder extends IDbSubject {

    public Integer getOrderID();
    public IUser getUser();
    public List<Booking> getBookingList();
    public Booking getBooking(Integer bookingID);
    public Boolean removeBooking(Booking booking);
    public Boolean addBooking(Booking booking);
    public void addBookingList(List<Booking> bookingList);
}
