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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.google.jkellaway.androidapp_datamodel.database.APIHandle.createContract;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.descriptionValidator;
import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.nameValidator;

/**
 * @author 10512691
 */
public class Artist implements IArtist {

    private List<IChildEvent> childEvents;

    private Integer socialMediaID;
    private SocialMedia socialMedia;

    private IReviewFactory reviewFactory;
    private String description;
    private DatabaseTable table;
    private int ID;
    private String name;
    private String type;
    private Integer typeID;
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
     * @param name
     * @param description
     * @param tags
     * @param social
     */
    public Artist(String name, String description, LinkedList<String> tags, SocialMedia social, Integer typeID) {

        nameValidator(name);
        descriptionValidator(description);

        this.ID = 0;
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
        return new LinkedList<>(tags);
    }

    @Override
    public Boolean addTag(String tag) throws IllegalArgumentException {
        if (tag == null)
            throw new IllegalArgumentException("Enter a tag.");
        Validator.tagValidator(tag);
        tags.add(tag);
        return tags.contains(tag);
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
    public Boolean setName (String name)  throws IllegalArgumentException {
        if (name == null)
            throw new IllegalArgumentException("Enter a name.");
        nameValidator(name);
        this.name = name;
        return this.name.equals(name);
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
    public Boolean setDescription(String description) throws IllegalArgumentException {
        if (description == null)
            throw new IllegalArgumentException("Enter a description.");
        Validator.descriptionValidator(description);
        this.description = description;
        return this.description.equals(description);
    }

    @Override
    public String getType() throws IOException {
        if (type == null) {
            type = (String)APIHandle.getSingle(typeID, DatabaseTable.ARTIST_TYPE);
            return type;
        } else {
            return type;
        }
    }

    @Override
    public Boolean setType(String type) {
        if (type == null) {
            throw new NullPointerException("cannot set type to null");
        } else {
            this.type = type;
            switch (type) {
                case "Singer":
                    typeID = 0;
                    break;
                case "Comedian":
                    typeID = 1;
                    break;
                case "Sports Team":
                    typeID = 2;
                    break;
                case "Band":
                    typeID = 3;
                    break;
                case "DJ":
                    typeID = 4;
                    break;
                default:
                    typeID = 0;
                    break;
            }
            return Objects.equals(this.type, type);
        }
    }

    @Override
    public Integer getTypeID() {
        return typeID;
    }

    @Override
    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMediaID = socialMedia.getSocialId();
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
    public IChildEvent getChildEvent(Integer childEventID) throws IOException {
        if (childEventID == null) {
            throw new NullPointerException("Null child event ID");
        } else {
            if (childEvents == null) {
                childEvents = getChildEvents();
            }
            for (IChildEvent childEvent : childEvents) {
                if (childEvent.getID().equals(childEventID))
                    return childEvent;
            }
            throw new NullPointerException("No child event with this ID");
        }
    }

    @Override
    public Boolean newContract(IChildEvent childEvent) throws IOException {
        if(createContract(this.ID, childEvent.getID())){
            childEvents.add(childEvent);
            return true;
        }
        return false;
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
    public Boolean setSocialId(Integer id) {
        socialMediaID = id;
        return socialMedia.setSocialId(id);
    }

    protected IReviewFactory getReviewFactory() {
        return reviewFactory;
    }

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
    public Boolean setFacebook(String fb) throws IllegalArgumentException {
        return socialMedia.setFacebook(fb);
    }

    @Override
    public String getTwitter() {
        return socialMedia.getTwitter();
    }

    @Override
    public Boolean setTwitter(String tw) throws IllegalArgumentException {
        return socialMedia.setTwitter(tw);
    }

    @Override
    public String getInstagram() {
        return socialMedia.getInstagram();
    }

    @Override
    public Boolean setInstagram(String insta) throws IllegalArgumentException {
        return socialMedia.setInstagram(insta);
    }

    @Override
    public String getSoundcloud() {
        return socialMedia.getSoundcloud();
    }

    @Override
    public Boolean setSoundcloud(String sc) throws IllegalArgumentException {
        return socialMedia.setSoundcloud(sc);
    }

    @Override
    public String getWebsite() {
        return socialMedia.getWebsite();
    }

    @Override
    public Boolean setWebsite(String web) throws IllegalArgumentException {
        return socialMedia.setWebsite(web);
    }

    @Override
    public String getSpotify() {
        return socialMedia.getSpotify();
    }

    @Override
    public Boolean setSpotify(String sp) throws IllegalArgumentException {
        return socialMedia.setSpotify(sp);
    }
}
