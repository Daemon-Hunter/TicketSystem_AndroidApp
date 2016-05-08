/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.getBookingAmount;

/**
 *
 * @author 10467841
 */
public class Order implements IOrder {
   
    private Integer orderID;
    private Integer userID;
    private IUser user;
    private List<IBooking> bookingList;
    private DatabaseTable table = DatabaseTable.ORDER;
    
    /**
     * Use this constructor when creating an order object from the database.
     * @param ID
     */
    public Order(Integer ID, Integer userID) {
        this.orderID = ID;
        this.userID = userID;

    }

    /**
     * Use this constructor when create a new order object
     * @param userID
     */
    public Order(Integer userID) {
        this.orderID = 0;
        this.userID = userID;
    }
    
    /**
     * Gets the order's unique ID
     * @return 
     */
    @Override
    public Integer getOrderID() {
        return orderID;
    }

    /**
     * Returns the user that made the order
     * @return 
     */
    @Override
    public IUser getUser() throws IOException {
        user = (IUser)APIHandle.getSingle(userID, DatabaseTable.CUSTOMER);
        userID = user.getID();
        return user;
    }

    @Override
    public Integer getUserID() {
        return userID;
    }

    /**
     * Gets the list of bookings related to the order
     * @return 
     */
    @Override
    public List<IBooking> getBookingList() throws IOException {
        bookingList = getBookingAmount(this.orderID, 4, 0);
        return new LinkedList(bookingList);
    }

    /**
     * Returns a single booking, get via booking ID.
     * @param bookingID
     * @return 
     */
    @Override
    public IBooking getBooking(Integer bookingID) {
        return bookingList.get(bookingID);
    }

    @Override
    public List<IBooking> loadMoreBookings() throws IOException {
        int lowestID = 0;
        for (IBooking booking : bookingList){
            if (booking.getBookingID() < lowestID || lowestID == 0)
                lowestID = booking.getBookingID();
        }
        List<IBooking> newData = getBookingAmount(this.orderID, 9, 0);
        bookingList.addAll(newData);
        return new LinkedList<>(newData);
    }

    @Override
    public Boolean addBooking(IBooking booking) {
        if (booking == null){
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (bookingList == null)
            bookingList = new LinkedList();
        return bookingList.add(booking);
    }
}
