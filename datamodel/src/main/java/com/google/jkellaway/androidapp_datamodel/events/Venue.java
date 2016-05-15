/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import android.graphics.Bitmap;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.reviews.IReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.VenueReviewFactory;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.addressValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.capacityValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.descriptionValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.emailValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.facilitiesValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.nameValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.parkingSpaceValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.phoneNumberValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.postcodeValidator;

/**
 * The type Venue.
 *
 * @author 10512691
 */
public class Venue implements IVenue {

    private IReviewFactory reviewFactory;
    private List<IReview> reviews;
    private List<IChildEvent> childEvents;
    private List<IObserver> observers;
    private Integer socialMediaID;
    private SocialMedia socialMedia;
    private String description;
    private DatabaseTable table;
    private int ID;
    private String name;
    private String phoneNumber, email, address, city, postcode;
    private Integer capacitySeating, capacityStanding, parkingSpaces;
    private Boolean disabledAccess;
    private String facilities;

    /**
     * Instantiates a new Venue.
     */
    public Venue() {
        reviewFactory = new VenueReviewFactory();
    }

    /**
     * For use when creating new Venues on the Administrators application.
     * No ID is given, it will be assigned from a returning call to the API.
     * in the setID() method.
     *
     * @param social      the social
     * @param description the description
     * @param capSeating  the cap seating
     * @param capStanding the cap standing
     * @param access      the access
     * @param facilities  the facilities
     * @param parking     the parking
     * @param phoneNo     the phone no
     * @param email       the email
     * @param address     the address
     * @param city        the city
     * @param postcode    the postcode
     * @param name        the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Venue(SocialMedia social, String description, Integer capSeating, Integer capStanding, Boolean access, String facilities, Integer parking, String phoneNo, String email, String address, String city, String postcode, String name) throws IllegalArgumentException {

        nameValidator(name);
        descriptionValidator(description);
        capacityValidator(capSeating);
        capacityValidator(capStanding);
        facilitiesValidator(facilities);
        parkingSpaceValidator(parking);
        phoneNumberValidator(phoneNo);
        emailValidator(email);
        addressValidator(address);
        nameValidator(city);
        postcodeValidator(postcode);

        this.name = name;
        this.description = description;
        this.socialMedia = social;
        this.table = DatabaseTable.VENUE;

        // Initialize all other variables with method arguments
        this.capacitySeating = capSeating;
        this.capacityStanding = capStanding;
        this.disabledAccess = access;
        this.facilities = facilities;
        this.parkingSpaces = parking;
        this.phoneNumber = phoneNo;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.reviewFactory = new VenueReviewFactory();
    }

    /**
     * For use when creating venues that already exist in the database.
     * ID is given and assigned.
     *
     * @param id            the id
     * @param socialMediaID the social media id
     * @param description   the description
     * @param capSeating    the cap seating
     * @param capStanding   the cap standing
     * @param access        the access
     * @param facilities    the facilities
     * @param parking       the parking
     * @param phoneNo       the phone no
     * @param email         the email
     * @param address       the address
     * @param city          the city
     * @param postcode      the postcode
     * @param name          the name
     */
    public Venue(Integer id, Integer socialMediaID, String description, Integer capSeating, Integer capStanding, Boolean access, String facilities, Integer parking, String phoneNo, String email, String address, String city, String postcode, String name) {
        this.ID = id;
        this.name = name;
        this.description = description;
        this.socialMedia = new SocialMedia();
        this.reviews = new LinkedList<>();
        this.table = DatabaseTable.VENUE;

        // Initialize all other variables with method arguments
        capacitySeating = capSeating;
        capacityStanding = capStanding;
        disabledAccess = access;
        this.facilities = facilities;
        parkingSpaces = parking;
        phoneNumber = phoneNo;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        reviewFactory = new VenueReviewFactory();
        this.socialMediaID = socialMediaID;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public String getName() {
        if (name == null) {
            throw new NullPointerException("Null venue name");
        } else {
            return name;
        }
    }

    @Override
    public String getDescription() {
        if (description == null) {
            throw new NullPointerException("Null venue description");
        } else {
            return description;
        }
    }

    @Override
    public Integer getStandingCapacity() {
        if (capacityStanding == null) {
            throw new NullPointerException("Null standing capacity");
        } else {
            return capacityStanding;
        }
    }

    @Override
    public Integer getSeatingCapacity() {
        if (capacitySeating == null) {
            throw new NullPointerException("Null seating capacity");
        } else {
            return capacitySeating;
        }
    }

    @Override
    public Boolean getDisabledAccess() {
        if (disabledAccess == null) {
            throw new NullPointerException("Null disabled access variable");
        } else {
            return disabledAccess;
        }
    }

    @Override
    public String getFacilites() {
        if (facilities == null) {
            throw new NullPointerException("Null facililites variable");
        } else {
            return facilities;
        }
    }

    @Override
    public Integer getParking() {
        if (parkingSpaces == null) {
            throw new NullPointerException("Null parking spaces variable");
        } else {
            return parkingSpaces;
        }
    }

    @Override
    public String getEmail() {
        if (email == null) {
            throw new NullPointerException("Null email");
        } else {
            return email;
        }
    }

    @Override
    public String getPhoneNumber() {
        if (phoneNumber == null) {
            throw new NullPointerException("Null phone number");
        } else {
            return phoneNumber;
        }
    }

    @Override
    public String getAddress() {
        if (address == null) {
            throw new NullPointerException("Null address");
        } else {
            return address;
        }
    }

    @Override
    public String getCity() {
        if (city == null) {
            throw new NullPointerException("Null city");
        } else {
            return city;
        }
    }

    @Override
    public String getPostcode() {
        if (postcode == null) {
            throw new NullPointerException("Null postcode");
        } else {
            return postcode;
        }
    }

    @Override
    public List<IChildEvent> getChildEvents() throws IOException {
        this.childEvents = (List<IChildEvent>) (Object) APIHandle.getObjectsFromObject(this.ID, DatabaseTable.CHILD_EVENT, DatabaseTable.VENUE);
        return this.childEvents;
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMediaID = socialMedia.getSocialId();
        this.socialMedia = socialMedia;
    }

    @Override
    public SocialMedia getSocialMedia() {
        return this.socialMedia;
    }

    @Override
    public Boolean setName(String name) throws IllegalArgumentException {
        if (name == null)
            throw new IllegalArgumentException("Enter a name");
        nameValidator(name);
        this.name = name;
        return this.name.equals(name);

    }

    @Override
    public Boolean setDescription(String description) throws IllegalArgumentException {
        if (description == null)
            throw new IllegalArgumentException("Enter a description");
        descriptionValidator(description);
        this.description = description;
        return this.description.equals(description);
    }

    @Override
    public Boolean setStandingCapacity(Integer standing) throws IllegalArgumentException {
        if (standing == null)
            throw new IllegalArgumentException("Enter a capacity.");
        capacityValidator(standing);
        this.capacityStanding = standing;
        return this.capacityStanding.equals(capacityStanding);
    }

    @Override
    public Boolean setSeatingCapacity(Integer seating) throws IllegalArgumentException {
        if (seating == null)
            throw new IllegalArgumentException("Enter a capacity.");
        capacityValidator(seating);
        this.capacitySeating = seating;
        return this.capacitySeating.equals(capacitySeating);
    }

    @Override
    public Boolean setDisabledAccess(Boolean access) throws IllegalArgumentException {
        if (access == null) {
            throw new IllegalArgumentException("Enter a Value.");
        } else {
            this.disabledAccess = access;
            return true;
        }
    }

    @Override
    public Boolean setFacilites(String facilities) throws IllegalArgumentException {
        if (facilities == null)
            throw new IllegalArgumentException("Enter something interesting about the facilities.");
        facilitiesValidator(facilities);
        this.facilities = facilities;
        return this.facilities.equals(facilities);
    }

    @Override
    public Boolean setParking(Integer parking) throws IllegalArgumentException {
        if (parking == null)
            parking = 0;
        parkingSpaceValidator(parking);
        this.parkingSpaces = parking;
        return this.parkingSpaces == parking;
    }

    @Override
    public Boolean setEmail(String email) throws IllegalArgumentException {
        if (email == null)
            throw new IllegalArgumentException("Enter an email address.");
        emailValidator(email);
        this.email = email;
        return this.email.equals(email);
    }

    @Override
    public Boolean setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (phoneNumber == null)
            throw new IllegalArgumentException("Enter a phone number.");
        phoneNumberValidator(phoneNumber);
        this.phoneNumber = phoneNumber;
        return this.phoneNumber.equals(phoneNumber);
    }

