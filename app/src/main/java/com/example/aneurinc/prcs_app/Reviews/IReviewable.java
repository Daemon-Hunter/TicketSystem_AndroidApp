package com.example.aneurinc.prcs_app.Reviews;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IReviewable extends IHaveReviews{
    // Inside create review method, call getReviewFactory() on 'this' object
    public IReview createReview(Integer customerID, Integer rating, String body);

}
