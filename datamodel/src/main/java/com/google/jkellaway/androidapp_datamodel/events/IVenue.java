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
    String getPostcode();

    List<IChildEvent> getChildEvents() throws IOException;

    void setSocialMedia(SocialMedia socialMedia);

    Boolean setName(String name);
    Boolean setDescription(String description);
    Boolean setStandingCapacity(Integer standing);
    Boolean setSeatingCapacity(Integer seating);
    Boolean setDisabledAccess(Boolean access);
    Boolean setFacilites(String facilities);
    Boolean setParking(Integer parking);
    Boolean setEmail(String email);
    Boolean setPhoneNumber(String phoneNumber);
    Boolean setAddress(String address);
    Boolean setPostcode(String postcode);
}
