package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Geocode;

import java.util.Optional;

public interface MapService {

    Optional<Geocode> findGeocode(String text);
} 