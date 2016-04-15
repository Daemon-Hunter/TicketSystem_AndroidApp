package com.example.aneurinc.prcs_app.Datamodel;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IChildEvent extends ILineup{

    public Integer getChildEventID();
    public String  getChildEventName();
    public String  getChildEventDescription();
    public Date getChildEventStartDateTime();
    public Date    getChildEventEndDateTime();
    public Boolean getChildEventCancelled();

    public Boolean setChildEventName(String name);
    public Boolean setChildEventDescription(String description);
    public Boolean setChildEventStartDateTime(Date startDateTime);
    public Boolean setChildEventEndDateTime(Date endDateTime);
    public Boolean setChildEventCancelled(Boolean cancelled);

    public Boolean setVenue(IVenue venue);
    public IVenue  getVenue();

    public ILineup getLineup();
    public Boolean setLineup(ILineup lineup);

}
