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
public interface IPerson {
    
    String  getFirstName();
    Boolean setFirstName(String name);
    
    String getLastName();
    Boolean setLastName(String name);
    
    String  getEmail();
    Boolean setEmail(String email);

    Boolean setPassword(String password);
    String getPassword();
}
