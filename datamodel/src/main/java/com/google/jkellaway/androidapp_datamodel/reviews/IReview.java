/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author 10512691
 */
public interface IReview {
    
    Integer getReviewBaseID();
    // Boolean setReviewBaseID(Integer reviewBaseID);
    
    Integer getCustomerID();
    // Boolean setCustomerID(Integer customerID);
    
    Date    getDateTime();
    Boolean setDateTime(Date datetime);
    
    Integer getRating();
    Boolean setRating(Integer rating) throws IOException;
    
    String  getBody();
    Boolean SetBody(String body) throws IOException;
    
    Boolean isVerified();
    Boolean setVerified(Boolean verified);
}
