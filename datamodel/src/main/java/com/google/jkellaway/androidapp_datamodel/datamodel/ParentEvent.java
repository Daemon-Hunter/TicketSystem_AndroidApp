/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.datamodel;

import android.graphics.Bitmap;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.reviews.IReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.ParentEventReviewFactory;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.idValidator;

/**
 *
 * @author 10512691
 */
public class ParentEvent implements IParentEvent {

    private IReviewFactory reviewFactory;
    private List<IReview> reviews;
    private List<IObserver> observers;
    private SocialMedia socialMedia;
    private String description;
    private DatabaseTable table;
    private int ID;
    private String name;
    
    private List<IChildEvent> childEvents;
    
    /**
     * Empty constructor initializes it's review factory and child event list.
     */
    public ParentEvent() {
        super();
        // Initialize table variable, which matches Java object to database table
        table = DatabaseTable.PARENT_EVENT;
        childEvents = new LinkedList<>();
        reviewFactory = new ParentEventReviewFactory();
    }
    
    public ParentEvent(Integer ID, SocialMedia social, String name, String description,
                        LinkedList<IReview> reviewsList, List<IChildEvent> events)
    {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.socialMedia = social;
        this.reviews = reviewsList;
        this.table = DatabaseTable.PARENT_EVENT;
        this.childEvents = events;
        this.reviewFactory = new ParentEventReviewFactory();

    }

    @Override
    public List<IChildEvent> getChildEvents() {
        if (childEvents == null) {
            throw new NullPointerException("Null child event list");
        } else {
            return childEvents;
        }
    }

    @Override
    public Boolean addChildEvent(IChildEvent childEvent) {
        if (childEvent == null) {
            throw new NullPointerException("Null child event");
        }
        return childEvents.add(childEvent);
    }

    @Override
    public IChildEvent getChildEvent(Integer childEventID) {
        if (childEventID == null) {
            throw new NullPointerException("Null child event ID");
        } else {
            return childEvents.get(childEventID);
        }
    }

    @Override
    public Boolean removeChildEvent(IChildEvent childEvent) {
        if (childEvent == null) {
            throw new NullPointerException("Null child event");
        } else {
            childEvents.remove(childEvent);
            return true;
        }
    }

    @Override
    public Integer getParentEventID() {
        return ID;
    }

    @Override
    public String getParentEventName() {
        return name;
    }

    @Override
    public String getParentEventDescription() {
        if (description == null) {
            throw new NullPointerException("Parent Event description is null");
        } else {
            return description;
        }
    }

    @Override
    public Boolean setParentEventName(String name) {
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null.");
        } else {
            this.name = name;
            return true;
        }
    }

    @Override
    public Boolean setParentEventDescription(String description) {
        if (description == null){
            throw new IllegalArgumentException("Description cannot be null");
        } else {
            this.description = description;
            return true;

        }
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    @Override
    public Integer getSocialId() {
        return socialMedia.getSocialId();
    }

    /**
     * Checks the validity of the ID before assigning.
     * @param id
     * @return Boolean true if ID set.
     */

    @Override
    public Boolean setSocialId(Integer id) {
        return socialMedia.setSocialId(id);
    }



    protected IReviewFactory getReviewFactory() {
        return reviewFactory;
    }

    /**
     * Adds IObserver object to list of objects to notify when a change is made.
     * Checks if the object is null or already exists in the list.
     * @param o
     * @return
     */
    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already exists");
        } else {
            observers.add(o);
            return true;
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer doesn't exist in observers list");
        } else {
            observers.remove(o);
            return true;
        }
    }

    @Override
    public void notifyObservers() {
        if (observers == null) {
            observers = new LinkedList();
        } else {
            for (IObserver o : observers) {
                o.update(this);
            }
        }
    }

    @Override
    public IReview createReview(Integer customerID, Integer rating, String body, Date date, Boolean verified) {
        return reviewFactory.createReview( ID, customerID, rating, date, body, verified);
    }

    @Override
    public IReview getReview(Integer customerID) {
        if (customerID == null) {
            throw new NullPointerException();
        } else {
            Boolean valid = idValidator(customerID);

            if (valid) {
                for (IReview r : reviews) {
                    if (r.getCustomerID().equals(customerID)) {
                        return r;
                    }
                }
                throw new IllegalArgumentException("No customers with that ID have "
                        + "written a review for this venue.");

            } else {
                throw new IllegalArgumentException("Invalid ID");
            }
        }

    }

    @Override
    public List<IReview> getReviews() {
        if (reviews == null) {
            throw new NullPointerException();
        } else {
            return reviews;
        }
    }

    @Override
    public Boolean deleteReview(IReview review) {
        if (review == null) {
            throw new NullPointerException("Review to be deleted was null");
        } else if (!reviews.contains(review)) {
            throw new IllegalArgumentException("Review to be deleted wasn't in list");
        } else {
            reviews.remove(review);
            notifyObservers();
            return true;
        }
    }

    @Override
    public DatabaseTable getTable() {
        if (table == null) {
            throw new NullPointerException();
        } else {
            return table;
        }
    }

    @Override
    public List<Bitmap> getImages() {
        return socialMedia.getImages();
    }

    @Override
    public Bitmap getImage(int index) {
        return socialMedia.getImage(index);
    }

    @Override
    public Boolean addImage(Bitmap img) {
        return socialMedia.addImage(img);
    }

    @Override
    public Boolean removeImage(int index) {
        return socialMedia.removeImage(index);
    }

    @Override
    public Boolean setImages(List<Bitmap> images) {
        return socialMedia.setImages(images);
    }


    @Override
    public String getFacebook() {
        return socialMedia.getFacebook();
    }

    @Override
    public Boolean setFacebook(String fb) {
        return socialMedia.setFacebook(fb);
    }

    @Override
    public String getTwitter() {
        return socialMedia.getTwitter();
    }

    @Override
    public Boolean setTwitter(String tw) {
        return socialMedia.setTwitter(tw);
    }

    @Override
    public String getInstagram() {
        return socialMedia.getInstagram();
    }

    @Override
    public Boolean setInstagram(String insta) {
        return socialMedia.setInstagram(insta);
    }

    @Override
    public String getSoundcloud() {
        return socialMedia.getSoundcloud();
    }

    @Override
    public Boolean setSoundcloud(String sc) {
        return socialMedia.setSoundcloud(sc);
    }

    @Override
    public String getWebsite() {
        return socialMedia.getWebsite();
    }

    @Override
    public Boolean setWebsite(String web) {
        return socialMedia.setWebsite(web);
    }

    @Override
    public String getSpotify() {
        return socialMedia.getSpotify();
    }

    @Override
    public Boolean setSpotify(String sp) {
        return socialMedia.setSpotify(sp);
    }
}
