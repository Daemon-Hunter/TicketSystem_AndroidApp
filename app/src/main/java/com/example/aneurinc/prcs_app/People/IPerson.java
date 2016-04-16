package com.example.aneurinc.prcs_app.People;

import com.example.aneurinc.prcs_app.Utility.Observer.IDbSubject;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IPerson extends IDbSubject {

    String  getFirstName();
    Boolean setFirstName(String name);

    String getLastName();
    Boolean setLastName(String name);

    String  getEmail();
    Boolean setEmail(String email);
}
