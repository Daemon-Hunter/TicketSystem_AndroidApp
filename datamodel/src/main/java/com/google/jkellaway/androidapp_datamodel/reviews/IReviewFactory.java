/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.util.Date;

/**
 *
 * @author 10512691
 */
public interface IReviewFactory {
    
    public IReview createReview(Integer reviewBaseID, Integer customerID,
                                Integer rating, Date date, String body, Boolean verified);
}