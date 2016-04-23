/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import com.google.jkellaway.androidapp_datamodel.reviews.IReviewable;

import java.util.List;


/**
 *
 * @author 10467841
 */
public interface IParentEvent extends ISocial, IReviewable {
    public Integer getID();
    public String getName();
    public String getDescription();
    public Boolean setName(String name);
    public Boolean setDescription(String description);

    public void setSocialMedia(SocialMedia socialMedia);
    
    public Boolean addChildEvent(IChildEvent childEvent);
    public Boolean addChildEventList(List<IChildEvent> childEvents);
    public IChildEvent getChildEvent(Integer childEventID);
    public Boolean removeChildEvent(IChildEvent childEvent);
    public List<IChildEvent> getChildEvents();
}
