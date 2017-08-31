package com.myproj.blogapp;

/**
 * Created by Jawwad on 19/08/2017.
 */

public class Message {
    private String message;
    private long time;
    private String from, image;

    public Message() {
        // Default COnstrutor
    }

    public Message(String from) {
        this.from = from;
    }

    public Message(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public Message(String message, long time, String from, String image) {
        this.message = message;
        this.time = time;
        this.from = from;
        this.image = image;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
