package com.example.aneurinc.prcs_app.Reviews;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
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
        table = DatabaseTable.VENUEREVIEW;
    }

    /**
     * Use this constructor when creating a new review object.
     * ID unknown.
     * @param baseID
     * @param customerID
     * @param rating
     * @param body
     */
    public VenueReview(Integer baseID, Integer customerID, Integer rating, String body)
    {
        super(baseID, customerID, rating, body);
        table = DatabaseTable.VENUEREVIEW;
        this.baseID = baseID;
    }
}
