package com.jemberdeveloper.asianwikitutorial.model;

public class UpcomingModel {
    private String link,title,thumbnail;

    public UpcomingModel() {
    }

    public UpcomingModel(String link, String title, String thumbnail) {
        this.link = link;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
