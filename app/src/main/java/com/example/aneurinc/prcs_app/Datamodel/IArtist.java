package com.example.aneurinc.prcs_app.Datamodel;

import java.util.List;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface IArtist {

    public Integer getArtistID();
    public String getArtistName();
    public List<String> getArtistTags();
    public Boolean addArtistTag(String tag);
    public Boolean removeArtistTag(String tag);
}
