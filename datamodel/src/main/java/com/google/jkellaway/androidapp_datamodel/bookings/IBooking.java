/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.bookings;

import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author 10512691
 */
public interface IBooking {
    Integer getBookingID();

    Integer getTicketID();
    ITicket getTicket() throws IOException;
    Boolean setTicket(ITicket ticket);
    
    Integer getQuantity();
    Boolean setQuantity(Integer qty);
    
    Date    getBookingTime();
    Boolean setBookingTime(Date time);
}
