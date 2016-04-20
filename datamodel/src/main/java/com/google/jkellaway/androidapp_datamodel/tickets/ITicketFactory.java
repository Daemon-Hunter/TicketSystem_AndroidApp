/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.tickets;

import com.google.jkellaway.androidapp_datamodel.datamodel.ChildEvent;

/**
 *
 * @author 10512691
 */
public interface ITicketFactory {
    
    public Ticket createTicket(ChildEvent event, Double price, 
            String description, Integer remaining, String type);
}
