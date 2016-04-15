package com.example.aneurinc.prcs_app.Datamodel;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Reviews.IReview;
import com.example.aneurinc.prcs_app.Reviews.ParentEventReviewFactory;
import com.example.aneurinc.prcs_app.Reviews.ReviewBase;

import java.util.LinkedList;

import static com.example.aneurinc.prcs_app.Utility.Validator.descriptionValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.nameValidator;

/**
 * Created by Dominic on 14/04/2016.
 */
public class ParentEvent extends ReviewBase implements IParentEvent {

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

    private LinkedList<ChildEvent> childEvents;

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
                       LinkedList<IReview> reviewsList,LinkedList<ChildEvent> events)
    {
        this.ID = ID;
        this.name = name;
        this.description = description;
        table = DatabaseTable.PARENT_EVENT;
        childEvents = events;
        this.reviews = reviewsList;
        socialMedia = social;
        reviewFactory = new ParentEventReviewFactory();

    }

    @Override
    public Integer getSocialId() {
        return socialMedia.getSocialId();
    }

    @Override
    public Boolean setSocialId(Integer socialId) {
        return socialMedia.setSocialId(socialId);
    }

    @Override
    public LinkedList<ChildEvent> getChildEvents() {
        if (childEvents == null) {
            throw new NullPointerException("Null child event list");
        } else {
            return childEvents;
        }
    }

    @Override
    public Integer getParentEventID() {
        if (ID == null) {
            throw new NullPointerException("Null ID");
        } else {
            return ID;
        }
    }

    @Override
    public String getParentEventName() {
        if (name == null) {
            throw new NullPointerException("Null name");
        } else {
            return name;
        }
    }

    @Override
    public String getParentEventDescription() {
        if (description == null) {
            throw new NullPointerException("Null description");
        } else {
            return description;
        }
    }

    @Override
    public Boolean setParentEventName(String name) {
        if (name == null) {
            throw new NullPointerException("Null name");
        } else {
            Boolean valid = nameValidator(name);
            if (valid) {
                this.name = name;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Boolean setParentEventDescription(String description) {
        if (description == null) {
            throw new NullPointerException("Null description");
        } else {
            Boolean valid = descriptionValidator(description);
            if (valid) {
                this.description = description;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Boolean addChildEvent(ChildEvent childEvent) {
        if (childEvent == null) {
            throw new NullPointerException("Null child event");
        }
        return childEvents.add(childEvent);
    }

    @Override
    public ChildEvent getChildEvent(Integer childEventID) {
        if (childEventID == null) {
            throw new NullPointerException("Null child event ID");
        } else {
            return childEvents.get(childEventID);
        }
    }

    @Override
    public Boolean removeChildEvent(ChildEvent childEvent) {
        if (childEvent == null) {
            throw new NullPointerException("Null child event");
        } else {
            childEvents.remove(childEvent);
            return true;
        }
    }
}
