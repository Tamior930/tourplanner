package com.teameight.tourplanner.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teameight.tourplanner.model.Location;
import com.teameight.tourplanner.service.MapService;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class OpenRouteServiceMapImpl implements MapService {

    private static final String API_KEY = ""; // Add your API key here
    private static final String GEOCODE_SEARCH_URI = "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public OpenRouteServiceMapImpl() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Optional<Location> findLocation(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            String encodedLocation = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
            String uri = String.format(GEOCODE_SEARCH_URI, API_KEY, encodedLocation);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Error response from geocoding service: " + response.statusCode());
                return Optional.empty();
            }

            GeocodeResponse geocodeResponse = objectMapper.readValue(response.body(), GeocodeResponse.class);

            if (geocodeResponse.features == null || geocodeResponse.features.isEmpty()) {
                return Optional.empty();
            }

            Feature feature = geocodeResponse.features.get(0);
            if (feature.geometry == null || feature.geometry.coordinates == null || feature.geometry.coordinates.length < 2) {
                return Optional.empty();
            }

            // OpenRouteService returns coordinates as [longitude, latitude]
            return Optional.of(new Location(
                    locationName,
                    feature.geometry.coordinates[1], // latitude
                    feature.geometry.coordinates[0]  // longitude
            ));
        } catch (Exception e) {
            System.err.println("Error during geocoding: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Model classes for JSON deserialization
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GeocodeResponse {
        public List<Feature> features;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Feature {
        public Geometry geometry;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Geometry {
        public double[] coordinates;
    }
} 