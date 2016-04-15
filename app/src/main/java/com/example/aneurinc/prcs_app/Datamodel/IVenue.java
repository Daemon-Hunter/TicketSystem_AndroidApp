package com.example.aneurinc.prcs_app.Datamodel;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IVenue {
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
