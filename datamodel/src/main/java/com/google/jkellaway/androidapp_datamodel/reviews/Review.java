/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.reviews;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

/**
 *
 * @author 10512691
 */
public abstract class Review implements IReview {
    protected Integer       reviewBaseID;
    protected Integer       customerID;
    protected Date          dateTime;
    protected Integer       rating;
    protected String        reviewBody;
    protected Boolean       verified;
    protected DatabaseTable table;
    protected LinkedList<IObserver> observers;
    
    /**
     * Use this constructor when creating a review object from the database.
     * Validity is is known.
     * @param baseID
     * @param customerID
     * @param rating
     * @param date
     * @param body
     * @param verified 
     */
    public Review(Integer baseID, Integer customerID, Integer rating, 
                    Date date, String body, Boolean verified) 
    {
        // Check to see if any of the arguments were null
        if (baseID == null || customerID == null) 
        {
            throw new NullPointerException("Review base & customer ID's cannot be null");
        } 
        else if (rating == null || body == null) 
        {
            throw new NullPointerException("Rating or body cannot be null");
        } 
        else if (verified == null) {
            throw new NullPointerException("Verified cannot be null");
        }
        else {
            
            // Given no arguments were null, see if they're valid
            if (Validator.idValidator(baseID)) {
                if (Validator.idValidator(customerID)) {
                    if (Validator.ratingValidator(rating)) {
                        if (Validator.reviewBodyValidator(body)) {
                                // Everything is valid -> initialise variables
                                reviewBaseID = baseID;
                                this.customerID = customerID;
                                this.rating = rating;
                                dateTime = date;
                                reviewBody = body;
                                this.verified = verified;
                                observers = new LinkedList();
                    } else {
                        throw new IllegalArgumentException("Body invalid");
                    }
                } else {
                    throw new IllegalArgumentException("Rating invalid");
                }
            } else {
                throw new IllegalArgumentException("CustomerID invalid");
            }
        } else {
                throw new IllegalArgumentException("ReviewBaseID invalid");
            }
        }
    }
    
    /**
     * Use this constructor when creating a new review object.
     * Verified is automatically set to false.
     * @param baseID
     * @param customerID
     * @param rating
     * @param body
     */
    public Review(Integer baseID, Integer customerID, Integer rating, String body) 
    {
        // Check to see if any of the arguments were null
        if (baseID == null || customerID == null) 
        {
            throw new NullPointerException("Review base & customer ID's cannot be null");
        } 
        else if (rating == null || body == null) 
        {
            throw new NullPointerException("Rating or body cannot be null");
        } 
        else {
            
            // Given no arguments were null, see if they're valid
            if (Validator.idValidator(baseID)) {
                if (Validator.idValidator(customerID)) {
                    if (Validator.ratingValidator(rating)) {
                        if (Validator.reviewBodyValidator(body)) {
                            
                            // Everything is valid -> initialise variables
                            reviewBaseID = baseID;
                            this.customerID = customerID;
                            this.rating = rating;
                            dateTime = Calendar.getInstance().getTime();
                            reviewBody = body;
                            this.verified = false;
                            observers = new LinkedList();
                        } else {
                            throw new IllegalArgumentException("Review body invalid");
                        }
                    } else {
                        throw new IllegalArgumentException("Rating invalid");
                    }
                } else {
                    throw new IllegalArgumentException("CustomerID invalid");
                }
            } else {
                throw new IllegalArgumentException("ReviewBaseID invalid");
            }
        }
    }
    
    @Override
    public DatabaseTable getTable() {
        return table;
    }
    
    @Override
    public void notifyObservers() throws IOException {
        if (observers == null) {
            observers = new LinkedList();
        } else {
            for (IObserver o : observers) {
                o.update(this, table);
            }
        }
    }
    
    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Cannot register a null observer");
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already exists in list");
        } else {
            return observers.add(o);
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Cannot remove a null observer");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer doesn't exist in list");
        } else {
            return observers.remove(o);
        }
    }

    @Override
    public Integer getReviewBaseID() {
        if (reviewBaseID == null) {
            throw new NullPointerException("Null ID");
        } else {
            return reviewBaseID;
        }
    }

    @Override
    public Integer getCustomerID() {
        if (customerID == null) {
            throw new NullPointerException("Null customer ID");
        } else {
            return customerID;
        }
    }

    @Override
    public Date getDateTime() {
        if (dateTime == null) {
            throw new NullPointerException("Null date / time");
        } else {
            return dateTime;
        }
    }

    @Override
    public Boolean setDateTime(Date datetime) {
        if (datetime == null) {
            throw new NullPointerException("Cannot set date / time to null");
        }
        dateTime = datetime;
        return dateTime == datetime;
        
    }

    @Override
    public Integer getRating() {
        if (rating == null) {
            throw new NullPointerException("Null rating");
        } else {
            return rating;
        }
    }

    @Override
    public Boolean setRating(Integer rating) throws IOException {
        if (rating == null) {
            throw new NullPointerException("Cannot set rating to null");
        } else {
            Boolean valid = Validator.ratingValidator(rating);
            if (valid) {
                this.rating = rating;
                notifyObservers();
            }
            return Objects.equals(this.rating, rating);
        }
    }

    @Override
    public String getBody() {
        if (reviewBody == null) {
            throw new NullPointerException("Null review body");
        } else {
            return reviewBody;
        }
    }

    @Override
    public Boolean SetBody(String body) throws IOException {
        if (body == null) {
            throw new NullPointerException("Cannot set review body to null");
        } else {
            Boolean valid = Validator.reviewBodyValidator(body);
            if (valid) {
                reviewBody = body;
                notifyObservers();
            }
            return reviewBody.equals(body);
        }
    }

    @Override
    public Boolean isVerified() {
        if (verified == null) {
            throw new NullPointerException("Verified equals null");
        } else {
            return verified;
        }
    }

    @Override
    public Boolean setVerified(Boolean verified) {
        if (verified == null) {
            throw new NullPointerException("Cannot set verified as null");
        } else {
            this.verified = verified;
        }
        return Objects.equals(this.verified, verified);
    }
}
