package com.example.aneurinc.prcs_app.Datamodel;

import java.util.List;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IParentEvent {
    public Integer getParentEventID();
    public String getParentEventName();
    public String getParentEventDescription();
    public Boolean setParentEventName(String name);
    public Boolean setParentEventDescription(String description);


    public Boolean addChildEvent(ChildEvent childEvent);
    public ChildEvent getChildEvent(Integer childEventID);
    public Boolean removeChildEvent(ChildEvent childEvent);
    public List<ChildEvent> getChildEvents();
}
