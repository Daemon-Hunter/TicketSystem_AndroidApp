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

    Boolean setName(String name) throws IOException;
    Boolean setDescription(String description) throws IOException;
    Boolean setStandingCapacity(Integer standing) throws IOException;
    Boolean setSeatingCapacity(Integer seating) throws IOException;
    Boolean setDisabledAccess(Boolean access) throws IOException;
    Boolean setFacilites(String facilities) throws IOException;
    Boolean setParking(Integer parking) throws IOException;
    Boolean setEmail(String email) throws IOException;
    Boolean setPhoneNumber(String phoneNumber) throws IOException;
    Boolean setAddress(String address) throws IOException;
    Boolean setCity(String city);
    Boolean setPostcode(String postcode) throws IOException;
}
