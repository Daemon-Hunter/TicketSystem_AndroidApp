/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.util.Date;

/**
 * The type Venue review factory is used by classes to create an instance of an venue review object.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public class VenueReviewFactory implements IReviewFactory {

    @Override
    public IReview createReview(Integer reviewBaseID, Integer customerID, Integer rating, Date date, String body, Boolean verified) throws IllegalArgumentException {
        return new VenueReview(reviewBaseID, customerID, rating, date, body, verified);
    }
}