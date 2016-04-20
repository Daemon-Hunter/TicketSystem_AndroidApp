/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.datamodel;

import com.google.jkellaway.androidapp_datamodel.utilities.observer.IDbSubject;

import java.util.Date;
import java.util.List;

/**
 *
 * @author 10512691
 */
public interface IChildEvent extends IDbSubject, ISocial {
    
    public Integer getChildEventID();
    public String  getChildEventName();
    public String  getChildEventDescription();
    public Date    getChildEventStartDateTime();
    public Date    getChildEventEndDateTime();
    public Boolean getChildEventCancelled();
    
    public Boolean setChildEventName(String name);
    public Boolean setChildEventDescription(String description);
    public Boolean setChildEventStartDateTime(Date startDateTime);
    public Boolean setChildEventEndDateTime(Date endDateTime);
    public Boolean setChildEventCancelled(Boolean cancelled);
    
    public Boolean setVenue(IVenue venue);
    public IVenue  getVenue();
    
    public List<IArtist> getArtistList();
    public Boolean removeArtist(IArtist artist);
    public Boolean addArtist(IArtist artist);

    public List<Integer> getArtistIDs();
    public Boolean removeArtistIDs(Integer artistID);
    public Boolean addArtistID(Integer artistID);

    public void setParentEvent(IParentEvent parentEvent);

    public void setVenueID(Integer venue);

    public void setSocialMedia(SocialMedia socialMedia);
}
