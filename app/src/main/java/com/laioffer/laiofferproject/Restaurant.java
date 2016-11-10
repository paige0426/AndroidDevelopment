package com.laioffer.laiofferproject;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by PaiGe on 2016/10/25.
 */

public class Restaurant {
    /**
     * All data for a restaurant.
     */
    private String name;
    private String address;
    private String type;
    private double lat;
    private double lng;
    private Bitmap thumbnail;
    private Bitmap rating;
    private List<String> categories;
    private double stars;
    private boolean isVisited;
    private String businessId;


    /**
     * Constructor
     *
     * @param name name of the restaurant
     */
    public Restaurant(String name, String address, String type, double lat, double lng, Bitmap thumbnail, Bitmap rating, boolean isVisited, String businessId) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.isVisited = isVisited;
        this.businessId = businessId;
    }

    public Restaurant() {
    }


    /**
     * Getters for private attributes of Restaurant class.
     */
    public String getName() {
        return this.name;
    }


    public String getAddress() {
        return this.address;
    }


    public String getType() {
        return this.type;
    }

    public double getLat() {
        return lat;
    }


    public double getLng() {
        return lng;
    }

    public List<String> getCategories() {
        return categories;
    }

    public double getStars() {
        return stars;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setRating(Bitmap rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setStars(double stars) {

        this.stars = stars;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public Bitmap getRating() {
        return rating;
    }
}
