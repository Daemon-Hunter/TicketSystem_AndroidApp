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
 * The interface Order.
 *
 * @author 10467841
 */
public interface IOrder {


    /**
     * Gets order id.
     *
     * @return the order id
     */
    Integer getOrderID();

    /**
     * Gets user.
     *
     * @return the user
     * @throws IOException the io exception
     */
    IUser getUser() throws IOException;

    /**
     * Gets user id.
     *
     * @return the user id
     */
    Integer getUserID();

    /**
     * Gets booking list.
     *
     * @return the booking list
     * @throws IOException the io exception
     */
    List<IBooking> getBookingList() throws IOException;

    /**
     * Gets booking.
     *
     * @param bookingID the booking id
     * @return the booking
     */
    IBooking getBooking(Integer bookingID);

    /**
     * Add booking boolean.
     *
     * @param booking the booking
     * @return the boolean
     */
    Boolean addBooking(IBooking booking);

}
