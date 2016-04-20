/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import java.util.LinkedList;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;
import com.google.jkellaway.androidapp_datamodel.utilities.observer.IObserver;

/**
 *
 * @author 10467841
 */
public class Admin implements IAdmin {
    
    private Integer ID;
    private String  firstName, lastName, email;
    private DatabaseTable table;
    private LinkedList<IObserver> observers;
    
    public Admin(String fName, String lName, String email) {
        ID = 0;
        if (fName == null || lName == null) {
            throw new NullPointerException("First or last name is null.");
        } else if (Validator.nameValidator(fName) && Validator.nameValidator(lName)) 
        {
            firstName = fName;
            lastName = lName;
        } else {
            throw new IllegalArgumentException("Invalid admin names.");
        }
        
        if (email == null) {
            throw new NullPointerException("Null email address");
        } else if (Validator.emailValidator(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email address");
        }
        observers = new LinkedList<>();
    }
    
    public Admin(Integer ID, String fName, String lName, String email) {
        this.ID = ID;
        firstName = fName;
        lastName = lName;
        
        observers = new LinkedList<>();
    }
    
    @Override
    public int getAdminID() {
        if (ID == null) {
            throw new NullPointerException("Null ID");
        } else {
            return ID;
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
            Boolean valid = Validator.emailValidator(email);
            if (valid) {
                this.email = email;
                notifyObservers();
            }
            return this.email.equals(email);
        }
    }

    @Override
    public DatabaseTable getTable() {
        return table;
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
    public Boolean registerObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer, cannot register.");
        } else if (observers.contains(o)) {
            throw new IllegalArgumentException("Observer already exists in list");
        } else {
            observers.add(o);
            return true;
        }
    }

    @Override
    public Boolean removeObserver(IObserver o) {
        if (o == null) {
            throw new NullPointerException("Null observer, cannot remove.");
        } else if (!observers.contains(o)) {
            throw new IllegalArgumentException("Observer doesn't exist in list");
        } else {
            observers.remove(o);
            return true;
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
    public Boolean setFirstName(String name) {
        if (name == null) {
            throw new NullPointerException("Cannot set first name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                firstName = name;
                notifyObservers();
            }
            return firstName.equals(name);
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
    public Boolean setLastName(String name) {
        if (name == null) {
            throw new NullPointerException("Cannot set last name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                lastName = name;
                notifyObservers();
            }
            return lastName.equals(name);
        }
    }
    
}
