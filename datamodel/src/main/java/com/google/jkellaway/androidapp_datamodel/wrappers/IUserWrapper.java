/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface IUserWrapper extends IWrapper {

    Boolean loginUser(String email, String password) throws IOException;
    IUser   getUser();
    Integer registerUser(IUser cust, String password) throws IOException;
}
