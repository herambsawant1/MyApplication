package com.example.heramb.applicationoutline.Models;

/**
 * Created by heram on 7/22/2017.
 */

public class UserInformation {

    private String description;
    private String displayName;
    private String profilePhoto;
    private String username;
    private String location;
    private String service;
    private long serviceRating;
    private long serviceRatingCount;
    private long experienceRating;

    public UserInformation(String description, String displayName, String profilePhoto, String username, String location,
                           String service, long serviceRating, long serviceRatingCount, long experienceRating) {
        this.description = description;
        this.displayName = displayName;
        this.profilePhoto = profilePhoto;
        this.username = username;
        this.location = location;
        this.service = service;
        this.serviceRating = serviceRating;
        this.serviceRatingCount = serviceRatingCount;
        this.experienceRating = experienceRating;
    }
    public UserInformation() {

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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

    public void setServiceRating(long serviceRating) {
        this.serviceRating = serviceRating;
    }

    public void setServiceRatingCount(long serviceRatingCount) {
        this.serviceRatingCount = serviceRatingCount;
    }

    public void setExperienceRating(long experienceRating) {
        this.experienceRating = experienceRating;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
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

    public long getServiceRating() {
        return serviceRating;
    }

    public long getServiceRatingCount() {
        return serviceRatingCount;
    }

    public long getExperienceRating() {
        return experienceRating;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                ", service='" + service + '\'' +
                ", serviceRating='" + serviceRating + '\'' +
                ", serviceRatingCount='" + serviceRatingCount + '\'' +
                ", experienceRating='" + experienceRating + '\'' +
                '}';
    }
}