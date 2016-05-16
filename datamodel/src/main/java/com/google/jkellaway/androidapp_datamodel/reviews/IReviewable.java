/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.util.Date;

/**
 * The interface Reviewable is implemented by a class which is reviewable.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IReviewable extends IHaveReviews {

    /**
     * Gets review.
     *
     * @param customerID the customer id
     * @param rating     the rating
     * @param body       the body
     * @param date       the date
     * @param verified   the verified
     * @return the review
     */
// Inside create review method, call getReviewFactory() on 'this' object
    IReview createReview(Integer customerID, Integer rating, String body, Date date, Boolean verified);
}
