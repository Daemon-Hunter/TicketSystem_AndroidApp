/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

/**
 * The interface IPerson is extend by the IUser and the IAdmin to abstract the methods.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IPerson {

    /**
     * Gets first name.
     *
     * @return the first name
     */
    String getFirstName();

    /**
     * Sets first name.
     *
     * @param name the name
     * @return the first name
     */
    Boolean setFirstName(String name);

    /**
     * Gets last name.
     *
     * @return the last name
     */
    String getLastName();

    /**
     * Sets last name.
     *
     * @param name the name
     * @return the last name
     */
    Boolean setLastName(String name);

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

    /**
     * Sets email.
     *
     * @param email the email
     * @return the email
     */
    Boolean setEmail(String email);

    /**
     * Sets password.
     *
     * @param password the password
     * @return the password
     */
    Boolean setPassword(String password);

    /**
     * Gets password.
     *
     * @return the password
     */
    String getPassword();
}
