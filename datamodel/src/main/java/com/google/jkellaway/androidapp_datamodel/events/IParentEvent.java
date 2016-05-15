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
 * The interface Parent event.
 *
 * @author 10467841
 */
public interface IParentEvent extends ISocial, IReviewable {
    /**
     * Gets id.
     *
     * @return the id
     */
    Integer getID();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setName(String name) throws IllegalArgumentException;

    /**
     * Sets description.
     *
     * @param description the description
     * @return the description
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setDescription(String description) throws IllegalArgumentException;

    /**
     * Sets social media.
     *
     * @param socialMedia the social media
     */
    void setSocialMedia(SocialMedia socialMedia);

    /**
     * Gets social media.
     *
     * @return the social media
     */
    SocialMedia getSocialMedia();

    /**
     * Add child event boolean.
     *
     * @param childEvent the child event
     * @return the boolean
     */
    Boolean addChildEvent(IChildEvent childEvent);

    /**
     * Gets child event.
     *
     * @param childEventID the child event id
     * @return the child event
     * @throws IOException the io exception
     */
    IChildEvent getChildEvent(Integer childEventID) throws IOException;

    /**
     * Remove child event boolean.
     *
     * @param childEvent the child event
     * @return the boolean
     */
    Boolean removeChildEvent(IChildEvent childEvent);

    /**
     * Gets child events.
     *
     * @return the child events
     * @throws IOException the io exception
     */
    List<IChildEvent> getChildEvents() throws IOException;
}
