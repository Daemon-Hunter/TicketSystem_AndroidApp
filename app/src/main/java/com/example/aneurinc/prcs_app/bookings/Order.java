package com.example.aneurinc.prcs_app.Bookings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.People.IUser;
import com.example.aneurinc.prcs_app.Utility.Observer.IObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 10467841
 */
public class Order implements IOrder{

    private Integer orderID;
    private IUser user;
    private List<Booking> bookingList;
    protected LinkedList<IObserver> observers;

    /**
     *
     * @param ID
     * @param user
     * @param bList
     */
    public Order(Integer ID, IUser user, List<Booking> bList){
        this.orderID = ID;
        this.user = user;
        if (bList != null){
            this.bookingList = bList;
        } else {
            this.bookingList = new ArrayList();
        }

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
    public IUser getUser() {
        return user;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Booking> getBookingList() {
        return new ArrayList(bookingList);
    }

    /**
     *
     * @param bookingID
     * @return
     */
    @Override
    public Booking getBooking(Integer bookingID) {
        return bookingList.get(bookingID);
    }

    /**
     *
     * @param booking
     * @return
     */
    @Override
    public Boolean removeBooking(Booking booking) {
        return bookingList.remove(booking);
    }

    /**
     *
     * @param booking
     * @return
     */
    @Override
    public Boolean addBooking(Booking booking) {
        return bookingList.add(booking);
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

    @Override
    public void addBookingList(List<Booking> bookingList) {
        this.bookingList = new LinkedList(bookingList);
    }
}
