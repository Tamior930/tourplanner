package com.teameight.tourplanner.model;

public class Geocode {


    private String text;
    private double latitude;
    private double longitude;

    public Geocode() {
    }

    public Geocode(String text, double latitude, double longitude) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}