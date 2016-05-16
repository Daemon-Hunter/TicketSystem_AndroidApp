/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import android.graphics.Bitmap;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicketFactory;
import com.google.jkellaway.androidapp_datamodel.tickets.TicketFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.createContract;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.descriptionValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.nameValidator;

/**
 * The child of a parent event, containing lineup and venue details, as well
 * as a further description and start/end times.
 *
 * The ChildEvent class represents a record in the ChildEvent table within the database.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class ChildEvent implements IChildEvent {

    private List<IArtist> artists;
    private IParentEvent parentEvent;
    private Integer parentEventID;
    private List<ITicket> tickets;
    private ITicketFactory ticketFactory;
    private IVenue venue;
    private Integer venueID;
    private Integer childEventID;
    private String childEventName, childEventDescription;
    private String startDateTime, endDateTime;
    private Boolean cancelled;
    private final DatabaseTable table;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);


    /**
     * ID and 'cancelled' variables being passed, so this constructor will be used when
     * creating an object already stored in the database.
     * Therefore do not need to check validation - will already have been checked.
     *
     * @param ID            the id
     * @param venueID       the venue id
     * @param name          the name
     * @param description   the description
     * @param startTime     the start time
     * @param endTime       the end time
     * @param cancelled     the cancelled
     * @param parentEventID the parent event id
     * @throws IOException the io exception
     */
    public ChildEvent(Integer ID, Integer venueID, String name, String description, String startTime, String endTime, Boolean cancelled, Integer parentEventID) throws IOException {
        this.childEventID = ID;
        this.childEventName = name;
        this.childEventDescription = description;
        this.startDateTime = startTime;
        this.endDateTime = endTime;
        this.cancelled = cancelled;
        this.table = DatabaseTable.CHILD_EVENT;
        this.parentEventID = parentEventID;
        this.venueID = venueID;
        venue = (IVenue) APIHandle.getSingle(this.venueID, DatabaseTable.VENUE);
    }

    /**
     * Instantiates a new Child event.
     *
     * @param name        the name
     * @param description the description
     * @param startTime   the start time
     * @param endTime     the end time
     * @param venue       the venue
     * @param parentEvent the parent event
     * @throws IllegalArgumentException the illegal argument exception
     */
    public ChildEvent(String name, String description, Date startTime, Date endTime, IVenue venue, IParentEvent parentEvent) throws IllegalArgumentException {
        childEventID = 0;

        nameValidator(name);
        descriptionValidator(description);

        this.childEventName = name;
        this.childEventDescription = description;
        this.startDateTime = formatter.format(startTime);
        this.endDateTime = formatter.format(endTime);
        this.venue = venue;
        this.cancelled = false;
        this.table = DatabaseTable.CHILD_EVENT;
        this.parentEvent = parentEvent;
    }

    @Override
    public ITicketFactory getTicketFactory() {
        if (ticketFactory == null) ticketFactory = new TicketFactory(this);
        return ticketFactory;
    }

    @Override
    public Integer getID() {
        return childEventID;
    }

    @Override
    public String getName() {
        return childEventName;
    }

    @Override
    public String getDescription() {
        return childEventDescription;
    }

    @Override
    public Date getStartDateTime() {
        try {
            return formatter.parse(startDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    @Override
    public Date getEndDateTime() {
        try {
            return formatter.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    @Override
    public Boolean getCancelled() {
        return cancelled;
    }

    @Override
    public Boolean setName(String name) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("Enter a name");
        nameValidator(name);
        childEventName = name;
        return childEventName.equals(name);
    }

    @Override
    public Boolean setDescription(String description) throws IllegalArgumentException {
        if (description == null) throw new IllegalArgumentException("Enter a description");
        descriptionValidator(description);
        childEventDescription = description;
        return childEventDescription.equals(description);
    }

    @Override
    public Boolean setStartDateTime(Date startDateTime) throws IllegalArgumentException {

        if (startDateTime == null) {
            throw new IllegalArgumentException("Enter a Date and Time.");
        } else {
            this.startDateTime = formatter.format(startDateTime);
        }
        return this.startDateTime.equals(formatter.format(startDateTime));
    }

    @Override
    public Boolean setEndDateTime(Date endDateTime) throws IllegalArgumentException {
        if (endDateTime == null) {
            throw new IllegalArgumentException("end time is null");
        } else {
            this.endDateTime = formatter.format(endDateTime);
        }
        return this.endDateTime.equals(formatter.format(endDateTime));
    }

    @Override
    public Boolean setCancelled(Boolean cancelled) throws IllegalArgumentException {
        if (cancelled == null) {
            throw new IllegalArgumentException("Cannot set 'cancelled' flag to null");
        } else {
            this.cancelled = cancelled;
        }
        return this.cancelled.equals(cancelled);
    }

    @Override
    public Integer getVenueID() {
        return venueID;
    }

    @Override
    public List<IArtist> getArtistList() throws IOException {
        if (artists == null) {
            artists = (List<IArtist>) (Object) APIHandle.getObjectsFromObject(this.childEventID, DatabaseTable.ARTIST, DatabaseTable.CHILD_EVENT);
            return new LinkedList<>(artists);
        } else {
            return new LinkedList<>(artists);
        }
    }

    @Override
    public Integer getParentEventID() {
        if (parentEventID == null) parentEventID = parentEvent.getID();
        return parentEventID;
    }

    @Override
    public IParentEvent getParentEvent() throws IOException {
        if (this.parentEvent == null) {
            parentEvent = (IParentEvent) APIHandle.getSingle(parentEventID, DatabaseTable.PARENT_EVENT);
            parentEventID = parentEvent.getID();
        }
        return this.parentEvent;
    }

    @Override
    public Boolean setParentEvent(IParentEvent event) {
        if (event == null) {
            return false;
        } else {
            this.parentEvent = event;
            this.parentEventID = event.getID();
            return true;
        }
    }

    @Override
    public ITicket getTicket(Integer id) throws IOException {
        if (tickets != null) {
            for (ITicket ticket : tickets) {
                if (ticket.getID().equals(id)) return ticket;
            }
        }
        APIHandle.getSingle(id, DatabaseTable.TICKET);
        throw new IllegalArgumentException("No item in the list contains that id.");
    }

    @Override
    public List<ITicket> getTickets() throws IOException {
        if (tickets == null)
            tickets = (List<ITicket>) (Object) APIHandle.getObjectsFromObject(this.childEventID, DatabaseTable.TICKET, DatabaseTable.CHILD_EVENT);
        return tickets;
    }

    @Override
    public Boolean addTicket(ITicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Cannot add a null ticket.");
        }
        return tickets.add(ticket);
    }

    @Override
    public Boolean removeTicket(ITicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Cannot remove a null ticket.");
        }
        return tickets.remove(ticket);
    }

    @Override
    public void setVenueID(Integer venue) {
        this.venueID = venue;
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        if (socialMedia == null) {
            throw new IllegalArgumentException("SocialMedia cannot be null");
        }
        this.parentEvent.setSocialMedia(socialMedia);
    }

    @Override
    public Boolean newContract(IArtist artist) throws IOException {
        if (createContract(artist.getID(), this.childEventID)) {
            if (artists == null) artists = new LinkedList<>();
            artists.add(artist);
            return true;
        }
        return false;
    }

    @Override
    public Boolean setVenue(IVenue venue) {
        if (venue == null) {
            throw new NullPointerException("Cannot set venue to null");
        } else {
            this.venue = venue;
        }
        return true;
    }

    @Override
    public IVenue getVenue() {
        return venue;
    }

    @Override
    public Integer getSocialId() {
        return parentEvent.getSocialId();
    }

    @Override
    public Boolean setSocialId(Integer id) {
        return parentEvent.setSocialId(id);
    }

    @Override
    public List<Bitmap> getImages() {
        return parentEvent.getImages();
    }

    @Override
    public Bitmap getImage(int index) {
        return parentEvent.getImage(index);
    }

    @Override
    public Boolean addImage(Bitmap img) {
        return parentEvent.addImage(img);
    }

    @Override
    public Boolean removeImage(int index) {
        return parentEvent.removeImage(index);
    }

    @Override
    public Boolean setImages(List<Bitmap> images) {
        return parentEvent.setImages(images);
    }

    @Override
    public String getFacebook() {
        return parentEvent.getFacebook();
    }

    @Override
    public Boolean setFacebook(String fb) {
        return parentEvent.setFacebook(fb);
    }

    @Override
    public String getTwitter() {
        return parentEvent.getTwitter();
    }

    @Override
    public Boolean setTwitter(String tw) {
        return parentEvent.setTwitter(tw);
    }

    @Override
    public String getInstagram() {
        return parentEvent.getInstagram();
    }

    @Override
    public Boolean setInstagram(String insta) {
        return parentEvent.setInstagram(insta);
    }

    @Override
    public String getSoundcloud() {
        return parentEvent.getSoundcloud();
    }

    @Override
    public Boolean setSoundcloud(String sc) {
        return parentEvent.setSoundcloud(sc);
    }

    @Override
    public String getWebsite() {
        return parentEvent.getWebsite();
    }

    @Override
    public Boolean setWebsite(String web) {
        return parentEvent.setWebsite(web);
    }

    @Override
    public String getSpotify() {
        return parentEvent.getSpotify();
    }

    @Override
    public Boolean setSpotify(String sp) {
        return parentEvent.setSpotify(sp);
    }
}
