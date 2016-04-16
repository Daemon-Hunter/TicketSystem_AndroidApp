package com.example.aneurinc.prcs_app.Tickets;

import com.example.aneurinc.prcs_app.Datamodel.ChildEvent;
import com.example.aneurinc.prcs_app.Utility.Observer.IDbSubject;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface ITicket extends IDbSubject {

    public Integer getID();

    public ChildEvent getEvent();
    public Boolean    setEvent(ChildEvent event);

    public Double getPrice();
    public Boolean setPrice(Double price);

    public String  getDescription();
    public Boolean setDescription(String description);

    /**
     * Gets the amount of tickets of that ticket type remaining.
     * Child event can have multiple ticket types, so remaining
     * is stored in that ticket type.
     * @return
     */
    public Integer getRemaining();
    public Boolean setRemaining(Integer remaining);

    public String  getType();
    public Boolean setType(String type);
}
