package com.example.simfur.navme;

public class POI {
    private double lat;
    private double lon;
    private String name;
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return this.lon;
    }
}
