/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IDbSubject;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author 10512691
 */
public interface ITicket extends IDbSubject {
    
    Integer getID();
    
    IChildEvent getEvent() throws IOException;
    Boolean    setEvent(IChildEvent event);
    
    Double getPrice();
    Boolean setPrice(Double price);
    
    String  getDescription();
    Boolean setDescription(String description);
    
    /**
     * Gets the amount of tickets of that ticket type remaining.
     * Child event can have multiple ticket types, so remaining
     * is stored in that ticket type.
     * @return 
     */
    Integer getRemaining();
    Boolean setRemaining(Integer remaining);
    
    String  getType();
    Boolean setType(String type);

    List<IBooking> getBookings() throws IOException;
    Boolean addBooking(IBooking booking);
    Boolean removeBooking(IBooking booking);
}
