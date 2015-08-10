package com.example.simfur.navme;

class POI {
    private double lat;
    private double lon;
    private int radius;
    private String name;
    private String text;
    private String tts;
    private int id;
    private boolean consumed = false;

    public void setName(String name) {this.name = name; }
    public String getName() { return this.name; }

    public void setText(String text) { this.text = text; }
    public String getText() { return this.text; }

    public void setTts(String tts) { this.tts = tts; }
    public String getTts() { return this.tts; }

    public void setId(int id) { this.id = id; }
    public int getId() { return this.id; }

    public void setLat(double lat) { this.lat = lat; }
    public double getLat() { return this.lat; }

    public void setLon(double lon) { this.lon = lon; }
    public double getLon() { return this.lon; }

    public void setRadius(int radius) { this.radius = radius; }
    public double getRadius() { return this.radius; }

    public void consume() { this.consumed = true; }
    public boolean isConsumed() { return this.consumed; }
}
