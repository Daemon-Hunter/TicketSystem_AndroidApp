/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.events;

import android.graphics.Bitmap;

import java.util.List;

/**
 * The interface Social.
 *
 * @author 10512691
 */
public interface ISocial {

    /**
     * Gets social id.
     *
     * @return the social id
     */
    Integer getSocialId();

    /**
     * Sets social id.
     *
     * @param id the id
     * @return the social id
     */
    Boolean setSocialId(Integer id);

    /**
     * Gets images.
     *
     * @return the images
     */
    List<Bitmap> getImages();

    /**
     * Gets image.
     *
     * @param index the index
     * @return the image
     */
    Bitmap getImage(int index);

    /**
     * Add image boolean.
     *
     * @param img the img
     * @return the boolean
     */
    Boolean addImage(Bitmap img);

    /**
     * Remove image boolean.
     *
     * @param index the index
     * @return the boolean
     */
    Boolean removeImage(int index);

    /**
     * Sets images.
     *
     * @param images the images
     * @return the images
     */
    Boolean setImages(List<Bitmap> images);

    /**
     * Gets facebook.
     *
     * @return the facebook
     */
    String getFacebook();

    /**
     * Sets facebook.
     *
     * @param fb the fb
     * @return the facebook
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setFacebook(String fb) throws IllegalArgumentException;

    /**
     * Gets twitter.
     *
     * @return the twitter
     */
    String getTwitter();

    /**
     * Sets twitter.
     *
     * @param tw the tw
     * @return the twitter
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setTwitter(String tw) throws IllegalArgumentException;

    /**
     * Gets instagram.
     *
     * @return the instagram
     */
    String getInstagram();

    /**
     * Sets instagram.
     *
     * @param insta the insta
     * @return the instagram
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setInstagram(String insta) throws IllegalArgumentException;

    /**
     * Gets soundcloud.
     *
     * @return the soundcloud
     */
    String getSoundcloud();

    /**
     * Sets soundcloud.
     *
     * @param sc the sc
     * @return the soundcloud
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setSoundcloud(String sc) throws IllegalArgumentException;

    /**
     * Gets website.
     *
     * @return the website
     */
    String getWebsite();

    /**
     * Sets website.
     *
     * @param web the web
     * @return the website
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setWebsite(String web) throws IllegalArgumentException;

    /**
     * Gets spotify.
     *
     * @return the spotify
     */
    String getSpotify();

    /**
     * Sets spotify.
     *
     * @param sp the sp
     * @return the spotify
     * @throws IllegalArgumentException the illegal argument exception
     */
    Boolean setSpotify(String sp) throws IllegalArgumentException;
}
