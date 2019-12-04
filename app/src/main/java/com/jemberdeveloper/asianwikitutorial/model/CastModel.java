package com.jemberdeveloper.asianwikitutorial.model;

public class CastModel {

    private String image,url,realName,name;

    public CastModel() {
    }

    public CastModel(String image, String url, String realName, String name) {
        this.image = image;
        this.url = url;
        this.realName = realName;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
