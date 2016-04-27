/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IDbSubject;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IOrder extends IDbSubject {
    
    
    public Integer getOrderID();
    public IUser getUser() throws IOException;
    public List<IBooking> getBookingList() throws IOException;
    public IBooking getBooking(Integer bookingID);
}
