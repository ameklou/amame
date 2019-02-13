package com.ocurelab.amame.model;

public class Answer {
    private String message, owner,topic;

    public Answer(String message, String owner, String topic) {
        this.message = message;
        this.owner = owner;
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Answer() {
    }
}
