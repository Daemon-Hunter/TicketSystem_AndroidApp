package com.example.aneurinc.prcs_app.Reviews;

/**
 * Created by Dominic on 14/04/2016.
 */
public class ArtistReviewFactory implements IReviewFactory{

    @Override
    public IReview createReview(Integer reviewBaseID, Integer customerID,
                                Integer rating, String body)
    {
        IReview review = new ArtistReview(reviewBaseID, customerID, rating, body);
        return review;
    }


}
