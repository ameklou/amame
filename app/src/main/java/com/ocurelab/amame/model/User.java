package com.ocurelab.amame.model;

public class User {
    private String username,bio,phone,id, fireId;

    public User() {
    }

    public String getFireId() {
        return fireId;
    }

    public void setFireId(String fireId) {
        this.fireId = fireId;
    }

    public User(String username, String bio, String phone, String id, String fireId) {
        this.username = username;
        this.bio = bio;
        this.phone = phone;
        this.id = id;
        this.fireId = fireId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
