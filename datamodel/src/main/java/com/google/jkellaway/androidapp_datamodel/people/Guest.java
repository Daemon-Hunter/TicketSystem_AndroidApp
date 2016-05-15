/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 * The type Guest.
 *
 * @author 10512691
 */
public class Guest implements IGuest {

    private String email, address, postcode, password;
    private DatabaseTable table;
    private Integer ID;
    private IBooking booking;

    /**
     * Use this constructor when creating a new Guest object.
     * ID is unknown.
     *
     * @param email    the email
     * @param address  the address
     * @param postcode the postcode
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Guest(String email, String address, String postcode) throws IllegalArgumentException {
        // Validate arguments, IllegalArgument will be thrown if they fail.
        Validator.emailValidator(email);
        Validator.addressValidator(address);
        Validator.postcodeValidator(postcode);

        // Given valid arguments, set variables.
        this.email = email;
        this.address = address;
        this.postcode = postcode;

        table = DatabaseTable.GUEST_BOOKING;
    }

    /**
     * Use this constructor when creating an object from the database.
     * ID is known.
     *
     * @param ID       the id
     * @param email    the email
     * @param address  the address
     * @param postcode the postcode
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Guest(Integer ID, String email, String address, String postcode) throws IllegalArgumentException {
        // Check email. Email can be null.
        if (email == null)
            throw new IllegalArgumentException("Please enter a valid email address. None was found when creating guest.");
        if (address == null)
            throw new IllegalArgumentException("Please enter a valid address. None was found when creating guest.");

        Validator.emailValidator(email);
        Validator.addressValidator(address);

        this.email = email;
        this.address = address;

        // Check the users postcode. Users don't have to have a postcode - so can be null
        Validator.postcodeValidator(postcode);
        this.postcode = postcode;

        this.password = Encrypt(password);
        table = DatabaseTable.GUEST_BOOKING;
    }

    @Override
    public Boolean setFirstName(String name) {
        return false;
    }

    @Override
    public Boolean setLastName(String name) {
        return false;
    }

    @Override
    public Boolean setPassword(String password) {
        return false;
    }

    @Override
    public IBooking getBooking() {
        return this.booking;
    }

    @Override
    public boolean setBooking(IBooking booking) throws IllegalArgumentException {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        } else {
            this.booking = booking;
            return this.booking == booking;
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
    public Integer getID() {
        if (ID == null) {
            throw new NullPointerException("ID is null");
        } else {
            return this.ID;
        }
    }

    @Override
    public String getFirstName() {

        return "GUEST";
    }

    @Override
    public String getLastName() {
        return "ACCOUNT";
    }

    /**
     * Gets table.
     *
     * @return the table
     */
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
    public Boolean setAddress(String address) throws IllegalArgumentException {
        if (address == null) throw new IllegalArgumentException("Enter an address.");
        Validator.addressValidator(address);
        this.address = address;
        return this.address.equals(address);
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
    public Boolean setPostcode(String postcode) throws IllegalArgumentException {
        if (postcode == null) throw new NullPointerException("Cannot set postcode to null");
        Validator.postcodeValidator(postcode);
        this.postcode = postcode;
        return this.postcode.equals(postcode);

    }

    @Override
    public String getPassword() {
        return password;
    }
}