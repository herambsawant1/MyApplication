package com.example.heramb.applicationoutline.Search;

/**
 * Created by heram on 6/21/2017.
 */

public class SearchResultItem {

     String userName;
     String location;
     String service;
     Float rating;

    public SearchResultItem(String userName, String location, String service, Float rating ) {
        this.userName=userName;
        this.location=location;
        this.service=service;
        this.rating=rating;

    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserName() {

        return userName;
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
}
