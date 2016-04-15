package com.example.aneurinc.prcs_app.Utility;

import com.example.aneurinc.prcs_app.Datamodel.Artist;

import java.util.List;

/**
 * Created by Dominic on 15/04/2016.
 */
public class Information {
    private static Information me;
    private static List<Artist> listOfArtists;

    private Information()
    {}

    public static Information getInstance()
    {
        if (me == null)
        {
            me = new Information();
        }
        return me;
    }

    public static List<Artist> getListOfArtists()
    {
        return listOfArtists;
    }

    public static boolean setListOfArtists(List<Artist> artistList)
    {
        listOfArtists = artistList;
        return true;
    }

}
