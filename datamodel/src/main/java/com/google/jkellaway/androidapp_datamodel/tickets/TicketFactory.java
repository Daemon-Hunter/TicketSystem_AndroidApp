/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;

/**
 * The class Ticket factory is used to create an instance of the ticket class.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class TicketFactory implements ITicketFactory {

    /**
     * The Event.
     */
    IChildEvent event;

    /**
     * Instantiates a new Ticket factory.
     *
     * @param event the event
     */
    public TicketFactory(IChildEvent event) {
        this.event = event;
    }

    /**
     * Creates a ticket for the child event, adding it to the event on creation.
     *
     * @param price
     * @param description
     * @param remaining
     * @param type
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public ITicket createTicket(Double price, String description, Integer remaining, String type) throws IllegalArgumentException {
        ITicket ticket = new Ticket(event, price, description, remaining, type);
        event.addTicket(ticket);
        return ticket;
    }
}
