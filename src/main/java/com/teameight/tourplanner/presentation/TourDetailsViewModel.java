package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {
    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty tourOrigin = new SimpleStringProperty("");
    private final StringProperty tourDestination = new SimpleStringProperty("");
    private final StringProperty tourTransportType = new SimpleStringProperty("");
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTime = new SimpleStringProperty("");
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);

    private final TourService tourService;
    private Tour currentTour;

    public TourDetailsViewModel(EventManager eventManager, TourService tourService) {
        this.tourService = tourService;

        // Subscribe to tour selection events
        eventManager.subscribe(Events.TOUR_SELECTED, this::handleTourSelected);
    }

    private void handleTourSelected(String tourId) {
        if (tourId != null && !tourId.isEmpty()) {
            // Load the selected tour
            Tour tour = tourService.getTourById(tourId);
            if (tour != null) {
                displayTour(tour);
                return;
            }
        }

        // If we get here, either no tour was selected or the tour wasn't found
        clearDisplay();
    }

    private void displayTour(Tour tour) {
        currentTour = tour;

        // Update all display properties
        tourName.set(tour.getName());
        tourDescription.set(tour.getDescription());
        tourOrigin.set(tour.getOrigin());
        tourDestination.set(tour.getDestination());
        tourTransportType.set(formatTransportType(tour.getTransportType()));
        tourDistance.set(tour.getDistance());
        tourEstimatedTime.set(tour.getEstimatedTime());

        // Update state
        tourSelected.set(true);
    }

    private void clearDisplay() {
        currentTour = null;

        // Clear all display properties
        tourName.set("");
        tourDescription.set("");
        tourOrigin.set("");
        tourDestination.set("");
        tourTransportType.set("");
        tourDistance.set("");
        tourEstimatedTime.set("");

        // Update state
        tourSelected.set(false);
    }

    private String formatTransportType(TransportType transportType) {
        if (transportType == null) return "";

        String name = transportType.name().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    // Property getters
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

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }
}
