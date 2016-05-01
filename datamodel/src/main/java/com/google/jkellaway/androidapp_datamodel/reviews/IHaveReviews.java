/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author 10512691
 */
public interface IHaveReviews {
    
    List<IReview> getReviews();
    IReview getReview(Integer uniqueID);
    Boolean deleteReview(IReview review) throws IOException;
}
