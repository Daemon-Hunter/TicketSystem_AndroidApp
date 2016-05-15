/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 * The type Admin.
 *
 * @author 10467841
 */
public class Admin implements IAdmin {

    private Integer ID;
    private String firstName, lastName, email, password;
    private DatabaseTable table;

    /**
     * Instantiates a new Admin.
     *
     * @param fName    the f name
     * @param lName    the l name
     * @param email    the email
     * @param password the password
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Admin(String fName, String lName, String email, String password) throws IllegalArgumentException {
        ID = 0;
        if (fName == null || lName == null)
            throw new IllegalArgumentException("First or last name is null.");
        Validator.nameValidator(fName);
        Validator.nameValidator(lName);
        Validator.emailValidator(email);

        firstName = fName;
        lastName = lName;
        this.email = email;
        this.password = Encrypt(password);
    }

    /**
     * Instantiates a new Admin.
     *
     * @param ID    the id
     * @param fName the f name
     * @param lName the l name
     * @param email the email
     */
    public Admin(Integer ID, String fName, String lName, String email) {
        this.ID = ID;
        firstName = fName;
        lastName = lName;
        this.email = email;
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
    public Boolean setEmail(String email) throws IllegalArgumentException {
        if (email == null) throw new NullPointerException("Cannot set email to null");
        Validator.emailValidator(email);
        this.email = email;
        return this.email.equals(email);

    }

    @Override
    public Boolean setPassword(String password) {
        this.password = Encrypt(password);
        return true;
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
    public Boolean setFirstName(String name) throws IllegalArgumentException {
        if (name == null) throw new NullPointerException("Cannot set first name to null");
        Validator.nameValidator(name);
        firstName = name;
        return firstName.equals(name);
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
    public Boolean setLastName(String name) throws IllegalArgumentException {
        if (name == null) throw new NullPointerException("Cannot set last name to null");
        Validator.nameValidator(name);
        lastName = name;
        return lastName.equals(name);
    }
}
