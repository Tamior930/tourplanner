package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Geocode;
import com.teameight.tourplanner.provider.SnapshotProvider;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.ExporterService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.web.WebEngine;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class MapViewModel {

    private final EventManager eventManager;
    private final TourService tourService;
    private final MapService mapService;
    private final ExporterService exporterService;
    private WebEngine webEngine;
    private SnapshotProvider snapshotProvider;

    public MapViewModel(EventManager eventManager, TourService tourService, MapService mapService, ExporterService exporterService) {
        this.eventManager = eventManager;
        this.tourService = tourService;
        this.mapService = mapService;
        this.exporterService = exporterService;

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
            return;
        }

        var tour = tourService.getTourById(tourId);
        if (tour != null) {
            String origin = tour.getOrigin();
            if (origin != null && !origin.isEmpty()) {
                Optional<Geocode> geocode = mapService.findGeocode(origin);
                geocode.ifPresent(this::updateMapLocation);
            }
        }
    }

    private void updateMapLocation(Geocode geocode) {
        if (geocode == null) {
            return;
        }

        if (webEngine != null) {
            webEngine.executeScript(
                    String.format("map.setView([%s, %s], 13);",
                            geocode.getLatitude(),
                            geocode.getLongitude())
            );
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