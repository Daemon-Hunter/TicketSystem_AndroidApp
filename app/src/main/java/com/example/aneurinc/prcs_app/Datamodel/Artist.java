package com.example.aneurinc.prcs_app.Datamodel;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Reviews.ArtistReviewFactory;
import com.example.aneurinc.prcs_app.Reviews.IReview;
import com.example.aneurinc.prcs_app.Reviews.IReviewFactory;
import com.example.aneurinc.prcs_app.Reviews.ReviewBase;

import java.util.LinkedList;
import java.util.List;

import static com.example.aneurinc.prcs_app.Utility.Validator.tagValidator;

/**
 * Created by Dominic on 14/04/2016.
 */
public class Artist extends ReviewBase implements IArtist {
     /*
        Inherits:
        IReviewFactory        reviewFactory;
        LinkedList<Review>    reviews;
        LinkedList<IObserver> observers;
        SocialMedia           socialMedia;
        Integer               ID, socialMediaID;
        String                name;
        DatabaseTable         table;
     */

    private LinkedList<String> tags;

    public Artist() {
        super();
        // Initialize table variable - matches Java object to database table
        table = DatabaseTable.ARTIST;
        tags = new LinkedList<>();
        reviewFactory = new ArtistReviewFactory();
    }

    public Artist(Integer ID, String name, String description, LinkedList<String> tags, SocialMedia social,
                  LinkedList<IReview> reviews) {
        // Initialize table variable - matches Java object to database table
        table = DatabaseTable.ARTIST;

        // Initialise default values for rest of attributes
        this.tags = tags;
        this.ID = ID;
        socialMedia = social;
        this.reviews = reviews;
        this.name = name;
        reviewFactory = new ArtistReviewFactory();
        observers = new LinkedList<>();
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public IReviewFactory getReviewFactory() {
        return reviewFactory;
    }

    @Override
    public Integer getArtistID() {
        return ID;
    }

    @Override
    public String getArtistName() {
        return name;
    }

    @Override
    public List<String> getArtistTags() {
        return tags;
    }

    @Override
    public Boolean addArtistTag(String tag) {
        if (tag == null) {
            throw new NullPointerException();
        } else {
            Boolean valid = tagValidator(tag);
            if (valid) {
                tags.add(tag);
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Boolean removeArtistTag(String tag) {
        return tags.remove(tag);
    }
}
