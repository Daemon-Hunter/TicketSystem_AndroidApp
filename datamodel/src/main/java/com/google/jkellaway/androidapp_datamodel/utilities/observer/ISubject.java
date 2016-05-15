/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.utilities.observer;

import java.io.IOException;

/**
 * The interface Subject.
 *
 * @author 10512691
 */
public interface ISubject {

    /**
     * Notify the observers of a change in this object.
     *
     * @throws IOException the io exception
     */
    void notifyObservers() throws IOException;

    /**
     * Register observer boolean.
     *
     * @param o the o
     * @return the boolean
     */
    Boolean registerObserver(IObserver o);

    /**
     * Remove observer boolean.
     *
     * @param o the o
     * @return the boolean
     */
    Boolean removeObserver(IObserver o);
}
