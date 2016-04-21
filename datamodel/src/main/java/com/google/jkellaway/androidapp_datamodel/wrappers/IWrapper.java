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

    List<IParentEvent> getParentEvents();
    List<IParentEvent> loadMoreParentEvents();
    IParentEvent       getParentEvent(Integer id);
    Boolean            removeParentEvent(IParentEvent pEvent);
    List<IParentEvent> refreshParentEvents();


    List<IVenue> getVenues();
    IVenue       getVenue(Integer id);
    List<IVenue> loadMoreVenues();
    Boolean      removeVenue(IVenue venue);
    List<IVenue> refreshVenues();


    List<IArtist>  getArtists();
    List<IArtist> loadMoreArtists();
    IArtist        getArtist(Integer id);
    Boolean        removeArtist(IArtist artist);
    List<IArtist> refreshArtists();
}
