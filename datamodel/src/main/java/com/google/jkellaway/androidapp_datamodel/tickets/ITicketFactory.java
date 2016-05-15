/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

/**
 * The interface Ticket factory.
 *
 * @author 10512691
 */
public interface ITicketFactory {

    /**
     * Create ticket ticket.
     *
     * @param price       the price
     * @param description the description
     * @param remaining   the remaining
     * @param type        the type
     * @return the ticket
     */
    ITicket createTicket(Double price, String description, Integer remaining, String type);
}
