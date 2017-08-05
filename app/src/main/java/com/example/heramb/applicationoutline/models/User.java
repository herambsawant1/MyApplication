package com.example.heramb.applicationoutline.models;

/**
 * Created by heram on 7/22/2017.
 */

public class User {

    private String user_id;
    private String email;
    private String username;
    private long phoneNumber;

    public User(String user_id, String email, String username, long phoneNumber) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public User() {

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
