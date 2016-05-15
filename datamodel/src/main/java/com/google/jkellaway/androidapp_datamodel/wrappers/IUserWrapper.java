/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.people.IUser;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.List;

/**
 * The interface User wrapper.
 *
 * @author 10467841
 */
public interface IUserWrapper extends IWrapper {

    /**
     * Login user boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean loginUser(String email, String password) throws IOException;

    /**
     * Gets user.
     *
     * @return the user
     */
    IUser   getUser();

    /**
     * Register user user.
     *
     * @param customer the customer
     * @return the user
     * @throws IOException the io exception
     */
    IUser registerUser(IUser customer) throws IOException;

    /**
     * Make customer booking order.
     *
     * @param tickets    the tickets
     * @param quantities the quantities
     * @return the order
     * @throws IOException the io exception
     */
    IOrder makeCustomerBooking(List<ITicket> tickets, List<Integer> quantities) throws IOException;

}
