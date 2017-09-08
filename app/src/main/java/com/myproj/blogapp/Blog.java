package com.myproj.blogapp;

/**
 * Created by user on 8/7/2017.
 */
public class Blog {
    private String title, desc, image, username, uid, content;

    public Blog(String title, String desc, String image, String username, String uid, String content) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;
        this.content = content;
    }

    public Blog(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
