package com.teameight.tourplanner.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.RouteInfo;
import com.teameight.tourplanner.provider.SnapshotProvider;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.ExporterService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.web.WebEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class MapViewModel {
    private static final Logger LOGGER = LogManager.getLogger(MapViewModel.class);

    private final EventManager eventManager;
    private final TourService tourService;
    private final MapService mapService;
    private final ExporterService exporterService;
    private final ObjectMapper objectMapper;
    private WebEngine webEngine;
    private SnapshotProvider snapshotProvider;

    public MapViewModel(EventManager eventManager, TourService tourService, MapService mapService, ExporterService exporterService) {
        this.eventManager = eventManager;
        this.tourService = tourService;
        this.mapService = mapService;
        this.exporterService = exporterService;
        this.objectMapper = new ObjectMapper();

        this.eventManager.subscribe(Events.TOUR_SELECTED, this::onTourSelected);
        this.eventManager.subscribe(Events.MAP_EXPORT_CLICKED, this::onMapExport);
    }

    public void init() {
        String html = getClass()
                .getResource("/com/teameight/tourplanner/map.html").toExternalForm();
        this.webEngine.load(html);
    }

    private void onTourSelected(String tourId) {
        if (tourId == null || tourId.isEmpty()) {
            webEngine.executeScript("clearRoute();");
            return;
        }

        var tour = tourService.getTourById(tourId);
        if (tour != null) {
            mapService.getDirections(tour.getOrigin(), tour.getDestination(), tour.getTransportType())
                    .ifPresent(this::updateMapRoute);
        }
    }

    private void updateMapRoute(RouteInfo routeInfo) {
        if (routeInfo == null || routeInfo.getGeoJson() == null) {
            LOGGER.warn("RouteInfo or its GeoJson is null. Cannot update map.");
            return;
        }

        if (webEngine != null) {
            try {
                String geoJsonString = objectMapper.writeValueAsString(routeInfo.getGeoJson());
                webEngine.executeScript("drawRoute(" + geoJsonString + ");");
            } catch (JsonProcessingException e) {
                LOGGER.error("Failed to serialize GeoJSON for map display", e);
            }
        }
    }

    private void onMapExport(String message) {
        if (snapshotProvider == null) {
            return;
        }

        snapshotProvider.requestSnapshot(writableImage -> {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            exporterService.export(bufferedImage, "tour_map");
        });
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void setSnapshotProvider(SnapshotProvider snapshotProvider) {
        this.snapshotProvider = snapshotProvider;
    }
}