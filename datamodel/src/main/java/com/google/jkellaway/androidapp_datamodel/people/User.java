/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;

import java.io.IOException;
import java.util.LinkedList;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

/**
 *
 * @author 10512691
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
        if (Validator.nameValidator(fName) || Validator.nameValidator(lName)) {
            firstName = fName;
            lastName = lName;
        } else {
            throw new IllegalArgumentException("Invalid admin name");
        }
        
        // Check email. Email can be null.
        if (email != null) {
            if (Validator.emailValidator(email)) {
            this.email = email;
            } else {
                throw new IllegalArgumentException("Invalid email address");
            }
        } else {
            email = null;
        }
        
        // Check address. Users don't have to have an address, so it can be set to null
        if (address != null) {
            if (Validator.addressValidator(address)) {
                this.address = address;
            } else {
                throw new IllegalArgumentException("Invalid address.");
            }
        } else {
            address = null;
        }
        
        // Check the users postcode. Users don't have to have a postcode - so can be null
        if (postcode != null) {
            if (Validator.postcodeValidator(postcode)) {
                this.postcode = postcode;
            } else {
                throw new IllegalArgumentException("Invalid postcode");
            }
        } else {
            postcode = null;
        }
    }
    
    @Override
    public void notifyObservers() throws IOException {
        
        if (observers != null) {
            for (IObserver o : observers) {
                o.update(this, table);
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
    public Boolean setEmail(String email) throws IOException {
        if (email == null) {
            throw new NullPointerException("Cannot set email to null");
        } else {
            Boolean valid = Validator.emailValidator(email);
            if (valid) {
                this.email = email;
                notifyObservers();
            }
            return valid;
        }
    }
    
    @Override
    public Integer getID() {
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
    public Boolean setAddress(String address) throws IOException {
        if (address == null) {
            throw new NullPointerException("Cannot set address to null");
        } else {
            Boolean valid = Validator.addressValidator(address);
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
    public Boolean setPostcode(String postcode) throws IOException {
        if (postcode == null) {
            throw new NullPointerException("Cannot set postcode to null");
        } else {
            Boolean valid = Validator.postcodeValidator(postcode);
            if (valid) {
                this.postcode = postcode;
                notifyObservers();
            }
            return valid;
        }
    }
}
