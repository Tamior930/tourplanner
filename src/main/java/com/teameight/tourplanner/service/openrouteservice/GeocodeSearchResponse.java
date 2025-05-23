package com.teameight.tourplanner.service.openrouteservice;

import java.util.List;

public class GeocodeSearchResponse {

    private String type;
    private List<Feature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
} 