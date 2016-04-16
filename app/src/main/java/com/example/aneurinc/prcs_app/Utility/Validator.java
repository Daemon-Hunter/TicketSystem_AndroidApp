package com.example.aneurinc.prcs_app.Utility;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.aneurinc.prcs_app.Utility.Blacklist.contains;
import static java.lang.System.out;
import static java.util.regex.Pattern.compile;


/**
 * Created by Dominic on 14/04/2016.
 */
public final class Validator {

    public static Boolean quantityValidator(Integer qty) {
        return 0 < qty;
    }

//    /**
//     * Checks whether the date / time is in a valid time period.
//     * @param time
//     * @return
//     */
//    public static Boolean dateTimeValidator(Date time) {
//
//        // Initialise date object with values from first launch.
//        Date date = new Date();
//        date.setDate(22);
//        date.setHours(12);
//        date.setMinutes(0);
//        date.setMonth(2);
//        date.setSeconds(0);
//        date.setYear(2016);
//
//        // Check whether the date object is in a valid time period.
//        return time.after(date) && time.before(new Date());
//    }

    /**
     * Checks the rating is in range '0 to 5'
     *
     * @param rating
     * @return
     */
    public static Boolean ratingValidator(Integer rating) {
        return 0 < +rating && rating <= 5;
    }

    /**
     * Returns true if the review body is between 15 & 100 characters.
     *
     * @param body
     * @return
     */
    public static Boolean reviewBodyValidator(String body) {
        return (body.length() <= 140 && 5 <= body.length() && !contains(body));
    }

    /**
     * Returns true if the ID has been initialized.
     *
     * @param id
     * @return
     */
    public static Boolean idValidator(Integer id) {

//      ************ Remove this statement in final version!! *****************
        if (id == 0) {
            out.println("Object has not been set a valid ID!");
            return true;
            // return false!
        } else {
            Pattern idPattern = compile("[1-9]{1}[0-9]{0,7}");
            Matcher matcher = idPattern.matcher(id.toString());
            return matcher.matches();
        }
    }

    /**
     * Tries to make a connection with the given URL.
     * Returns true if a connection is made, false if not.
     *
     * @param url
     * @return True if a connection is made
     */
    public static Boolean URLValidator(String url) {
        try {
            // Create an instance of a URL object.
            // Will throw an error if the string is invalid.
            URL website = new URL(url);
            try {
                // Try to make a connection.
                website.openConnection();
                return true;
            } catch (IOException ex) {
                return false;
            }
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     * returns true if the name does not contain any blacklisted words,
     * and is between 2 & 20 characters long.
     *
     * @param name
     * @return
     */
    public static Boolean nameValidator(String name) {
//        if (!Blacklist.contains(name)) {
//            return 2 <= name.length()
//                    && name.length() <= 20;
//        } else
        return false;
    }

    /**
     * Description must be between 10 & 100 characters long,
     * and not have bad words...
     *
     * @param description
     * @return
     */
    public static Boolean descriptionValidator(String description) {

        ArrayList<String> badWords = new ArrayList<>();
        badWords.add("flange");
        badWords.add("bieber");
        badWords.add("rolf harris");
        badWords.add("Mouldy cabbage");
//      ...etc...

        Boolean naughty = false;

        for (String badWord : badWords) {
            if (description.contains(badWord)) {
                Log.i("", "Oi! None of that...");
                naughty = true;
                break;
            }
        }

        return 10 <= description.length()
                && description.length() <= 100
                && naughty;
    }

    public static Boolean capacityValidator(Integer standing) {
        return true;
    }

    public static Boolean facilitiesValidator(String facilities) {
        return contains(facilities) && facilities.length() <= 100;
    }

    public static Boolean parkingSpaceValidator(Integer parking) {
        return parking <= 100000 && parking >= 0;
    }

    public static Boolean emailValidator(String email) {
        return true;
    }

    public static Boolean phoneNumberValidator(String phoneNumber) {
        return true;
    }

    // Check against other addresses? Cannot have 2 venues at same place?
    public static Boolean addressValidator(String address) {
        return true;
    }

    public static Boolean postcodeValidator(String postcode) {
        return true;
    }

    public static Boolean tagValidator(String tag) {
        return contains(tag) && tag.contains(",");
    }

    public static String formatPrice(Double price) {

        DecimalFormat df = new DecimalFormat("£0.00");
        String formattedPrice = df.format(price);

        return formattedPrice;
    }

    public static Bitmap scaleDown(Bitmap image, int xy) {
        return Bitmap.createScaledBitmap(image, xy, xy, true);
    }
}
