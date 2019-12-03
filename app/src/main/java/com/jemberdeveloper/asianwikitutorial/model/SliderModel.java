package com.jemberdeveloper.asianwikitutorial.model;

public class SliderModel {

    private String url,image,title;

    public SliderModel() {
    }

    public SliderModel(String url, String image, String title) {
        this.url = url;
        this.image = image;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
