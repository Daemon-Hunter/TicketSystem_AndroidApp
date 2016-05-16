/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

/**
 * The type ITicketFactory is implemented by classes wishing to be able to create tickets.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
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