    @Override
    public Boolean setAddress(String address) throws IllegalArgumentException {
        if (address == null)
            throw new IllegalArgumentException("Enter an address.");
        addressValidator(address);
        this.address = address;
        return this.address.equals(address);
    }

    @Override
    public Boolean setCity(String city) throws IllegalArgumentException {
        if (city == null)
            throw new IllegalArgumentException("Enter a city.");
        addressValidator(city);
        this.city = city;
        return this.city.equals(city);
    }

    @Override
    public Boolean setPostcode(String postcode)  throws IllegalArgumentException {
        if (postcode == null)
            throw new IllegalArgumentException("Enter a postcode.");
        this.postcode = postcode;
        return this.postcode.equals(postcode);
    }
    @Override
    public Integer getSocialId() {
        return socialMediaID;
    }

    /**
     * Checks the validity of the ID before assigning.
     *
     * @param id
     * @return Boolean true if ID set.
     */

    @Override
    public Boolean setSocialId(Integer id) {
        socialMediaID = id;
        return socialMedia.setSocialId(id);
    }


    /**
     * Gets review factory.
     *
     * @return the review factory
     */
    protected IReviewFactory getReviewFactory() {
        return reviewFactory;
    }

    @Override
    public IReview createReview(Integer customerID, Integer rating, String body, Date date, Boolean verified) {
        return reviewFactory.createReview(ID, customerID, rating, date, body, verified);
    }

