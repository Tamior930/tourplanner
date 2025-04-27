package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Location;

import java.util.Optional;

/**
 * Service for map-related operations like geocoding
 */
public interface MapService {
    /**
     * Find geographical coordinates for a location name
     * @param locationName The name of the location to geocode
     * @return Optional containing Location if found, empty otherwise
     */
    Optional<Location> findLocation(String locationName);
} 