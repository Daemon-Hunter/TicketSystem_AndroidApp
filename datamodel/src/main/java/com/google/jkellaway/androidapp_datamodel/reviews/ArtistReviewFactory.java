/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.util.Date;

/**
 * The type Artist review factory.
 *
 * @author 10512691
 */
public class ArtistReviewFactory implements IReviewFactory {

    @Override
    public IReview createReview(Integer reviewBaseID, Integer customerID,
                                Integer rating, Date date, String body, Boolean verified) throws IllegalArgumentException
    {
        return new ArtistReview(reviewBaseID, customerID, rating, date, body, verified);
    }
    
}
