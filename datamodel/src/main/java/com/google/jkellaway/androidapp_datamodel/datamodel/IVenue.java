/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.datamodel;

import com.google.jkellaway.androidapp_datamodel.reviews.IReviewable;

/**
 *
 * @author 10467841
 */
public interface IVenue extends ISocial, IReviewable {
    
    public Integer getVenueID();
    public String getVenueName();
    public String getVenueDescription();
    public Integer getVenueStandingCapacity();
    public Integer getVenueSeatingCapacity();
    public Boolean getVenueDisabledAccess();
    public String getVenueFacilites();
    public Integer getVenueParking();
    public String getVenueEmail();
    public String getVenuePhoneNumber();
    public String getVenueAddress();
    public String getVenuePostcode();

    public void setSocialMedia(SocialMedia socialMedia);

    public Boolean setVenueName(String name);
    public Boolean setVenueDescription(String description);
    public Boolean setVenueStandingCapacity(Integer standing);
    public Boolean setVenueSeatingCapacity(Integer seating);
    public Boolean setVenueDisabledAccess(Boolean access);
    public Boolean setVenueFacilites(String facilities);
    public Boolean setVenueParking(Integer parking);
    public Boolean setVenueEmail(String email);
    public Boolean setVenuePhoneNumber(String phoneNumber);
    public Boolean setVenueAddress(String address);
    public Boolean setVenuePostcode(String postcode);
}
