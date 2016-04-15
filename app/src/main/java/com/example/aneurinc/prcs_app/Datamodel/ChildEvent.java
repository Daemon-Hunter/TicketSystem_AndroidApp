package com.example.aneurinc.prcs_app.Datamodel;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.utilities.Observer.IObserver;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.example.aneurinc.prcs_app.Utility.Validator.descriptionValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.nameValidator;

/**
 * Created by Dominic on 14/04/2016.
 */
public class ChildEvent implements IChildEvent
{    private Integer childEventID;
    private String childEventName, childEventDescription;
    private Date startDateTime, endDateTime;
    private Boolean cancelled;
    private IVenue venue;
    private ILineup lineup;
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
     * @param lineup
     * @param cancelled
     */
    public ChildEvent(Integer ID, String name, String description, Date startTime, Date endTime, IVenue venue, ILineup lineup, Boolean cancelled) {
        childEventID = ID;
        childEventName = name;
        childEventDescription = description;
        startDateTime = startTime;
        endDateTime = endTime;
        this.venue = venue;
        this.lineup = lineup;
        this.cancelled = cancelled;
        table = DatabaseTable.CHILDEVENT;
    }

    public ChildEvent(String name, String description, Date startTime, Date endTime, IVenue venue, ILineup lineup) {
        childEventID = 0;
        if (nameValidator(name)) {
            if (descriptionValidator(description)) {
                childEventName = name;
                childEventDescription = description;
                startDateTime = startTime;
                endDateTime = endTime;
                this.venue = venue;
                this.lineup = lineup;
                cancelled = false;
                table = DatabaseTable.CHILDEVENT;
            } else {
                throw new IllegalArgumentException("Invalid description");
            }
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public ChildEvent() {

        table = DatabaseTable.CHILDEVENT;

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
        } else if (nameValidator(name)) {
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
        } else if (descriptionValidator(description)) {
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
    public Integer getLineupID() {
        if (lineup == null) {
            throw new NullPointerException("Null lineup. cannot get ID");
        } else {
            return lineup.getLineupID();
        }
    }

    @Override
    public List<IArtist> getArtistList() {
        if (lineup == null) {
            throw new NullPointerException("Null lineup. cannot get artist list");
        } else {
            return lineup.getArtistList();
        }
    }

    @Override
    public Boolean addArtist(IArtist artist) {

        if (lineup == null) {
            lineup = new Lineup();
        }
        if (artist == null) {
            throw new NullPointerException("Cannot add null to the artist list");
        } else {
            if (lineup.getArtistList().contains(artist)) {
                throw new IllegalArgumentException("Artist already in lineup!");
            } else {
                lineup.addArtist(artist);
            }
        }
        return lineup.getArtistList().contains(artist);
    }

    @Override
    public Boolean removeArtist(IArtist artist) {
        if (lineup == null) {
            lineup = new Lineup();
            throw new NullPointerException("No Artists in the lineup");
        } else {
            if (artist == null) {
                throw new NullPointerException("cannot remove a null artist");
            } else {
                if (!lineup.getArtistList().contains(artist)) {
                    throw new IllegalArgumentException("Artist list doesn't contain artist");
                } else {
                    lineup.removeArtist(artist);
                    return !lineup.getArtistList().contains(artist);
                }
            }
        }
    }

    @Override
    public IArtist getArtist(Integer artistID) {
        if (artistID == null) {
            throw new NullPointerException("Null artist ID. Can't get from artist list.");
        } else {
            if (lineup == null) {
                lineup = new Lineup();
                throw new IllegalArgumentException("Artist list is empty");
            } else {
                for(IArtist a : lineup.getArtistList()) {
                    if (a.getArtistID().equals(artistID)) {
                        return a;
                    }
                }
                throw new IllegalArgumentException("Artist not in list.");
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
        } return this.venue == venue;
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
    public ILineup getLineup() {
        if (lineup == null) {
            lineup = new Lineup();
        }
        return lineup;
    }

    @Override
    public Boolean setLineup(ILineup lineup) {
        if (lineup == null) {
            throw new NullPointerException("Cannot set lineup to null");
        } else {
            this.lineup = lineup;
        }
        return this.lineup == lineup;
    }
}


