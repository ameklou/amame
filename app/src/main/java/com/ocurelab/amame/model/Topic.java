package com.ocurelab.amame.model;

import android.widget.TextView;

public class Topic {
    private String title, content, username,avatar, id;

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Topic() {
    }

    public Topic(String title, String content, String username, String avatar,String id) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.id=id;
    }
}
