/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.datamodel.Artist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IWrapper {
    public Boolean            addParentEvent(IParentEvent pEvent);
    public IParentEvent       getParentEvent(Integer index);
    public List<IParentEvent> getParentEvents();
    public Boolean            removeParentEvent(IParentEvent pEvent);
    
    public Boolean      addVenue(IVenue venue);
    public IVenue       getVenue(Integer index);
    public List<IVenue> getVenues();
    public Boolean      removeVenue(IVenue venue);
    
    public Boolean       addArtist(IArtist artist);
    public IArtist        getArtist(Integer index);
    public List<IArtist>  getArtists();
    public Boolean       removeArtist(IArtist artist);
}
