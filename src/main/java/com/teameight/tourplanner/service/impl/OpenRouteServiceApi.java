package com.teameight.tourplanner.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teameight.tourplanner.model.Geocode;
import com.teameight.tourplanner.service.ConfigManager;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.openrouteservice.GeocodeSearchResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class OpenRouteServiceApi implements MapService {

    private static final Logger LOGGER = LogManager.getLogger(OpenRouteServiceApi.class);
    private static final String GEOCODE_SEARCH_URI = "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s";

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

            Geocode geocode = new Geocode();
            geocode.setText(text);
            // getFirst is only available in Java 21 =(
            geocode.setLongitude(geocodeSearchResponse.getFeatures().get(0).getGeometry().getCoordinates()[0]);
            geocode.setLatitude(geocodeSearchResponse.getFeatures().get(0).getGeometry().getCoordinates()[1]);

            LOGGER.info("Location found for '{}': [{}, {}]", text, geocode.getLatitude(), geocode.getLongitude());
            return Optional.of(geocode);
        } catch (Exception e) {
            LOGGER.error("Error connecting to OpenRouteService API: {}", e.getMessage());
            return Optional.empty();
        }
    }
} 