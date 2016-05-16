/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;

/**
 * The interface Guest is implemented by any class wishing to represent a Guest.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IGuest extends IUser {
    /**
     * Gets booking.
     *
     * @return the booking
     */
    IBooking getBooking();

    /**
     * Sets booking.
     *
     * @param booking the booking
     * @return the booking
     */
    boolean setBooking(IBooking booking);
}
