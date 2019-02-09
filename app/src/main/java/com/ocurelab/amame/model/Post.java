package com.ocurelab.amame.model;

public class Post {
    private String title,content,cover,category,created_at;

    public Post(String title, String content, String cover, String category, String created_at) {
        this.title = title;
        this.content = content;
        this.cover = cover;
        this.category = category;
        this.created_at = created_at;
    }

    public Post() {
    }

    public String getTitle() {
        return title;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
