package com.google.jkellaway.androidapp_datamodel.utilities;

/**
 * Created by Dominic on 25/04/2016.
 */
public class HashString {
    /**
     * Encrypt string.
     *
     * @param pass the pass
     * @return the string
     */
    public static String Encrypt(String pass) {
        String saltEnd = "162300JBKYSCOTT"; // 128 bit key;
        String saltStart = "7hegdChickenu2ys90";
        pass = saltStart + pass + saltEnd;
        Integer hashedValue = pass.hashCode();
        return Integer.toString(hashedValue);
    }

}
