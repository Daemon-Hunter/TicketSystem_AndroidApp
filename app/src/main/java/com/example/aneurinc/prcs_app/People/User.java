package com.example.aneurinc.prcs_app.People;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.utilities.Observer.IObserver;

import java.util.LinkedList;

import static com.example.aneurinc.prcs_app.Utility.Validator.addressValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.emailValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.nameValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.postcodeValidator;
import com.example.aneurinc.prcs_app.utilities.Observer.IObserver;

/**
 * Created by Dominic on 14/04/2016.
 */
public abstract class User implements IUser {

    protected LinkedList<IObserver> observers;
    protected String firstName, lastName, email, address, postcode;
    protected DatabaseTable table;
    protected Integer ID;


    public User(){}

    /**
     * Use when creating an object from the database.
     * e.g. ID is known.
     * @param ID
     * @param fName
     * @param lName
     * @param email
     * @param address
     * @param postcode
     */
    public User(Integer ID, String fName, String lName, String email, String address, String postcode) {
        this.ID = ID;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
    }

    /**
     * Use when creating a new object.
     * e.g. ID is unknown.
     * @param fName
     * @param lName
     * @param email
     * @param address
     * @param postcode
     */
    public User(String fName, String lName, String email, String address, String postcode) {

        // Check if names are null or valid. All users must have a valid name.
        if (fName == null || lName == null) {
            throw new NullPointerException("Cannot set first or last name to null");
        }
        if (nameValidator(fName) || nameValidator(lName)) {
            firstName = fName;
            lastName = lName;
        } else {
            throw new IllegalArgumentException("Invalid admin name");
        }

        // Check email. Email can be null.
        if (email != null) {
            if (emailValidator(email)) {
                this.email = email;
            } else {
                throw new IllegalArgumentException("Invalid email address");
            }
        } else {
            email = null;
        }

        // Check address. Users don't have to have an address, so it can be set to null
        if (address != null) {
            if (addressValidator(address)) {
                this.address = address;
            } else {
                throw new IllegalArgumentException("Invalid address.");
            }
        } else {
            address = null;
        }

        // Check the users postcode. Users don't have to have a postcode - so can be null
        if (postcode != null) {
            if (postcodeValidator(postcode)) {
                this.postcode = postcode;
            } else {
                throw new IllegalArgumentException("Invalid postcode");
            }
        } else {
            postcode = null;
        }
    }

    @Override
    public void notifyObservers() {
        if (observers != null) {
            for (IObserver o : observers) {
                o.update(this);
            }
        }
    }

    @Override
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer, cannot register");
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already registered");
        } else {
            return observers.add(o);
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer, cannot remove");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer not in list");
        } else {
            return observers.remove(o);
        }
    }

    @Override
    public String getEmail() {
        if (email == null) {
            throw new NullPointerException("Null email");
        } else {
            return email;
        }
    }

    @Override
    public Boolean setEmail(String email) {
        if (email == null) {
            throw new NullPointerException("Cannot set email to null");
        } else {
            Boolean valid = emailValidator(email);
            if (valid) {
                this.email = email;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Integer getCustomerID() {
        if (ID == null) {
            throw new NullPointerException("ID is null");
        } else {
            return ID;
        }
    }

    @Override
    public String getFirstName() {
        if (firstName == null) {
            throw new NullPointerException("Null first name");
        } else {
            return firstName;
        }
    }

    @Override
    public String getLastName() {
        if (lastName == null) {
            throw new NullPointerException("Null last name");
        } else {
            return lastName;
        }
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public String getAddress() {
        if (address == null) {
            throw new NullPointerException("Null address");
        } else {
            return address;
        }
    }

    @Override
    public Boolean setAddress(String address) {
        if (address == null) {
            throw new NullPointerException("Cannot set address to null");
        } else {
            Boolean valid = addressValidator(address);
            if (valid) {
                this.address = address;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public String getPostcode() {
        if (postcode == null) {
            throw new NullPointerException("Null postcode");
        } else {
            return postcode;
        }
    }

    @Override
    public Boolean setPostcode(String postcode) {
        if (postcode == null) {
            throw new NullPointerException("Cannot set postcode to null");
        } else {
            Boolean valid = postcodeValidator(postcode);
            if (valid) {
                this.postcode = postcode;
                notifyObservers();
            }
            return valid;
        }
    }
}

