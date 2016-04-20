/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.datamodel;

import android.graphics.Bitmap;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

/**
 * The child of a parent event, containing lineup and venue details, as well
 * as a further description and start/end times.
 * @author 10512691
 */
public class ChildEvent implements IChildEvent {

    private List<Integer> artistIDs;
    private List<IArtist> artists;
    private IParentEvent parentEvent;
    
    private Integer childEventID;
    private String childEventName, childEventDescription;
    private Date startDateTime, endDateTime;
    private Boolean cancelled;
    private IVenue venue;
    private List<IObserver> observers;
    private final DatabaseTable table;

    /**
     * ID and 'cancelled' variables being passed, so this constructor will be used when 
     * creating an object already stored in the database.
     * Therefore do not need to check validation - will already have been checked.
     * @param ID
     * @param name
     * @param description
     * @param startTime
     * @param endTime
     * @param venue
     * @param cancelled
     */
    public ChildEvent(Integer ID, String name, String description, Date startTime, Date endTime, IVenue venue,
                      List<IArtist> artists, Boolean cancelled, List<Integer> artistIDs, IParentEvent parentEvent) {
        childEventID = ID;
        childEventName = name;
        childEventDescription = description;
        startDateTime = startTime;
        endDateTime = endTime;
        this.venue = venue;
        this.artists = artists;
        this.cancelled = cancelled;
        table = DatabaseTable.CHILD_EVENT;
        this.artistIDs = artistIDs;
        this.parentEvent = parentEvent;
        this.artists = new LinkedList<>();
    }
    
    public ChildEvent(String name, String description, Date startTime, Date endTime, IVenue venue, List<IArtist> artists, IParentEvent parentEvent) {
        childEventID = 0;
        if (Validator.nameValidator(name)) {
            if (Validator.descriptionValidator(description)) {
                this.childEventName = name;
                this.childEventDescription = description;
                this.startDateTime = startTime;
                this.endDateTime = endTime;
                this.venue = venue;
                this.artists = artists;
                this.cancelled = false;
                this.table = DatabaseTable.CHILD_EVENT;
                this.artistIDs = new LinkedList<>();
                this.artists = new LinkedList<>();
                this.parentEvent = parentEvent;
            } else {
                throw new IllegalArgumentException("Invalid description");
            }
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public ChildEvent() {
    
        table = DatabaseTable.CHILD_EVENT;

    }
    
    @Override
    public Integer getChildEventID() {
        return childEventID;
    }

    @Override
    public String getChildEventName() {
        return childEventName;
    }

    @Override
    public String getChildEventDescription() {
        return childEventDescription;
    }

    @Override
    public Date getChildEventStartDateTime() {
        return (Date) startDateTime.clone();
    }

    @Override
    public Date getChildEventEndDateTime() {
        return (Date) endDateTime.clone();
    }

    @Override
    public Boolean getChildEventCancelled() {
        return cancelled;
    }

    @Override
    public Boolean setChildEventName(String name) {
        if (name == null) {
            throw new NullPointerException("Null name!");
        } else if (Validator.nameValidator(name)) {
            childEventName = name;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
        return childEventName.equals(name);
    }

    @Override
    public Boolean setChildEventDescription(String description) {
        if (description == null) {
            throw new NullPointerException("Null description");
        } else if (Validator.descriptionValidator(description)) {
            childEventDescription = description;
        } else {
            throw new IllegalArgumentException("Invalid description");
        }
        return childEventDescription.equals(description);
    }

    @Override
    public Boolean setChildEventStartDateTime(Date startDateTime) {
        if (startDateTime == null) {
            throw new NullPointerException("start time is null");
        } else {
            this.startDateTime = startDateTime;
        }
        return this.startDateTime.equals(startDateTime);
    }

    @Override
    public Boolean setChildEventEndDateTime(Date endDateTime) {
        if (endDateTime == null) {
            throw new NullPointerException("end time is null");
        } else {
            this.endDateTime = endDateTime;
        }
        return this.endDateTime.equals(endDateTime);
    }

    @Override
    public Boolean setChildEventCancelled(Boolean cancelled) {
        if (cancelled == null) {
            throw new NullPointerException("Cannot set 'cancelled' flag to null");
        } else {
            this.cancelled = cancelled;
        }
        return this.cancelled.equals(cancelled);
    }


    @Override
    public List<IArtist> getArtistList() {
        if (artists == null) {
            throw new NullPointerException("Null lineup. cannot get artist list");
        } else {
            return new LinkedList<>(artists);
        }
    }

    @Override
    public Boolean addArtist(IArtist artist) {
        
        if (artists == null) {
            artists = new LinkedList<>();
        }
        if (artist == null) {
            throw new NullPointerException("Cannot add null to the artist list");
        } else {
            if (artists.contains(artist)) {
                throw new IllegalArgumentException("Artist already in contract!");
            } else {
                artists.add(artist);
            }
        }
        return artists.contains(artist);
    }

    @Override
    public List<Integer> getArtistIDs() {
        return new LinkedList<>(artistIDs);
    }

    @Override
    public Boolean removeArtistIDs(Integer artistID) {
        return artistIDs.remove(artistID);
    }

    @Override
    public Boolean addArtistID(Integer artistID) {
        return artistIDs.add(artistID);
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        this.setSocialMedia(socialMedia);
    }

    @Override
    public Boolean removeArtist(IArtist artist) {
        if (artists == null) {
            artists = new LinkedList<>();
            throw new NullPointerException("No Artists in the lineup");
        } else {
            if (artist == null) {
                throw new NullPointerException("cannot remove a null artist");
            } else {
                if (!artists.contains(artist)) {
                    throw new IllegalArgumentException("Artist list doesn't contain artist");
                } else {
                    artists.remove(artist);
                    return !artists.contains(artist);
                }
            }
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
            throw new NullPointerException("Cannot register a null observer");
        } else {
            if (observers == null) {
                observers = new LinkedList();
                observers.add(o);
            } else {
                if (observers.contains(o)) {
                    throw new IllegalArgumentException("Observer already registered");
                } else {
                    observers.add(o);
                }
            }
            return observers.contains(o);
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Cannot remove a null observer");
        } else {
            if (observers == null) {
                observers = new LinkedList();
                throw new IllegalArgumentException("Observer list is empty. Doesn't contain any objects.");
            } else {
                if (!observers.contains(o)) {
                    throw new IllegalArgumentException("Observer isn't already registered");
                } else {
                    observers.remove(o);
                }
            }
            return !observers.contains(o);
        }
    }

    @Override
    public Boolean setVenue(IVenue venue) {
        if (venue == null) {
            throw new NullPointerException("Cannot set venue to null");
        } else {
            this.venue = venue;
        } return true;
    }

    @Override
    public IVenue getVenue() {
        if (venue == null) {
            throw new NullPointerException("The child event's venue hasn't been initialised (still null).");
        } else {
            return venue;
        }
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
