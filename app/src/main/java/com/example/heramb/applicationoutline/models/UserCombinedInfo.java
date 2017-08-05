package com.example.heramb.applicationoutline.models;

/**
 * Created by heram on 8/2/2017.
 */

public class UserCombinedInfo {
    private User user;
    private UserInformation settings;

    public UserCombinedInfo(User user, UserInformation settings) {
        this.user = user;
        this.settings = settings;
    }

    public UserCombinedInfo() {

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserInformation getInformation() {
        return settings;
    }

    public void setInformation(UserInformation settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", settings=" + settings +
                '}';
    }
}
