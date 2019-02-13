package com.ocurelab.amame.model;

public class DomMessage {
    private String id,user,message;
    private Boolean receiver;

    public DomMessage() {
    }

    public DomMessage(String id, String user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getReceiver() {
        return receiver;
    }

    public void setReceiver(Boolean receiver) {
        this.receiver = receiver;
    }
}