package com.dcservicez.a247services.objects;

public class Service {
    String title,img_url;
    int color;
    String service;


    public Service(String title, String img_url, int color, String service) {
        this.title = title;
        this.img_url = img_url;
        this.color = color;
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public int getColor() {
        return color;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }
}
