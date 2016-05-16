/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

/**
 * The interface IUser is extended by the ICustomer and IGuest interfaces to provide abstraction.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IUser extends IPerson {

    /**
     * Gets address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Sets address.
     *
     * @param address the address
     * @return the address
     */
    Boolean setAddress(String address);

    /**
     * Gets id.
     *
     * @return the id
     */
    Integer getID();

    /**
     * Gets postcode.
     *
     * @return the postcode
     */
    String getPostcode();

    /**
     * Sets postcode.
     *
     * @param postcode the postcode
     * @return the postcode
     */
    Boolean setPostcode(String postcode);
}
