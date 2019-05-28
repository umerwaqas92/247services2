package com.dcservicez.a247services.objects;

public class MyLocation {
    public double latitude;
    public double longitude;


    public MyLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}