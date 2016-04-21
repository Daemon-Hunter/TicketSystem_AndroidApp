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

    public List<IParentEvent> getParentEvents();
    public IParentEvent       getParentEvent(Integer id);
    public Boolean            removeParentEvent(IParentEvent pEvent);


    public List<IVenue> getVenues();
    public IVenue       getVenueEvent(Integer id);
    public Boolean      removeVenue(IVenue venue);


    public List<IArtist>  getArtists();
    public IArtist        getArtistEvent(Integer id);
    public Boolean        removeArtist(IArtist artist);
}
