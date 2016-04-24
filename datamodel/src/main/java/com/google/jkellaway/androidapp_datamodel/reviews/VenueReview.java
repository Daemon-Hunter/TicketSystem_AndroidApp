/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import java.util.Date;

/**
 *
 * @author 10512691
 */
public class VenueReview extends Review {

    private Integer baseID;
    
    /**
     * Use this constructor when creating a review from the database.
     * ID known.
     * @param baseID
     * @param customerID
     * @param rating
     * @param date
     * @param body
     * @param verified 
     */
    public VenueReview(Integer baseID, Integer customerID, Integer rating, 
            Date date, String body, Boolean verified) 
    {
        super(baseID, customerID, rating, date, body, verified);
        table = DatabaseTable.VENUE_REVIEW;
    }
}