package com.example.aneurinc.prcs_app.Reviews;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IReviewFactory {

    public IReview createReview(Integer reviewBaseID, Integer customerID,
                                Integer rating, String body);
}
