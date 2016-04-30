/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;

/**
 *
 * @author 10512691
 */
public class Guest extends User implements IGuest {
    
    /**
     * Use this constructor when creating a new Guest object.
     * ID is unknown.
     * @param email
     * @param address
     * @param pcode 
     */
    
    private IBooking booking;
    
    public Guest(String email, String address, String pcode)
    {
        super("GUEST", "ACCOUNT", email, address, pcode);
        table = DatabaseTable.GUEST_BOOKING;
    }
    
    /**
     * Use this constructor when creating an object from the database.
     * ID is known.
     * @param ID
     * @param email
     * @param address
     * @param pcode 
     */
    public Guest(Integer ID, String email, String address, String pcode)
    {
        super(ID, "GUEST", "ACCOUNT", email, address, pcode);
        table = DatabaseTable.GUEST_BOOKING;
    }

    @Override
    public Boolean setFirstName(String name) {
        return false;
    }

    @Override
    public Boolean setLastName(String name) {
        return false;
    }

    @Override
    public Boolean setPassword(String password) {
        return false;
    }

    @Override
    public IBooking getBooking() {
        return this.booking;
    }

    @Override
    public boolean setBooking(IBooking booking) {
        if (booking == null){
            throw new IllegalArgumentException("Booking cannot be null");
        } else {
            this.booking = booking;
            return this.booking == booking;
        }
    }
}