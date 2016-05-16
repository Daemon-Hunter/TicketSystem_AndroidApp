/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.reviews.IHaveReviews;

import java.io.IOException;
import java.util.List;

/**
 * The interface ICustomer is implemented by any class wishing to represent a customer.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface ICustomer extends IUser, IHaveReviews {
    /**
     * Gets order list.
     *
     * @return the order list
     * @throws IOException the io exception
     */
    List<IOrder> getOrderList() throws IOException;

    /**
     * Gets order.
     *
     * @param orderID the order id
     * @return the order
     * @throws IOException the io exception
     */
    IOrder getOrder(int orderID) throws IOException;

    /**
     * Add order boolean.
     *
     * @param order the order
     * @return the boolean
     */
    Boolean addOrder(IOrder order);

    /**
     * Add order list boolean.
     *
     * @param orderList the order list
     * @return the boolean
     */
    Boolean addOrderList(List<IOrder> orderList);

    /**
     * Remove order boolean.
     *
     * @param order the order
     * @return the boolean
     */
    Boolean removeOrder(IOrder order);

    /**
     * Gets bookings.
     *
     * @return the bookings
     * @throws IOException the io exception
     */
    List<IBooking> getBookings() throws IOException;

    /**
     * Load more bookings list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IBooking> loadMoreBookings() throws IOException;

}
