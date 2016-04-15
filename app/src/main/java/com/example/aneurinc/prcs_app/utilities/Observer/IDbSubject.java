package com.example.aneurinc.prcs_app.utilities.Observer;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IDbSubject extends ISubject{
    /**
     * Get the database table which this object maps.
     * @return Table enumeration.
     */
    DatabaseTable getTable();
}
