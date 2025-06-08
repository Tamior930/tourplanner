package com.teameight.tourplanner.model;

import com.fasterxml.jackson.databind.JsonNode;

public class RouteInfo {
    private final String distance;
    private final String time;
    private final JsonNode geoJson;

    public RouteInfo(String distance, String time, JsonNode geoJson) {
        this.distance = distance;
        this.time = time;
        this.geoJson = geoJson;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public JsonNode getGeoJson() {
        return geoJson;
    }
}
