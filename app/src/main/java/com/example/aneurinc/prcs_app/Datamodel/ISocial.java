package com.example.aneurinc.prcs_app.Datamodel;

import android.graphics.Bitmap;

import com.example.aneurinc.prcs_app.utilities.Observer.IDbSubject;

/**
 * Created by Dominic on 14/04/2016.
 */
public interface ISocial extends IDbSubject{
    Integer getSocialId();
    Boolean setSocialId(Integer id);

    Bitmap getImage();
    Boolean setImage(Bitmap img);

    String getFacebook();
    Boolean setFacebook(String fb);

    String getTwitter();
    Boolean setTwitter(String tw);

    String getInstagram();
    Boolean setInstagram(String insta);

    String getSoundcloud();
    Boolean setSoundcloud(String sc);

    String getWebsite();
    Boolean setWebsite(String web);

    String getSpotify();
    Boolean setSpotify(String sp);

}
