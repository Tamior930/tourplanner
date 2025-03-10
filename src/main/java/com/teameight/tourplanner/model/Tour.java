package com.teameight.tourplanner.model;

import javafx.scene.image.Image;

/**
 * Modellklasse für eine Tour
 */
public class Tour {
    private String id;
    private String name;
    private String description;
    private String origin;
    private String destination;
    private TransportType transportType;
    private String distance;
    private String estimatedTime;
    private Image mapImage;
    
    // Standard Konstruktor
    public Tour() {
    }
    
    // Konstruktor mit allen Feldern
    public Tour(String id, String name, String description, String origin, String destination,
               TransportType transportType, String distance, String estimatedTime, Image mapImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.destination = destination;
        this.transportType = transportType;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.mapImage = mapImage;
    }
    
    // Getter und Setter
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public TransportType getTransportType() {
        return transportType;
    }
    
    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }
    
    public String getDistance() {
        return distance;
    }
    
    public void setDistance(String distance) {
        this.distance = distance;
    }
    
    public String getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    public Image getMapImage() {
        return mapImage;
    }
    
    public void setMapImage(Image mapImage) {
        this.mapImage = mapImage;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 