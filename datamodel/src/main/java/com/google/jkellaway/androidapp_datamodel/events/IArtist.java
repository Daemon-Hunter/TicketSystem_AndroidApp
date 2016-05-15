/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import java.io.IOException;
import java.util.List;

/**
 * The interface Artist.
 *
 * @author 10467841
 */
public interface IArtist extends ISocial {

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
     * Sets name.
     *
     * @param name the name
     * @return the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setName(String name) throws IllegalArgumentException;

    /**
     * Gets tags.
     *
     * @return the tags
     */
    List<String> getTags();

    /**
     * Add tag boolean.
     *
     * @param tag the tag
     * @return the boolean
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean addTag(String tag) throws IllegalArgumentException;

    /**
     * Remove tag boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    Boolean removeTag(String tag);

    /**
     * Gets description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Sets description.
     *
     * @param decription the decription
     * @return the description
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setDescription(String decription) throws IllegalArgumentException;

    /**
     * Gets type.
     *
     * @return the type
     * @throws IOException the io exception
     */
    String getType() throws IOException;

    /**
     * Sets type.
     *
     * @param type the type
     * @return the type
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setType(String type) throws IllegalArgumentException;

    /**
     * Gets type id.
     *
     * @return the type id
     */
    Integer getTypeID();

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
     * Gets child events.
     *
     * @return the child events
     * @throws IOException the io exception
     */
    List<IChildEvent> getChildEvents() throws IOException;

    /**
     * Gets child event.
     *
     * @param childEventID the child event id
     * @return the child event
     * @throws IOException the io exception
     */
    IChildEvent getChildEvent(Integer childEventID) throws IOException;

    /**
     * New contract boolean.
     *
     * @param childEvent the child event
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean newContract(IChildEvent childEvent) throws IOException;
}
