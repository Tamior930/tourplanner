package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Geocode;
import com.teameight.tourplanner.model.RouteInfo;
import com.teameight.tourplanner.model.TransportType;

import java.util.Optional;

public interface MapService {

    Optional<Geocode> findGeocode(String text);

    Optional<RouteInfo> getDirections(String origin, String destination, TransportType transportType);
} 