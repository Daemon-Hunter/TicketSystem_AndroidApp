package com.example.aneurinc.prcs_app.Tickets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import com.example.aneurinc.prcs_app.Database.DatabaseTable;
        import com.example.aneurinc.prcs_app.Datamodel.ChildEvent;
        import com.example.aneurinc.prcs_app.utilities.Observer.IDbSubject;
        import com.example.aneurinc.prcs_app.utilities.Observer.IObserver;

        import java.util.LinkedList;
        import java.util.List;

        import static com.example.aneurinc.prcs_app.Utility.Blacklist.contains;
        import static com.example.aneurinc.prcs_app.Utility.Validator.descriptionValidator;

/**
 *
 * @author 10512691
 */
public class Ticket implements ITicket, IDbSubject {

    private Integer         ticketID;
    private ChildEvent      event;
    private Double          price;
    private String          description;
    private Integer         amountRemaining;
    private String          type;
    private List<IObserver> observers;
    private DatabaseTable   table;

    /**
     * Use this constructor when creating a new ticket object.
     * Use the constructor when creating an object from the database.
     * @param ID Unique number for the ticket given by the database.
     * @param event The event the ticket is for.
     * @param price Price of the ticket.
     * @param desc Description of the ticket.
     * @param remaining Number remaining (total number of tickets at time of construction).
     * @param type The ticket type (standing / seating / weekend etc.)
     */
    public Ticket(Integer ID, ChildEvent event, Double price, String desc,
                  Integer remaining, String type) {
        ticketID = ID;
        this.event = event;
        this.price = price;
        description = desc;
        amountRemaining = remaining;
        this.type = type;
    }

    public Ticket(ChildEvent event, Double price, String desc, Integer remaining,
                  String type) {
        if (event == null) {
            throw new NullPointerException("Cannot make a ticket for a null event");
        } else {
            this.event = event;
        }

        if (0 <= price) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Cannot set a price below 0!");
        }
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public void notifyObservers() {
        if (observers == null) {
            observers = new LinkedList();
        } else {
            for (IObserver o : observers) {
                o.update(this);
            }
        }
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("cannot register null observer");
        } else {
            if (!observers.contains(o)) {
                return observers.add(o);
            } else {
                return false;
            }
        }
    }


    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("cannot remove null observer");
        } else {
            if (observers.contains(o)) {
                return observers.remove(o);
            } else {
                return false;
            }
        }
    }

    @Override
    public Integer getID() {
        return ticketID;
    }

    @Override
    public ChildEvent getEvent() {
        return event;
    }

    @Override
    public Boolean setEvent(ChildEvent event) {
        if (event == null) {
            throw new NullPointerException("Cannot set event to null");
        } else {
            this.event = event;
            return this.event.equals(event);
        }
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Boolean setPrice(Double price) {
        if (0 <= price) {
            this.price = price;
            return this.price.equals(price);
        } else return false;
    }

    @Override
    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            throw new NullPointerException("description is null!");
        }
    }

    @Override
    public Boolean setDescription(String description) {
        if (descriptionValidator(description)) {
            this.description = description;
            return this.description.equals(description);
        } else {
            return false;
        }
    }

    @Override
    public Integer getRemaining() {
        return amountRemaining;
    }

    @Override
    public Boolean setRemaining(Integer remaining) {
        if (0 <= remaining) {
            amountRemaining = remaining;
            return amountRemaining.equals(remaining);
        } else return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Boolean setType(String type) {
        if (!contains(type)) {
            this.type = type;
            return this.type.equals(type);
        } else {
            return false;
        }
    }

}

