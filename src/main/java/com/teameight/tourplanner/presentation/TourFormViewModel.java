package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

public class TourFormViewModel {

    private final TourService tourService;

    private final BooleanProperty editModeProperty = new SimpleBooleanProperty(false);

    private final StringProperty tourNameProperty = new SimpleStringProperty("");
    private final StringProperty tourDescriptionProperty = new SimpleStringProperty("");
    private final StringProperty tourOriginProperty = new SimpleStringProperty("");
    private final StringProperty tourDestinationProperty = new SimpleStringProperty("");
    private final ObjectProperty<TransportType> tourTransportTypeProperty = new SimpleObjectProperty<>(TransportType.CAR);
    private final StringProperty tourDistanceProperty = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTimeProperty = new SimpleStringProperty("");
    private final ObjectProperty<Image> tourMapImageProperty = new SimpleObjectProperty<>();
    private final ListProperty<TransportType> transportTypesProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(TransportType.values())
    );
    private final BooleanProperty formValidProperty = new SimpleBooleanProperty(false);
    private Tour originalTour;

    private final StringProperty nameErrorProperty = new SimpleStringProperty("");
    private final StringProperty descriptionErrorProperty = new SimpleStringProperty("");
    private final StringProperty originErrorProperty = new SimpleStringProperty("");
    private final StringProperty destinationErrorProperty = new SimpleStringProperty("");
    private final StringProperty distanceErrorProperty = new SimpleStringProperty("");
    private final StringProperty estimatedTimeErrorProperty = new SimpleStringProperty("");

    public TourFormViewModel(TourService tourService) {
        this.tourService = tourService;

        formValidProperty.bind(
                tourNameProperty.isNotEmpty()
                        .and(tourDescriptionProperty.isNotEmpty())
                        .and(tourOriginProperty.isNotEmpty())
                        .and(tourDestinationProperty.isNotEmpty())
                        .and(tourTransportTypeProperty.isNotNull())
                        .and(tourDistanceProperty.isNotEmpty())
                        .and(tourEstimatedTimeProperty.isNotEmpty())
                        .and(tourMapImageProperty.isNotNull())
        );
        
        tourNameProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                nameErrorProperty.set("Tour name is required");
            } else {
                nameErrorProperty.set("");
            }
        });
        
        tourDescriptionProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                descriptionErrorProperty.set("Description is required");
            } else {
                descriptionErrorProperty.set("");
            }
        });
        
        tourOriginProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                originErrorProperty.set("Origin is required");
            } else {
                originErrorProperty.set("");
            }
        });
        
        tourDestinationProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                destinationErrorProperty.set("Destination is required");
            } else {
                destinationErrorProperty.set("");
            }
        });
        
        tourDistanceProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                distanceErrorProperty.set("Distance is required");
            } else {
                distanceErrorProperty.set("");
            }
        });
        
        tourEstimatedTimeProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                estimatedTimeErrorProperty.set("Estimated time is required");
            } else {
                estimatedTimeErrorProperty.set("");
            }
        });
    }

    public void initialize() {
        resetForm();

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED_FOR_EDIT, this::handleTourSelectedForEdit);
    }

    private void handleTourSelectedForEdit(Event<?> event) {
        if (event.getData() instanceof Tour) {
            loadTour((Tour) event.getData());
        }
    }

    public void loadTour(Tour tour) {
        originalTour = tour;
        editModeProperty.set(true);

        tourNameProperty.set(tour.getName());
        tourDescriptionProperty.set(tour.getDescription());
        tourOriginProperty.set(tour.getOrigin());
        tourDestinationProperty.set(tour.getDestination());
        tourTransportTypeProperty.set(tour.getTransportType());
        tourDistanceProperty.set(tour.getDistance());
        tourEstimatedTimeProperty.set(tour.getEstimatedTime());
        tourMapImageProperty.set(tour.getMapImage());
    }

    public void resetForm() {
        originalTour = null;
        editModeProperty.set(false);

        tourNameProperty.set("");
        tourDescriptionProperty.set("");
        tourOriginProperty.set("");
        tourDestinationProperty.set("");
        tourTransportTypeProperty.set(TransportType.CAR);
        tourDistanceProperty.set("");
        tourEstimatedTimeProperty.set("");

        tourMapImageProperty.set(new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png")));
    }

    public void saveTour() {
        try {
            if (editModeProperty.get() && originalTour != null) {
                Tour updatedTour = new Tour(
                        originalTour.getId(),
                        tourNameProperty.get(),
                        tourDescriptionProperty.get(),
                        tourOriginProperty.get(),
                        tourDestinationProperty.get(),
                        tourTransportTypeProperty.get(),
                        tourDistanceProperty.get(),
                        tourEstimatedTimeProperty.get(),
                        tourMapImageProperty.get()
                );
    
                tourService.updateTour(originalTour, updatedTour);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, originalTour));
            } else {
                Tour newTour = new Tour(
                        java.util.UUID.randomUUID().toString(),
                        tourNameProperty.get(),
                        tourDescriptionProperty.get(),
                        tourOriginProperty.get(),
                        tourDestinationProperty.get(),
                        tourTransportTypeProperty.get(),
                        tourDistanceProperty.get(),
                        tourEstimatedTimeProperty.get(),
                        tourMapImageProperty.get()
                );
    
                tourService.createTour(newTour);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_CREATED, newTour));
            }
    
            cancelForm();
    
        } catch (Exception e) {
            EventBus.getInstance().publish(new Event<>(EventType.STATUS_UPDATED, "Error saving tour: " + e.getMessage()));
        }
    }

    public void cancelForm() {
        resetForm();
        EventBus.getInstance().publish(new Event<>(EventType.CLOSE_TOUR_FORM, null));
    }

    public StringProperty tourNameProperty() {
        return tourNameProperty;
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescriptionProperty;
    }

    public StringProperty tourOriginProperty() {
        return tourOriginProperty;
    }

    public StringProperty tourDestinationProperty() {
        return tourDestinationProperty;
    }

    public ObjectProperty<TransportType> tourTransportTypeProperty() {
        return tourTransportTypeProperty;
    }

    public StringProperty tourDistanceProperty() {
        return tourDistanceProperty;
    }

    public StringProperty tourEstimatedTimeProperty() {
        return tourEstimatedTimeProperty;
    }

    public ObjectProperty<Image> tourMapImageProperty() {
        return tourMapImageProperty;
    }

    public ListProperty<TransportType> transportTypesProperty() {
        return transportTypesProperty;
    }

    public BooleanProperty formValidProperty() {
        return formValidProperty;
    }

    public BooleanProperty editModeProperty() {
        return editModeProperty;
    }

    public StringProperty nameErrorProperty() {
        return nameErrorProperty;
    }

    public StringProperty descriptionErrorProperty() {
        return descriptionErrorProperty;
    }

    public StringProperty originErrorProperty() {
        return originErrorProperty;
    }

    public StringProperty destinationErrorProperty() {
        return destinationErrorProperty;
    }

    public StringProperty distanceErrorProperty() {
        return distanceErrorProperty;
    }

    public StringProperty estimatedTimeErrorProperty() {
        return estimatedTimeErrorProperty;
    }
} 