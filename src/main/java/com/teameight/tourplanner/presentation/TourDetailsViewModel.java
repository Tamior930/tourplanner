package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Location;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class TourDetailsViewModel {
    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty tourOrigin = new SimpleStringProperty("");
    private final StringProperty tourDestination = new SimpleStringProperty("");
    private final StringProperty tourTransportType = new SimpleStringProperty("");
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTime = new SimpleStringProperty("");
    private final ObjectProperty<Image> tourMapImage = new SimpleObjectProperty<>();
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    
    private final EventBus eventBus;

    public TourDetailsViewModel() {
        this.eventBus = EventBus.getInstance();
        
        eventBus.subscribe(EventType.TOUR_SELECTED, event -> {
            Tour tour = (Tour) event.getData();
            if (tour != null) {
                updateTourDetails(tour);
                tourSelected.set(true);
                
                // When a tour is selected, update the map location to show the destination
                if (tour.getDestination() != null && !tour.getDestination().isEmpty()) {
                    updateMapLocation(tour.getDestination());
                }
            } else {
                clearTourDetails();
                tourSelected.set(false);
            }
        });
    }
    
    private void updateMapLocation(String locationName) {
        // Create a temporary location with default coordinates (will be updated by MapViewModel)
        Location location = new Location(locationName, 0, 0);
        
        // Publish event to update the map location
        eventBus.publish(new Event<>(EventType.MAP_LOCATION_CHANGED, location));
    }

    private void updateTourDetails(Tour tour) {
        tourName.set(tour.getName());
        tourDescription.set(tour.getDescription());
        tourOrigin.set(tour.getOrigin());
        tourDestination.set(tour.getDestination());
        tourTransportType.set(formatTransportType(tour.getTransportType()));
        tourDistance.set(tour.getDistance());
        tourEstimatedTime.set(tour.getEstimatedTime());
        // tourMapImage.set(tour.getMapImage());
    }

    private void clearTourDetails() {
        tourName.set("");
        tourDescription.set("");
        tourOrigin.set("");
        tourDestination.set("");
        tourTransportType.set("");
        tourDistance.set("");
        tourEstimatedTime.set("");
        // tourMapImage.set(null);
    }

    private String formatTransportType(TransportType transportType) {
        if (transportType == null) {
            return "";
        }

        String name = transportType.name().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public StringProperty tourNameProperty() {
        return tourName;
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescription;
    }

    public StringProperty tourOriginProperty() {
        return tourOrigin;
    }

    public StringProperty tourDestinationProperty() {
        return tourDestination;
    }

    public StringProperty tourTransportTypeProperty() {
        return tourTransportType;
    }

    public StringProperty tourDistanceProperty() {
        return tourDistance;
    }

    public StringProperty tourEstimatedTimeProperty() {
        return tourEstimatedTime;
    }

    public ObjectProperty<Image> tourMapImageProperty() {
        return tourMapImage;
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }
}
