/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10467841
 */
public class Order implements IOrder{
   
    private Integer orderID;
    private Integer userID;
    private IUser user;
    private List<IBooking> bookingList;
    protected LinkedList<IObserver> observers;
    
    /**
     * 
     * @param ID
     * @param user
     * @param bList 
     */
    public Order(Integer ID, IUser user, List<IBooking> bList){
        this.orderID = ID;
        this.user = user;
        if (bList != null){
            this.bookingList = bList;
        } else {
            this.bookingList = new LinkedList<>();
        }

    }

    public Order(Integer ID, Integer userID){
        this.orderID = ID;
        this.userID = userID;
        this.bookingList = new LinkedList<>();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public Integer getOrderID() {
        return orderID;
    }

    /**
     * 
     * @return 
     */
    @Override
    public IUser getUser() throws IOException {
        user = (IUser)APIHandle.getSingle(userID, DatabaseTable.CUSTOMER);
        return user;
    }

    /**
     * 
     * @return 
     */
    @Override
    public List<IBooking> getBookingList() throws IOException {
        bookingList = (List<IBooking>)(Object)APIHandle.getObjectsFromObject(this.orderID, DatabaseTable.BOOKING, DatabaseTable.ORDER);
        return new ArrayList(bookingList);
    }

    /**
     * 
     * @param bookingID
     * @return 
     */
    @Override
    public IBooking getBooking(Integer bookingID) {
        return bookingList.get(bookingID);
    }

    @Override
    public DatabaseTable getTable() {
        return DatabaseTable.ORDER;
    }

    @Override
    public void notifyObservers() {
        if (observers == null) {
            observers = new LinkedList();
        } else {
            for (IObserver o : observers) {
                o.update(this);
            }
        }
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (observers == null) {
            observers = new LinkedList<>();
            observers.add(o);
            return observers.contains(o);
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already exists in list");
        } else {
            observers.add(o);
            return observers.contains(o);
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer doesn't exist in list");
        } else {
            observers.remove(o);
            return !observers.contains(o);
        }
    }
}
