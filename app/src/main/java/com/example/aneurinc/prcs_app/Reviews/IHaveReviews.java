package com.example.aneurinc.prcs_app.Reviews;

import com.example.aneurinc.prcs_app.utilities.Observer.IDbSubject;

import java.util.LinkedList;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IHaveReviews extends IDbSubject{

    public LinkedList<IReview> getReviews();
    public IReview getReview(Integer uniqueID);
    public Boolean deleteReview(IReview review);

}
