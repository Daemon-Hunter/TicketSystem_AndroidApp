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
 *
 * @author 10467841
 */
public interface IDesktopWrapper extends IWrapper {
    Boolean      addCustomer(ICustomer customer);
    List<ICustomer>  getCustomers()throws IOException;
    ICustomer    getCustomer(Integer index) throws IOException;
    Boolean      removeCustomer(ICustomer customer);
    List<ICustomer>  loadMoreCustomers() throws IOException;
    List<ICustomer>  refreshCustomers() throws IOException;
    List<ICustomer> searchCustomers(String string) throws IOException;

    Boolean      addAdmin(IAdmin admin);
    IAdmin       getAdmin(Integer index) throws IOException;
    List<IAdmin> getAdmins() throws IOException;
    Boolean      removeAdmin(IAdmin admin);
    List<IAdmin>  loadMoreAdmins() throws IOException;
    List<IAdmin>  refreshAdmins() throws IOException;

    IOrder makeCustomerBooking(ICustomer customer , ITicket ticket, Integer quantity) throws IOException;

    Boolean      addGuestBooking(GuestBooking guest);
    List<GuestBooking>  getGuestBookings()throws IOException;
    GuestBooking    getGuestBooking(Integer index) throws IOException;
    Boolean      removeGuestBooking(GuestBooking customer);
    List<GuestBooking>  loadMoreGuestBookings() throws IOException;
    List<GuestBooking>  refreshGuestBookings() throws IOException;
    List<GuestBooking> searchGuestBookings(String string) throws IOException;

    Boolean loginAdmin(String email, String password) throws IOException;
    Boolean checkAdminPassword(String email, String password) throws IOException;
    IAdmin  getCurrentAdmin();

    List<IBooking> getThisMonthsSales() throws IOException;
    List<IChildEvent> getSoldOutEvents() throws IOException;
}
