/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import java.util.List;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IDbSubject;

/**
 *
 * @author 10467841
 */
public interface IOrder extends IDbSubject {
    
    
    public Integer getOrderID();
    public IUser getUser();
    public List<IBooking> getBookingList();
    public IBooking getBooking(Integer bookingID);
}
