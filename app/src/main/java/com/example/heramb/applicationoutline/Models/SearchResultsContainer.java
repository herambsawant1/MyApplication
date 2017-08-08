package com.example.heramb.applicationoutline.Models;

/**
 * Created by heram on 8/7/2017.
 */

public class SearchResultsContainer {
    private UserInformation userInformation;
    private String UID;

    public SearchResultsContainer(UserInformation userInformation, String UID) {
        this.userInformation = userInformation;
        this.UID = UID;
    }
    public SearchResultsContainer() {
        this.userInformation = userInformation;
        this.UID = UID;
    }
    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public String getUID() {
        return UID;
    }
}
