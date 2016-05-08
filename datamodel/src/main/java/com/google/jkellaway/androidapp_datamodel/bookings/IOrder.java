/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IOrder {
    
    
    Integer getOrderID();
    IUser getUser() throws IOException;
    Integer getUserID();
    List<IBooking> getBookingList() throws IOException;
    IBooking getBooking(Integer bookingID);
    List<IBooking> loadMoreBookings() throws IOException;
    Boolean addBooking(IBooking booking);

}
