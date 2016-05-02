/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import java.io.IOException;

import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 *
 * @author 10467841
 */
public class Admin implements IAdmin {
    
    private Integer ID;
    private String  firstName, lastName, email, password;
    private DatabaseTable table;
    
    public Admin(String fName, String lName, String email, String password) {
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

        this.password = Encrypt(password);
    }
    
    public Admin(Integer ID, String fName, String lName, String email) {
        this.ID = ID;
        firstName = fName;
        lastName = lName;
    }
    
    @Override
    public Integer getID() {
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
    public Boolean setEmail(String email) throws IOException {
        if (email == null) {
            throw new NullPointerException("Cannot set email to null");
        } else {
            Boolean valid = Validator.emailValidator(email);
            if (valid) {
                this.email = email;
            }
            return this.email.equals(email);
        }
    }

    @Override
    public Boolean setPassword(String password) {
        this.password = password;
        return this.password == password;
    }

    @Override
    public String getPassword() {
        return password;
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
    public Boolean setFirstName(String name) throws IOException {
        if (name == null) {
            throw new NullPointerException("Cannot set first name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                firstName = name;
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
    public Boolean setLastName(String name) throws IOException {
        if (name == null) {
            throw new NullPointerException("Cannot set last name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                lastName = name;
            }
            return lastName.equals(name);
        }
    }
    
}
