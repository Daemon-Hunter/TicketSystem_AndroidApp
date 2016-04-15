package com.example.aneurinc.prcs_app.Reviews;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
 */
public class ArtistReview extends Review { /**
 * Use this constructor when creating a review from the database.
 * Validity is known.
 * @param baseID
 * @param customerID
 * @param rating
 * @param date
 * @param body
 * @param verified
 */
public ArtistReview(Integer baseID, Integer customerID, Integer rating, Date date, String body,
                    Boolean verified)
{
    super(baseID, customerID, rating, date, body, verified);
    table = DatabaseTable.ARTIST_REVIEW;
}

    /**
     * Use this constructor when creating a new review object.
     * Verified is automatically set to false.
     * @param baseID
     * @param customerID
     * @param rating
     * @param body
     */
    public ArtistReview(Integer baseID, Integer customerID, Integer rating, String body)
    {
        super(baseID, customerID, rating, body);
        table = DatabaseTable.ARTIST_REVIEW;
    }
}
