/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.GuestBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.List;

/**
 * The interface Desktop wrapper.
 *
 * @author 10467841
 */
public interface IDesktopWrapper extends IWrapper {
    /**
     * Add customer boolean.
     *
     * @param customer the customer
     * @return the boolean
     */
    Boolean      addCustomer(ICustomer customer);

    /**
     * Gets customers.
     *
     * @return the customers
     * @throws IOException the io exception
     */
    List<ICustomer>  getCustomers()throws IOException;

    /**
     * Gets customer.
     *
     * @param index the index
     * @return the customer
     * @throws IOException the io exception
     */
    ICustomer    getCustomer(Integer index) throws IOException;

    /**
     * Remove customer boolean.
     *
     * @param customer the customer
     * @return the boolean
     */
    Boolean      removeCustomer(ICustomer customer);

    /**
     * Load more customers list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<ICustomer>  loadMoreCustomers() throws IOException;

    /**
     * Refresh customers list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<ICustomer>  refreshCustomers() throws IOException;

    /**
     * Search customers list.
     *
     * @param string the string
     * @return the list
     * @throws IOException the io exception
     */
    List<ICustomer> searchCustomers(String string) throws IOException;

    /**
     * Add admin boolean.
     *
     * @param admin the admin
     * @return the boolean
     */
    Boolean      addAdmin(IAdmin admin);

    /**
     * Gets admin.
     *
     * @param index the index
     * @return the admin
     * @throws IOException the io exception
     */
    IAdmin       getAdmin(Integer index) throws IOException;

    /**
     * Gets admins.
     *
     * @return the admins
     * @throws IOException the io exception
     */
    List<IAdmin> getAdmins() throws IOException;

    /**
     * Remove admin boolean.
     *
     * @param admin the admin
     * @return the boolean
     */
    Boolean      removeAdmin(IAdmin admin);

    /**
     * Load more admins list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IAdmin>  loadMoreAdmins() throws IOException;

    /**
     * Refresh admins list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<IAdmin>  refreshAdmins() throws IOException;

    /**
     * Make customer booking order.
     *
     * @param customer the customer
     * @param ticket   the ticket
     * @param quantity the quantity
     * @return the order
     * @throws IOException the io exception
     */
    IOrder makeCustomerBooking(ICustomer customer , ITicket ticket, Integer quantity) throws IOException;

    /**
     * Add guest booking boolean.
     *
     * @param guest the guest
     * @return the boolean
     */
    Boolean      addGuestBooking(GuestBooking guest);

    /**
     * Gets guest bookings.
     *
     * @return the guest bookings
     * @throws IOException the io exception
     */
    List<GuestBooking>  getGuestBookings()throws IOException;

    /**
     * Gets guest booking.
     *
     * @param index the index
     * @return the guest booking
     * @throws IOException the io exception
     */
    GuestBooking    getGuestBooking(Integer index) throws IOException;

    /**
     * Remove guest booking boolean.
     *
     * @param customer the customer
     * @return the boolean
     */
    Boolean      removeGuestBooking(GuestBooking customer);

    /**
     * Load more guest bookings list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<GuestBooking>  loadMoreGuestBookings() throws IOException;

    /**
     * Refresh guest bookings list.
     *
     * @return the list
     * @throws IOException the io exception
     */
    List<GuestBooking>  refreshGuestBookings() throws IOException;

    /**
     * Search guest bookings list.
     *
     * @param string the string
     * @return the list
     * @throws IOException the io exception
     */
    List<GuestBooking> searchGuestBookings(String string) throws IOException;

    /**
     * Login admin boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean loginAdmin(String email, String password) throws IOException;

    /**
     * Check admin password boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean checkAdminPassword(String email, String password) throws IOException;

    /**
     * Gets current admin.
     *
     * @return the current admin
     */
    IAdmin  getCurrentAdmin();

    /**
     * Gets this months sales.
     *
     * @return the this months sales
     * @throws IOException the io exception
     */
    List<IBooking> getThisMonthsSales() throws IOException;

    /**
     * Gets sold out events.
     *
     * @return the sold out events
     * @throws IOException the io exception
     */
    List<IChildEvent> getSoldOutEvents() throws IOException;
}
