/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import android.graphics.Bitmap;

import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.reviews.ArtistReviewFactory;
import com.google.jkellaway.androidapp_datamodel.reviews.IReviewFactory;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 10512691
 */
public class Artist implements IArtist {

    private List<IChildEvent> childEvents;

    private Integer socialMediaID;
    private SocialMedia socialMedia;

    private IReviewFactory reviewFactory;
    private List<IObserver> observers;
    private String description;
    private DatabaseTable table;
    private int ID;
    private String name;
    private String type;
    private Integer typeID;

    /*
        Inherits:
        IReviewFactory        reviewFactory;
        LinkedList<Review>    reviews;
        LinkedList<IObserver> observers;
        SocialMedia           socialMedia;
        Integer               socialMediaID
        DatabaseTable         table;
     */

    private LinkedList<String> tags;

    public Artist() {
        this.table = DatabaseTable.ARTIST;
        tags = new LinkedList<>();
        reviewFactory = new ArtistReviewFactory();
    }

    /**
     * Use this constructor when creating an artist from the database.
     * Given arguments are known to be valid.
     * No child events given. When a call is made to the artist to return it's child events,
     * it will fetch relevant events through the API.
     *
     * @param ID          Already allocated
     * @param name
     * @param description
     * @param tags
     * @param social
     */
    public Artist(Integer ID, String name, String description, LinkedList<String> tags, SocialMedia social, Integer typeID) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.socialMedia = social;
        this.table = DatabaseTable.ARTIST;
        this.typeID = typeID;

        // Initialise default values for rest of attributes
        this.tags = tags;
        reviewFactory = new ArtistReviewFactory();
    }

    /**
     * Use this constructor when
     *
     * @param ID
     * @param name
     * @param description
     * @param tags
     * @param socialMediaID
     * @param typeID
     */
    public Artist(Integer ID, String name, String description, LinkedList<String> tags, Integer socialMediaID, Integer typeID) {

        this.ID = ID;
        this.name = name;
        this.description = description;
        this.socialMediaID = socialMediaID;
        this.table = DatabaseTable.ARTIST;
        this.typeID = typeID;

        // Initialise default values for rest of attributes
        this.tags = tags;
        reviewFactory = new ArtistReviewFactory();

    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public Boolean addTag(String tag) throws IOException {
        if (tag == null) {
            throw new NullPointerException();
        } else {
            Boolean valid = Validator.tagValidator(tag);
            if (valid) {
                tags.add(tag);
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Boolean removeTag(String tag) {

        return tags.remove(tag);

    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        if (description == null) {
            throw new NullPointerException("Artist description is null");
        } else {
            return description;
        }
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getType() {
        if (type == null) {
            throw new NullPointerException("Artist type is null");
        } else {
            return type;
        }
    }

    @Override
    public Boolean setType(String type) {
        if (type == null) {
            throw new NullPointerException("Artist type is null");
        } else {
            this.type = type;
            return this.type == type;
        }
    }

    @Override
    public Integer getTypeID() {
        return typeID;
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    @Override
    public SocialMedia getSocialMedia() {
        return this.socialMedia;
    }

    @Override
    public List<IChildEvent> getChildEvents() throws IOException {
        if (childEvents == null) {
            childEvents = (List<IChildEvent>) (Object) APIHandle.getObjectsFromObject(this.ID, DatabaseTable.CHILD_EVENT, DatabaseTable.ARTIST);
            return new LinkedList<>(childEvents);
        } else {
            return new LinkedList<>(childEvents);
        }
    }

    @Override
    public Integer getSocialId() {
        return socialMediaID;
    }

    /**
     * Checks the validity of the ID before assigning.
     *
     * @param id
     * @return Boolean true if ID set.
     */

    @Override
    public Boolean setSocialId(Integer id) throws IOException {
        socialMediaID = id;
        return socialMedia.setSocialId(id);
    }


    protected IReviewFactory getReviewFactory() {
        return reviewFactory;
    }

    /**
     * Adds IObserver object to list of objects to notify when a change is made.
     * Checks if the object is null or already exists in the list.
     *
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
