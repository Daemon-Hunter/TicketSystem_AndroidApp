/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import com.google.jkellaway.androidapp_datamodel.reviews.IReviewable;

import java.io.IOException;
import java.util.List;


/**
 *
 * @author 10467841
 */
public interface IParentEvent extends ISocial, IReviewable {
    Integer getID();
    String getName();
    String getDescription();
    Boolean setName(String name) throws IllegalArgumentException;
    Boolean setDescription(String description) throws IllegalArgumentException;

    void setSocialMedia(SocialMedia socialMedia);
    SocialMedia getSocialMedia();
    
    Boolean addChildEvent(IChildEvent childEvent);
    IChildEvent getChildEvent(Integer childEventID) throws IOException;
    Boolean removeChildEvent(IChildEvent childEvent);
    List<IChildEvent> getChildEvents() throws IOException;
}