    @Override
    public IReview getReview(Integer customerID) throws IllegalArgumentException {
        if (customerID == null)
            throw new NullPointerException();
        for (IReview r : reviews) {
            if (r.getCustomerID().equals(customerID)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No customers with that ID have " + "written a review for this venue.");

    }

    @Override
    public List<IReview> getReviews() {
        if (reviews == null) {
            throw new NullPointerException();
        } else {
            return reviews;
        }
    }

    @Override
    public Boolean deleteReview(IReview review) throws IOException {
        if (review == null) {
            throw new NullPointerException("Review to be deleted was null");
        } else if (!reviews.contains(review)) {
            throw new IllegalArgumentException("Review to be deleted wasn't in list");
        } else {
            reviews.remove(review);
            return true;
        }
    }

    /**
     * Gets table.
     *
     * @return the table
     */
    public DatabaseTable getTable() {
        if (table == null) {
            throw new NullPointerException();
        } else {
            return table;
        }
    }

    @Override
    public List<Bitmap> getImages() {
        return socialMedia.getImages();
    }

    @Override
    public Bitmap getImage(int index) {
        return socialMedia.getImage(index);
    }

    @Override
    public Boolean addImage(Bitmap img) {
        return socialMedia.addImage(img);
    }

    @Override
    public Boolean removeImage(int index) {
        return socialMedia.removeImage(index);
    }

    @Override
    public Boolean setImages(List<Bitmap> images) {
        return socialMedia.setImages(images);
    }


    @Override
    public String getFacebook() {
        return socialMedia.getFacebook();
    }

    @Override
    public Boolean setFacebook(String fb) throws IllegalArgumentException {
        return socialMedia.setFacebook(fb);
    }

    @Override
    public String getTwitter() {
        return socialMedia.getTwitter();
    }

    @Override
    public Boolean setTwitter(String tw) throws IllegalArgumentException {
        return socialMedia.setTwitter(tw);
    }

    @Override
    public String getInstagram() {
        return socialMedia.getInstagram();
    }

    @Override
    public Boolean setInstagram(String insta) throws IllegalArgumentException {
        return socialMedia.setInstagram(insta);
    }

    @Override
    public String getSoundcloud() {
        return socialMedia.getSoundcloud();
    }

    @Override
    public Boolean setSoundcloud(String sc) throws IllegalArgumentException {
        return socialMedia.setSoundcloud(sc);
    }

    @Override
    public String getWebsite() {
        return socialMedia.getWebsite();
    }

    @Override
    public Boolean setWebsite(String web) throws IllegalArgumentException {
        return socialMedia.setWebsite(web);
    }

    @Override
    public String getSpotify() {
        return socialMedia.getSpotify();
    }

    @Override
    public Boolean setSpotify(String sp) throws IllegalArgumentException {
        return socialMedia.setSpotify(sp);
    }
}
