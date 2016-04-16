package com.example.aneurinc.prcs_app.Utility.Observer;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface ISubject {

    /**
     * Notify the observers of a change in this object.
     **/
    void notifyObservers();

    Boolean registerObserver(IObserver o);

    Boolean removeObserver(IObserver o);
}