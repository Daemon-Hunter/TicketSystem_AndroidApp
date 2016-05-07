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
public interface IVenue extends ISocial, IReviewable {
    
    Integer getID();
    String getName();
    String getDescription();
    Integer getStandingCapacity();
    Integer getSeatingCapacity();
    Boolean getDisabledAccess();
    String getFacilites();
    Integer getParking();
    String getEmail();
    String getPhoneNumber();
    String getAddress();
    String getCity();
    String getPostcode();

    List<IChildEvent> getChildEvents() throws IOException;

    void setSocialMedia(SocialMedia socialMedia);
    SocialMedia getSocialMedia();

    Boolean setName(String name)  throws IllegalArgumentException;
    Boolean setDescription(String description)  throws IllegalArgumentException;
    Boolean setStandingCapacity(Integer standing)  throws IllegalArgumentException;
    Boolean setSeatingCapacity(Integer seating)  throws IllegalArgumentException;
    Boolean setDisabledAccess(Boolean access)  throws IllegalArgumentException;
    Boolean setFacilites(String facilities)  throws IllegalArgumentException;
    Boolean setParking(Integer parking)  throws IllegalArgumentException;
    Boolean setEmail(String email)  throws IllegalArgumentException;
    Boolean setPhoneNumber(String phoneNumber)  throws IllegalArgumentException;
    Boolean setAddress(String address)  throws IllegalArgumentException;
    Boolean setCity(String city) throws IllegalArgumentException;
    Boolean setPostcode(String postcode) throws IllegalArgumentException;
}
