/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.utilities.observer;

import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;

import java.io.IOException;

/**
 * The interface Observer.
 *
 * @author 10512691
 */
public interface IObserver {

    /**
     * Update.
     *
     * @param object the object
     * @param table  the table
     * @throws IOException the io exception
     */
    void update(ISubject object, DatabaseTable table) throws IOException;
}
