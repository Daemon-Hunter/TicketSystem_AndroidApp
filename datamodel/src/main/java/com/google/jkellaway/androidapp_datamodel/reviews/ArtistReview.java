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
public class ArtistReview extends Review {

    /**
     * Use this constructor when creating a review from the database.
     * Validity is known.
     *
     * @param baseID
     * @param customerID
     * @param rating
     * @param date
     * @param body
     * @param verified
     */
    public ArtistReview(Integer baseID, Integer customerID, Integer rating, Date date, String body,
                        Boolean verified) {
        super(baseID, customerID, rating, date, body, verified);
        table = DatabaseTable.ARTIST_REVIEW;
    }
}