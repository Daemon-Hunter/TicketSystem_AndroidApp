/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.utilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Validator class is used to check if input matches required validation.
 * This is also an example of the Servant Pattern.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public final class Validator {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%\\+-]+@[A-Z0-9.-]+.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern POSTCODE_REGEX = Pattern.compile("[a-zA-Z]{1,2}[0-9][0-9a-zA-Z]?[0-9][a-zA-Z]{2}");

    // Matches landlines and mobile numbers - e.g. 07534951289 || +447534951289 || 01934862045
    private static final Pattern PHONE_REGEX = Pattern.compile("[\\+][4]{2}[1237][\\d]{8,9}");

    /**
     * Price validator.
     *
     * @param price the price
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void priceValidator(String price) throws IllegalArgumentException {
        try {
            Double numbPrice = Double.parseDouble(price);

            String[] splitNumb = numbPrice.toString().split(".");
            if (splitNumb.length == 2) {
                if (splitNumb[1].length() > 2)
                    throw new IllegalArgumentException("Invalid price. Too many digits after the decimal point.");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Inputted price string cannot be converted to a number.");
        }
    }

    /**
     * Quantity validator.
     *
     * @param qty the qty
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void quantityValidator(Integer qty) throws IllegalArgumentException {
        if (!(0 < qty)) throw new IllegalArgumentException("You cannot have a negative quantity!");
    }

    /**
     * Checks the rating is in range '0 to 5'
     *
     * @param rating the rating
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void ratingValidator(Integer rating) throws IllegalArgumentException {
        if (!(0 < rating && rating <= 5))
            throw new IllegalArgumentException("Rating needs to between 1 and 5.");
    }

    /**
     * Returns true if the review body is between 15 & 100 characters.
     *
     * @param body the body
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void reviewBodyValidator(String body) throws IllegalArgumentException {
        if (Blacklist.contains(body))
            throw new IllegalArgumentException("Review body contains blacklisted words. Please remove, and try again.");

        if (!(body.length() <= 150 && 5 <= body.length()))
            throw new IllegalArgumentException("Review body length needs to be between 5 and 150 characters in length.");

    }

    /**
     * Tries to make a connection with the given URL.
     * Returns true if a connection is made, false if not.
     *
     * @param url the url
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void URLValidator(String url) throws IllegalArgumentException {
        if (url != null && !url.equals("")) {
            try {
                // Create an instance of a URL object.
                // Will throw an error if the string is invalid.
                URL website = new URL(url);
                try {
                    // Try to make a connection.
                    website.openConnection();
                } catch (IOException ex) {
                    throw new IllegalArgumentException("Could not make a connection to the URL. Review the web location and try again.");
                }
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Could not make a connection to the URL - malformed URL string.");
            }
        }
    }

    /**
     * returns true if the name does not contain any blacklisted words,
     * and is between 2 & 20 characters long.
     *
     * @param name the name
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void nameValidator(String name) throws IllegalArgumentException {
        if (Blacklist.contains(name))
            throw new IllegalArgumentException("Name contains blacklisted words. Please remove, and try again.");

        if (!(2 <= name.length() && name.length() <= 30))
            throw new IllegalArgumentException("The name's length must be between 2 and 30 characters. Please try again.");
    }

    /**
     * Description must be between 10 & 100 characters long,
     * and not contain blacklisted words...
     *
     * @param description the description
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void descriptionValidator(String description) throws IllegalArgumentException {
        if (Blacklist.contains(description))
            throw new IllegalArgumentException("Description cannot contain blacklisted words. Please remove, and try again.");

        if (!(10 <= description.length() && description.length() <= 500))
            throw new IllegalArgumentException("Description must be over length 10 and under 500.");
    }

    /**
     * Integer value must be between 0 & 1 million.
     *
     * @param capacity the capacity
     * @return
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void capacityValidator(Integer capacity) throws IllegalArgumentException {
        if (!(capacity >= 0 && capacity <= 1000000))
            throw new IllegalArgumentException("Capacity must be greater than 0 and under 1 million.");
    }

    /**
     * Facilities description cannot contain blacklisted words, and must be under 100 characters.
     *
     * @param facilities the facilities
     * @return
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void facilitiesValidator(String facilities) throws IllegalArgumentException {
        if (Blacklist.contains(facilities))
            throw new IllegalArgumentException("Facilities description contains blacklisted words. Please edit, and try again.");

        if (!(facilities.length() <= 100))
            throw new IllegalArgumentException("Facilities description length must be under 100 characters.");
    }

    /**
     * Parking space validator.
     *
     * @param parking the parking
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void parkingSpaceValidator(Integer parking) throws IllegalArgumentException {
        if (!(parking <= 100000))
            throw new IllegalArgumentException("Cannot have more than 100000 parking spaces.");

        if (!(parking >= 0))
            throw new IllegalArgumentException("Cannot set the number of parking spaces to a negative number.");
    }

    /**
     * Email validator.
     *
     * @param email the email
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void emailValidator(String email) throws IllegalArgumentException {
        Matcher m = EMAIL_REGEX.matcher(email);
        if (!m.matches())
            throw new IllegalArgumentException("You've entered an invalid email address. Please review and try again.");
    }

    /**
     * Phone number validator.
     *
     * @param phoneNumber the phone number
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void phoneNumberValidator(String phoneNumber) throws IllegalArgumentException {
        phoneNumber = phoneNumber.replace(" ", "");
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "+44" + phoneNumber.substring(1);
        }
        Matcher m = PHONE_REGEX.matcher(phoneNumber);
        if (!m.matches())
            throw new IllegalArgumentException("You've entered an invalid UK phone number. Please review and try again.");
    }

    /**
     * Address validator.
     *
     * @param address the address
     * @throws IllegalArgumentException the illegal argument exception
     */
// Check against other addresses? Cannot have 2 venues at same place?
    public static void addressValidator(String address) throws IllegalArgumentException {
        if (Blacklist.contains(address))
            throw new IllegalArgumentException("The address you've entered contains blacklisted words. Please review and try again.");

        if (!(address.length() <= 200 && 5 <= address.length()))
            throw new IllegalArgumentException("The address must be between 5 and 200 characters long.");
    }

    /**
     * Postcode cannot contain spaces.
     *
     * @param postcode the postcode
     * @return
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static void postcodeValidator(String postcode) throws IllegalArgumentException {
        Matcher m = POSTCODE_REGEX.matcher(postcode);
        if (!m.matches())
            throw new IllegalArgumentException("The postcode you've entered is invalid. please enter a valid UK postcode.");
    }

    /**
     * Tag validator.
     *
     * @param tag the tag
     */
    public static void tagValidator(String tag) {
        if (Blacklist.contains(tag)) throw new IllegalArgumentException("The tag you've entered ");
    }
}