package com.example.aneurinc.prcs_app.Datamodel;

import com.example.aneurinc.prcs_app.utilities.Observer.IDbSubject;

import java.util.List;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface ILineup extends IDbSubject{

    public Integer getLineupID();
    public List<IArtist> getArtistList();
    public Boolean addArtist(IArtist artist);
    public Boolean removeArtist(IArtist artist);
    public IArtist getArtist(Integer artistID);

}

