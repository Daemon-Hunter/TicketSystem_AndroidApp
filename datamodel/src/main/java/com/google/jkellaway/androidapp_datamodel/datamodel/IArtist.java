/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.datamodel;

import com.google.jkellaway.androidapp_datamodel.reviews.IReviewable;

import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IArtist extends ISocial, IReviewable {
    
    public Integer getArtistID();
    public String getArtistName();
    public void setArtist(String name);
    public List<String> getArtistTags();
    public Boolean addArtistTag(String tag);
    public Boolean removeArtistTag(String tag);
    public String getDescription();
    public void setDescription(String decription);

    public void setSocialMedia(SocialMedia socialMedia);

    public List<IChildEvent> getChildEvents();
    public Boolean removeChildEvent(IChildEvent childEvent);
    public Boolean addChildEvent(IChildEvent childEvent);

    public List<Integer> getChildEventIDs();
    public Boolean removeChildEventID(Integer childEventID);
    public Boolean addChildEventID(Integer childEventID);
}
