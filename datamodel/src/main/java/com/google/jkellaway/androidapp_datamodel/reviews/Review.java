/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * The type Review.
 *
 * @author 10512691
 */
public abstract class Review implements IReview {
    /**
     * The Review base id.
     */
    protected Integer reviewBaseID;
    /**
     * The Customer id.
     */
    protected Integer customerID;
    /**
     * The Date time.
     */
    protected Date dateTime;
    /**
     * The Rating.
     */
    protected Integer rating;
    /**
     * The Review body.
     */
    protected String reviewBody;
    /**
     * The Verified.
     */
    protected Boolean verified;
    /**
     * The Table.
     */
    protected DatabaseTable table;

    /**
     * Use this constructor when creating a review object from the database.
     * Validity is is known.
     *
     * @param baseID     the base id
     * @param customerID the customer id
     * @param rating     the rating
     * @param date       the date
     * @param body       the body
     * @param verified   the verified
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Review(Integer baseID, Integer customerID, Integer rating, Date date, String body, Boolean verified) throws IllegalArgumentException {
        // Check to see if any of the arguments were null
        if (baseID == null || customerID == null) {
            throw new IllegalArgumentException("Sorry, we've had an internal error :( please try again.");
        } else if (rating == null || body == null) {
            throw new IllegalArgumentException("Review rating, or body, hasn't been set - please try again.");
        } else if (verified == null) {
            throw new IllegalArgumentException("Sorry, we've had an internal error whilst checking review verification :( please try again.");
        } else {
            Validator.ratingValidator(rating);
            Validator.reviewBodyValidator(body);

            // Everything is valid -> initialise variables
            reviewBaseID = baseID;
            this.customerID = customerID;
            this.rating = rating;
            dateTime = date;
            reviewBody = body;
            this.verified = verified;
        }
    }

    /**
     * Use this constructor when creating a new review object.
     * Verified is automatically set to false.
     *
     * @param baseID     the base id
     * @param customerID the customer id
     * @param rating     the rating
     * @param body       the body
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Review(Integer baseID, Integer customerID, Integer rating, String body) throws IllegalArgumentException {
        // Check to see if any of the arguments were null
        if (baseID == null || customerID == null) {
            throw new IllegalArgumentException("Sorry, we've had an internal error :( please try again.");
        } else if (rating == null || body == null) {
            throw new IllegalArgumentException("Review rating, or body, hasn't been set - please try again.");
        } else {

            // Given no arguments were null, see if they're valid
            Validator.ratingValidator(rating);
            Validator.reviewBodyValidator(body);

            // Everything is valid -> initialise variables
            reviewBaseID = baseID;
            this.customerID = customerID;
            this.rating = rating;
            dateTime = Calendar.getInstance().getTime();
            reviewBody = body;
            this.verified = false;
        }
    }

    /**
     * Gets table.
     *
     * @return the table
     */
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public Integer getReviewBaseID() {
        if (reviewBaseID == null) {
            throw new NullPointerException("Null ID");
        } else {
            return reviewBaseID;
        }
    }

    @Override
    public Integer getCustomerID() {
        if (customerID == null) {
            throw new NullPointerException("Null customer ID");
        } else {
            return customerID;
        }
    }

    @Override
    public Date getDateTime() {
        if (dateTime == null) {
            throw new NullPointerException("Null date / time");
        } else {
            return dateTime;
        }
    }

    @Override
    public Boolean setDateTime(Date datetime) throws IllegalArgumentException {
        if (datetime == null) {
            throw new IllegalArgumentException("Cannot set the date / time to nothing!");
        }
        dateTime = datetime;
        return true;

    }

    @Override
    public Integer getRating() {
        if (rating == null) {
            throw new NullPointerException("Null rating");
        } else {
            return rating;
        }
    }

    @Override
    public Boolean setRating(Integer rating) throws IllegalArgumentException {
        if (rating == null)
            throw new IllegalArgumentException("Cannot set the review's rating to nothing!");

        Validator.ratingValidator(rating);

        this.rating = rating;
        return this.rating.equals(rating);
    }

    @Override
    public String getBody() {
        if (reviewBody == null) {
            throw new NullPointerException("Null review body");
        } else {
            return reviewBody;
        }
    }

    @Override
    public Boolean SetBody(String body) throws IllegalArgumentException {
        if (body == null) throw new NullPointerException("Cannot set review body to null");

        Validator.reviewBodyValidator(body);

        reviewBody = body;
        return reviewBody.equals(body);
    }

    @Override
    public Boolean isVerified() {
        if (verified == null) {
            throw new NullPointerException("Verified equals null");
        } else {
            return verified;
        }
    }

    @Override
    public Boolean setVerified(Boolean verified) {
        if (verified == null) {
            throw new NullPointerException("Cannot set verified as null");
        } else {
            this.verified = verified;
        }
        return Objects.equals(this.verified, verified);
    }
}
