package com.teameight.tourplanner.model;

// Enum für verschiedene Transportmittel für Touren
public enum TransportType {
    CAR("Car"),
    BICYCLE("Bicycle"),
    PUBLIC_TRANSPORT("Public Transport"),
    WALKING("Walking"),
    PLANE("Plane"),
    TRAIN("Train"),
    BOAT("Boat");
    
    private final String displayName;
    
    TransportType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 