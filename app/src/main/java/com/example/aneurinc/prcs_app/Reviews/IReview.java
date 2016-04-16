package com.example.aneurinc.prcs_app.Reviews;

import com.example.aneurinc.prcs_app.Utility.Observer.IDbSubject;

import java.util.Date;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IReview extends IDbSubject{

    Integer getReviewBaseID();
    // Boolean setReviewBaseID(Integer reviewBaseID);

    Integer getCustomerID();
    // Boolean setCustomerID(Integer customerID);

    Date getDateTime();
    Boolean setDateTime(Date datetime);

    Integer getRating();
    Boolean setRating(Integer rating);

    String  getBody();
    Boolean SetBody(String body);

    Boolean isVerified();
    Boolean setVerified(Boolean verified);
}
