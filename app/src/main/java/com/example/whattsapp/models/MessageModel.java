package com.example.whattsapp.models;

public class MessageModel {
    String uId, message;
    Long timeStamp;

    public MessageModel(String uId, String message, Long timeStamp) {
        this.uId = uId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModel(String uId) {
        this.uId = uId;
    }
    public MessageModel() {

    }

    public String getuId() {
        return uId;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

