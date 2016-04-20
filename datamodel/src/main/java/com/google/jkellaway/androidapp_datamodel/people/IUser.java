/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

/**
 *
 * @author 10512691
 */
public interface IUser extends IPerson {
    
    String getAddress();
    Boolean setAddress(String address);
    
    Integer getCustomerID();
    
    String getPostcode();
    Boolean setPostcode(String postcode);
}
