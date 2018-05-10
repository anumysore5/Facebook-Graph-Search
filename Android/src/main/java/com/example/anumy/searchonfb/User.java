package com.example.anumy.searchonfb;

/**
 * Created by anumy on 15-04-2017.
 */

public class User {

    private String id;
    private String name;
    private String pictureUrl;
    private String type;
    private boolean isFavorited;

    public User(String id, String name, String pictureUrl, String type, boolean isFavorited) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.type = type;
        this.isFavorited = isFavorited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }
}
