package com.dcservicez.a247services.objects;

public class Review_item {
    String name,desc,url_img;
    int stars;

    public Review_item( String desc, int stars) {
        this.name = "n";
        this.desc = desc;
        this.url_img = "";
        this.stars = stars;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl_img() {
        return url_img;
    }

    public int getStars() {
        return stars;
    }
}

