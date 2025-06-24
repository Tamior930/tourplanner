package com.teameight.tourplanner.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teameight.tourplanner.model.Geocode;
import com.teameight.tourplanner.model.RouteInfo;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.ConfigManager;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.openrouteservice.GeocodeSearchResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

public class OpenRouteServiceApi implements MapService {

    private static final Logger LOGGER = LogManager.getLogger(OpenRouteServiceApi.class);
    private static final String GEOCODE_SEARCH_URI = "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s";
    private static final String DIRECTIONS_URI = "https://api.openrouteservice.org/v2/directions/%s/geojson";
    private static final Map<TransportType, String> PROFILE_MAP = Map.of(
            TransportType.CAR, "driving-car",
            TransportType.BIKE, "cycling-regular",
            TransportType.BUS, "driving-car", // Fallback for bus
            TransportType.FOOT_WALKING, "foot-walking"
    );
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final ConfigManager configManager;

    public OpenRouteServiceApi(ConfigManager configManager) {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configManager = configManager;
    }

    @Override
    public Optional<Geocode> findGeocode(String text) {
        String uri = String.format(
                GEOCODE_SEARCH_URI,
                this.configManager.get("api.key"),
                text.replace(" ", "%20")
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                LOGGER.error("OpenRouteService API request failed with status code: {}", response.statusCode());
                return Optional.empty();
            }

            GeocodeSearchResponse geocodeSearchResponse = objectMapper.readValue(
                    response.body(), GeocodeSearchResponse.class
            );

            if (geocodeSearchResponse.getFeatures().isEmpty()) {
                LOGGER.info("No location found for search: '{}'", text);
                return Optional.empty();
            }

            // getFirst is only available in Java 21 =(
            double longitude = geocodeSearchResponse.getFeatures().get(0).getGeometry().getCoordinates()[0];
            double latitude = geocodeSearchResponse.getFeatures().get(0).getGeometry().getCoordinates()[1];

            Geocode geocode = new Geocode(text, latitude, longitude);

            LOGGER.info("Location found for '{}': [{}, {}]", text, geocode.getLatitude(), geocode.getLongitude());
            return Optional.of(geocode);
        } catch (Exception e) {
            LOGGER.error("Error connecting to OpenRouteService API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<RouteInfo> getDirections(String origin, String destination, TransportType transportType) {
        Optional<Geocode> originGeocode = findGeocode(origin);
        if (originGeocode.isEmpty()) {
            LOGGER.error("Could not find geocode for origin: {}", origin);
            return Optional.empty();
        }

        Optional<Geocode> destGeocode = findGeocode(destination);
        if (destGeocode.isEmpty()) {
            LOGGER.error("Could not find geocode for destination: {}", destination);
            return Optional.empty();
        }

        String profile = PROFILE_MAP.getOrDefault(transportType, "driving-car");
        String uri = String.format(DIRECTIONS_URI, profile);
        String apiKey = configManager.get("api.key");

        String requestBody = String.format("{\"coordinates\":[[%s,%s],[%s,%s]]}",
                originGeocode.get().getLongitude(), originGeocode.get().getLatitude(),
                destGeocode.get().getLongitude(), destGeocode.get().getLatitude());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOGGER.error("Directions API request failed with code {}: {}", response.statusCode(), response.body());
                return Optional.empty();
            }

            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode features = rootNode.path("features");

            if (features.isEmpty() || !features.isArray()) {
                LOGGER.warn("No route features found in API response for {} -> {}", origin, destination);
                return Optional.empty();
            }

            JsonNode firstFeature = features.get(0);
            JsonNode summary = firstFeature.path("properties").path("summary");
            double distanceInMeters = summary.path("distance").asDouble();
            double durationInSeconds = summary.path("duration").asDouble();

            String formattedDistance = formatDistance(distanceInMeters);
            String formattedTime = formatTime((int) durationInSeconds);
            JsonNode geoJsonGeometry = firstFeature.path("geometry");

            return Optional.of(new RouteInfo(formattedDistance, formattedTime, geoJsonGeometry));

        } catch (Exception e) {
            LOGGER.error("Error fetching directions from OpenRouteService API: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private String formatDistance(double meters) {
        if (meters < 1000) {
            return String.format("%.0f m", meters);
        } else {
            return String.format("%.2f km", meters / 1000.0);
        }
    }

    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            return String.format("%d h %d min", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }
}