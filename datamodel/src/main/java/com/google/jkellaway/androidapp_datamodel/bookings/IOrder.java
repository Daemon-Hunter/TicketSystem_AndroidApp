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
     * Gets primary  id associated with the order object.
     *
     * @return the order id
     */
    Integer getOrderID();

    /**
     * Gets the user that the order object that the  is for.
     *
     * @return the user for the order
     * @throws IOException that is thrown if there is no user found
     */
    IUser getUser() throws IOException;

    /**
     * Gets the primary  id that is associated with the user object
     *
     * @return the user's id
     */
    Integer getUserID();

    /**
     * Gets the list of bookings that have been made with the this order's id
     * @return a list of IBooking objects
     * @throws IOException if there are no bookings found or a connection error occurs
     */
    List<IBooking> getBookingList() throws IOException;

    /**
     * Gets the booking that is associated with this order with a given ID
     *
     * @param bookingID the primary ID that is needed to get the booking object for the order
     * @return the IBooking object that has the primary ID of the one passed through it
     */
    IBooking getBooking(Integer bookingID);

    /**
     * Add a new booking to the order.
     *
     * @param booking the booking that you wish to associate with the order
     * @return Boolean - dependant on whether the booking was successfully added
     */
    Boolean addBooking(IBooking booking);

}
