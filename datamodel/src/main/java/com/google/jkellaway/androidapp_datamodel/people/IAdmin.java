/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

/**
 * The interface IAdmin is implemented by any class wishing to represent an Admin.
 *
 * @author Joshua Kellaway
 * @author Charles Gillions
 */
public interface IAdmin extends IPerson {

    /**
     * Gets id.
     *
     * @return the id
     */
    Integer getID();
}
