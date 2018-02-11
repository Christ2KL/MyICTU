package com.tekaware.christ.myictu;

/**
 * This file was Created by Chris on 10/02/2018.
 */

public class Notice {

    private String title;
    private String content;

    public Notice(){

    }

    public Notice(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }

    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
