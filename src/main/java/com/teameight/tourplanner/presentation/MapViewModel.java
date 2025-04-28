package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventListener;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Location;
import com.teameight.tourplanner.provider.SnapshotProvider;
import com.teameight.tourplanner.service.ExporterService;
import com.teameight.tourplanner.service.MapService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.web.WebEngine;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MapViewModel implements EventListener<Object> {
    
    private WebEngine webEngine;
    private SnapshotProvider snapshotProvider;
    private final MapService mapService;
    private final ExporterService exporterService;
    private final EventBus eventBus;
    
    private Location currentLocation;
    
    public MapViewModel(MapService mapService, ExporterService exporterService) {
        this.mapService = mapService;
        this.exporterService = exporterService;
        this.eventBus = EventBus.getInstance();
        
        eventBus.subscribe(EventType.MAP_LOCATION_CHANGED, this);
        eventBus.subscribe(EventType.MAP_EXPORT_REQUESTED, this);
    }
    
    public void initialize() {
        if (webEngine != null) {
            String mapUrl = getClass().getResource("/com/teameight/tourplanner/map.html").toExternalForm();
            webEngine.load(mapUrl);
        }
    }
    
    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
    
    public void setSnapshotProvider(SnapshotProvider snapshotProvider) {
        this.snapshotProvider = snapshotProvider;
    }
    
    public void searchLocation(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return;
        }
        
        mapService.findLocation(locationName).ifPresent(location -> {
            currentLocation = location;
            eventBus.publish(new Event<>(EventType.MAP_LOCATION_CHANGED, location));
        });
    }
    
    public void updateMapLocation(Location location) {
        if (webEngine != null && location != null) {
            webEngine.executeScript(String.format(
                "updateMapView(%f, %f, 13);", 
                location.getLatitude(), 
                location.getLongitude()
            ));
            
            webEngine.executeScript(String.format(
                "updateMarker(%f, %f, '%s');", 
                location.getLatitude(), 
                location.getLongitude(),
                location.getName()
            ));
        }
    }
    
    public void exportMap() {
        if (snapshotProvider != null) {
            snapshotProvider.requestSnapshot(writableImage -> {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
                
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String locationName = currentLocation != null ? 
                        currentLocation.getName().replaceAll("[^a-zA-Z0-9]", "_") : "map";
                String filename = "map_" + locationName + "_" + timestamp;
                
                exporterService.exportImage(bufferedImage, filename);
            });
        }
    }

    @Override
    public void onEvent(Event<Object> event) {
        if (event.getType() == EventType.MAP_LOCATION_CHANGED && event.getData() instanceof Location) {
            Location location = (Location) event.getData();
            currentLocation = location;
            updateMapLocation(location);
        } else if (event.getType() == EventType.MAP_EXPORT_REQUESTED) {
            exportMap();
        }
    }
} 