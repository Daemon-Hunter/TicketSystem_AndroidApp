/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import android.graphics.Bitmap;

import java.util.List;

/**
 *
 * @author 10512691
 */
public interface ISocial {
    
    Integer getSocialId();
    Boolean setSocialId(Integer id);
    
    List<Bitmap> getImages();
    Bitmap getImage(int index);
    Boolean addImage(Bitmap img);
    Boolean removeImage(int index);
    Boolean setImages(List<Bitmap> images);
    
    String getFacebook();
    Boolean setFacebook(String fb) throws IllegalArgumentException;
    
    String getTwitter();
    Boolean setTwitter(String tw) throws IllegalArgumentException;
    
    String getInstagram();
    Boolean setInstagram(String insta) throws IllegalArgumentException;
    
    String getSoundcloud();
    Boolean setSoundcloud(String sc) throws IllegalArgumentException;
    
    String getWebsite();
    Boolean setWebsite(String web) throws IllegalArgumentException;
    
    String getSpotify();
    Boolean setSpotify(String sp) throws IllegalArgumentException;
}
