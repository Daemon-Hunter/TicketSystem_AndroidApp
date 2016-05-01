/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;

/**
 *
 * @author 10467841
 */
public interface IGuest extends IUser {
    IBooking getBooking();
    boolean setBooking(IBooking booking);
}
