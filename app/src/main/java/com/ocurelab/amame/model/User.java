package com.ocurelab.amame.model;

public class User {
    private String username,bio,phone,id;

    public User() {
    }

    public User(String username, String bio, String phone, String id) {
        this.username = username;
        this.bio = bio;
        this.phone = phone;
        this.id = id;
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
