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
     * Add a Customer to the customer list.
     *
     * @param customer The customer.
     * @return Boolean to indicate completion of add operation.
     */
    Boolean addCustomer(ICustomer customer);

    /**
     * Returns all Customers.
     *
     * @return A list of Customers.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<ICustomer> getCustomers() throws IOException;

    /**
     * Gets an individual customer.
     *
     * @param index ID of Customer.
     * @return A Customer matching the ID.
     * @throws IOException Thrown if connection to the database fails.
     */
    ICustomer getCustomer(Integer index) throws IOException;

    /**
     * Removes a Customer.
     *
     * @param customer The Customer to be removed.
     * @return Boolean to indicate removal.
     */
    Boolean removeCustomer(ICustomer customer);

    /**
     * Loads more
     *
     * @return The updated list that includes the new Customers.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<ICustomer> loadMoreCustomers() throws IOException;

    /**
     * Refreshs Customer list to show latest data from the database.
     *
     * @return The latest copy of Customers.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<ICustomer> refreshCustomers() throws IOException;

    /**
     * Search Customer list for a string.
     *
     * @param string The string query to search for.
     * @return A list that matched the query.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<ICustomer> searchCustomers(String string) throws IOException;

    /**
     * Adds an Admin to the admin list.
     *
     * @param admin The Admin to be added.
     * @return A boolean to indicate success of the adding operation.
     */
    Boolean addAdmin(IAdmin admin);

    /**
     * Gets an Admin by its ID.
     *
     * @param index The ID of the Admin to be returned.
     * @return The Admin that matched the ID.
     * @throws IOException Thrown if connection to the database fails.
     */
    IAdmin getAdmin(Integer index) throws IOException;

    /**
     * Gets all Admins.
     *
     * @return All Admins in the admin list.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IAdmin> getAdmins() throws IOException;

    /**
     * Removes an Admin from the admin list.
     *
     * @param admin The Admin to be removed.
     * @return Boolean to indicate success of removal.
     */
    Boolean removeAdmin(IAdmin admin);

    /**
     * Loads more Admins into the admin list.
     *
     * @return The updated list including the new Admins.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IAdmin> loadMoreAdmins() throws IOException;

    /**
     * Refresh latest copy of the admin list.
     *
     * @return The updated list of admins.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IAdmin> refreshAdmins() throws IOException;

    /**
     * Make Customer booking order.
     *
     * @param customer The Customer who is making the booking.
     * @param ticket   The selected ticket.
     * @param quantity The quantity of that ticket.
     * @return The order containing the ticket and booking.
     * @throws IOException the io exception
     */
    IOrder makeCustomerBooking(ICustomer customer, ITicket ticket, Integer quantity) throws IOException;

    /**
     * Add a guest booking.
     *
     * @param guest The Guest who is making the booking.
     * @return Success to indicate the outcome of the add operation.
     */
    Boolean addGuestBooking(GuestBooking guest);

    /**
     * Get all Guest bookings.
     *
     * @return The a list of all Guest bookings.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<GuestBooking> getGuestBookings() throws IOException;

    /**
     * Gets a guest booking by its ID.
     *
     * @param index ID of guest.
     * @return The booking that matches the ID.
     * @throws IOException Thrown if connection to the database fails.
     */
    GuestBooking getGuestBooking(Integer index) throws IOException;

    /**
     * Removes a guest booking.
     *
     * @param customer The booking to be removed.
     * @return Boolean to indicate removal.
     */
    Boolean removeGuestBooking(GuestBooking customer);

    /**
     * Load more guests into the bookings list.
     *
     * @return Up to date list.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<GuestBooking> loadMoreGuestBookings() throws IOException;

    /**
     * Refresh guest bookings list to get the latest copy.
     *
     * @return Up to date copy of the guest bookings list.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<GuestBooking> refreshGuestBookings() throws IOException;

    /**
     * Search guest bookings using a query.
     *
     * @param string The query.
     * @return A list of bookings that match the query.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<GuestBooking> searchGuestBookings(String string) throws IOException;

    /**
     * Login Admin.
     *
     * @param email    Email of the Admin.
     * @param password Password of the Admin.
     * @return Boolean to indicate success of operation.
     * @throws IOException Thrown if connection to the database fails.
     */
    Boolean loginAdmin(String email, String password) throws IOException;

    /**
     * Check admin password.
     *
     * @param email    Admin email.
     * @param password Admin password.
     * @return Boolean to indicate success of operation.
     * @throws IOException Thrown if connection to the database fails.
     */
    Boolean checkAdminPassword(String email, String password) throws IOException;

    /**
     * Gets current admin.
     *
     * @return The current admin.
     */
    IAdmin getCurrentAdmin();

    /**
     * Gets all sales from this month.
     *
     * @return A list of this months sales.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IBooking> getThisMonthsSales() throws IOException;

    /**
     * Gets all sold out events.
     *
     * @return A list of sold out events.
     * @throws IOException Thrown if connection to the database fails.
     */
    List<IChildEvent> getSoldOutEvents() throws IOException;
}
