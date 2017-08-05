package com.example.heramb.applicationoutline.Search;

/**
 * Created by heram on 6/29/2017.
 */

public class SearchResultItems {

    private String username;
    private String location;
    private String service;
    private Float rating;
    private String Uid;

    public SearchResultItems() {
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getService() {
        return service;
    }

    public Float getRating() {
        return rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public String getUid() {
        return Uid;
    }
}
