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
 * The interface Venue.
 *
 * @author 10467841
 */
public interface IVenue extends ISocial, IReviewable {

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
     * Gets standing capacity.
     *
     * @return the standing capacity
     */
    Integer getStandingCapacity();

    /**
     * Gets seating capacity.
     *
     * @return the seating capacity
     */
    Integer getSeatingCapacity();

    /**
     * Gets disabled access.
     *
     * @return the disabled access
     */
    Boolean getDisabledAccess();

    /**
     * Gets facilites.
     *
     * @return the facilites
     */
    String getFacilites();

    /**
     * Gets parking.
     *
     * @return the parking
     */
    Integer getParking();

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    String getPhoneNumber();

    /**
     * Gets address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Gets city.
     *
     * @return the city
     */
    String getCity();

    /**
     * Gets postcode.
     *
     * @return the postcode
     */
    String getPostcode();

    /**
     * Gets child events.
     *
     * @return the child events
     * @throws IOException the io exception
     */
    List<IChildEvent> getChildEvents() throws IOException;

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
     * Sets name.
     *
     * @param name the name
     * @return the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setName(String name)  throws IllegalArgumentException;

    /**
     * Sets description.
     *
     * @param description the description
     * @return the description
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setDescription(String description)  throws IllegalArgumentException;

    /**
     * Sets standing capacity.
     *
     * @param standing the standing
     * @return the standing capacity
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setStandingCapacity(Integer standing)  throws IllegalArgumentException;

    /**
     * Sets seating capacity.
     *
     * @param seating the seating
     * @return the seating capacity
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setSeatingCapacity(Integer seating)  throws IllegalArgumentException;

    /**
     * Sets disabled access.
     *
     * @param access the access
     * @return the disabled access
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setDisabledAccess(Boolean access)  throws IllegalArgumentException;

    /**
     * Sets facilites.
     *
     * @param facilities the facilities
     * @return the facilites
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setFacilites(String facilities)  throws IllegalArgumentException;

    /**
     * Sets parking.
     *
     * @param parking the parking
     * @return the parking
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setParking(Integer parking)  throws IllegalArgumentException;

    /**
     * Sets email.
     *
     * @param email the email
     * @return the email
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setEmail(String email)  throws IllegalArgumentException;

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     * @return the phone number
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setPhoneNumber(String phoneNumber)  throws IllegalArgumentException;

    /**
     * Sets address.
     *
     * @param address the address
     * @return the address
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setAddress(String address)  throws IllegalArgumentException;

    /**
     * Sets city.
     *
     * @param city the city
     * @return the city
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setCity(String city) throws IllegalArgumentException;

    /**
     * Sets postcode.
     *
     * @param postcode the postcode
     * @return the postcode
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setPostcode(String postcode) throws IllegalArgumentException;
}
