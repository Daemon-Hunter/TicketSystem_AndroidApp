/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

import java.io.IOException;

/**
 *
 * @author 10467841
 */
public interface IUserWrapper extends IWrapper {

    IUser loginUser(String email, String password) throws IOException;
    IUser   getUser();
    Integer registerUser(Customer cust, String password);
}
