/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.io.IOException;
import java.util.Date;

/**
 * The interface Review.
 *
 * @author 10512691
 */
public interface IReview {

    /**
     * Gets review base id.
     *
     * @return the review base id
     */
    Integer getReviewBaseID();
    // Boolean setReviewBaseID(Integer reviewBaseID);

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    Integer getCustomerID();
    // Boolean setCustomerID(Integer customerID);

    /**
     * Gets date time.
     *
     * @return the date time
     */
    Date getDateTime();

    /**
     * Sets date time.
     *
     * @param datetime the datetime
     * @return the date time
     */
    Boolean setDateTime(Date datetime);

    /**
     * Gets rating.
     *
     * @return the rating
     */
    Integer getRating();

    /**
     * Sets rating.
     *
     * @param rating the rating
     * @return the rating
     * @throws IOException the io exception
     */
    Boolean setRating(Integer rating) throws IOException;

    /**
     * Gets body.
     *
     * @return the body
     */
    String getBody();

    /**
     * Set body boolean.
     *
     * @param body the body
     * @return the boolean
     * @throws IOException the io exception
     */
    Boolean SetBody(String body) throws IOException;

    /**
     * Is verified boolean.
     *
     * @return the boolean
     */
    Boolean isVerified();

    /**
     * Sets verified.
     *
     * @param verified the verified
     * @return the verified
     */
    Boolean setVerified(Boolean verified);
}
