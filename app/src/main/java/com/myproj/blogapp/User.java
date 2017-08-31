package com.myproj.blogapp;

/**
 * Created by Jawwad on 18/08/2017.
 */

public class User {

    private String name;
    private String status;
    private String onlinestatus;
    private String image;

    public User() {
    }

    public User(String name, String status, String onlinestatus, String image) {
        this.name = name;
        this.status = status;
        this.onlinestatus = onlinestatus;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnlinestatus() {
        return onlinestatus;
    }

    public void setOnlunestatus(String onlinestatus) {
        this.onlinestatus = onlinestatus;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
