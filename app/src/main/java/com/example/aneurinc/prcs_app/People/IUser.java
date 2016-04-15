package com.example.aneurinc.prcs_app.People;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IUser extends IPerson {

    String getAddress();
    Boolean setAddress(String address);

    Integer getCustomerID();

    String getPostcode();
    Boolean setPostcode(String postcode);
}
