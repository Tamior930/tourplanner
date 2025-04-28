package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Location;

import java.util.Optional;

public interface MapService {

    Optional<Location> findLocation(String locationName);
} 