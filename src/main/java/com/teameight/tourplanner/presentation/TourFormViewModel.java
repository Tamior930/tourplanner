package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class TourFormViewModel {
    private final TourService tourService;
    
    // Form fields
    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty tourOrigin = new SimpleStringProperty("");
    private final StringProperty tourDestination = new SimpleStringProperty("");
    private final ObjectProperty<TransportType> tourTransportType = new SimpleObjectProperty<>(TransportType.CAR);
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTime = new SimpleStringProperty("");
    private final ObjectProperty<Image> tourMapImage = new SimpleObjectProperty<>();
    
    // Error messages
    private final StringProperty nameError = new SimpleStringProperty("");
    private final StringProperty descriptionError = new SimpleStringProperty("");
    private final StringProperty originError = new SimpleStringProperty("");
    private final StringProperty destinationError = new SimpleStringProperty("");
    private final StringProperty distanceError = new SimpleStringProperty("");
    private final StringProperty estimatedTimeError = new SimpleStringProperty("");
    
    // Form state
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final StringProperty formTitle = new SimpleStringProperty("New Tour");
    private final ObservableList<TransportType> transportTypes = FXCollections.observableArrayList(
            Arrays.asList(TransportType.values())
    );
    
    private String tourId;
    private boolean isEditMode = false;

    public TourFormViewModel(TourService tourService) {
        this.tourService = tourService;
        
        // Subscribe to events
        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, event -> {
            resetForm();
            isEditMode = false;
            formTitle.set("New Tour");
        });
        
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, event -> {
            Tour tour = (Tour) event.getData();
            if (tour != null) {
                loadTourForEditing(tour);
                isEditMode = true;
                formTitle.set("Edit Tour: " + tour.getName());
            }
        });
        
        // Validate form whenever fields change
        tourName.addListener((observable, oldValue, newValue) -> validateForm());
        tourOrigin.addListener((observable, oldValue, newValue) -> validateForm());
        tourDestination.addListener((observable, oldValue, newValue) -> validateForm());
    }

    private void loadTourForEditing(Tour tour) {
        tourId = tour.getId();
        tourName.set(tour.getName());
        tourDescription.set(tour.getDescription());
        tourOrigin.set(tour.getOrigin());
        tourDestination.set(tour.getDestination());
        tourTransportType.set(tour.getTransportType());
        tourDistance.set(tour.getDistance());
        tourEstimatedTime.set(tour.getEstimatedTime());
        tourMapImage.set(tour.getMapImage());
        
        validateForm();
    }

    private void resetForm() {
        tourId = null;
        tourName.set("");
        tourDescription.set("");
        tourOrigin.set("");
        tourDestination.set("");
        tourTransportType.set(TransportType.CAR);
        tourDistance.set("");
        tourEstimatedTime.set("");
        tourMapImage.set(null);
        
        nameError.set("");
        descriptionError.set("");
        originError.set("");
        destinationError.set("");
        distanceError.set("");
        estimatedTimeError.set("");
        
        formValid.set(false);
    }

    private void validateForm() {
        boolean valid = true;
        
        // Validate name
        if (tourName.get() == null || tourName.get().trim().isEmpty()) {
            nameError.set("Name is required");
            valid = false;
        } else {
            nameError.set("");
        }
        
        // Validate origin
        if (tourOrigin.get() == null || tourOrigin.get().trim().isEmpty()) {
            originError.set("Origin is required");
            valid = false;
        } else {
            originError.set("");
        }
        
        // Validate destination
        if (tourDestination.get() == null || tourDestination.get().trim().isEmpty()) {
            destinationError.set("Destination is required");
            valid = false;
        } else {
            destinationError.set("");
        }
        
        formValid.set(valid);
    }

    public void generateMap() {
        if (tourOrigin.get() != null && !tourOrigin.get().isEmpty() &&
            tourDestination.get() != null && !tourDestination.get().isEmpty()) {
            
            // In a real implementation, this would call a map service
            // For now, we'll just set placeholder values
            tourDistance.set("10 km");
            tourEstimatedTime.set("30 min");
            
            // This would be replaced with actual map generation
            // tourMapImage.set(mapService.generateMap(tourOrigin.get(), tourDestination.get(), tourTransportType.get()));
        }
    }

    public boolean saveTour() {
        if (!formValid.get()) {
            return false;
        }
        
        Tour tour;
        if (isEditMode && tourId != null) {
            // Update existing tour
            tour = tourService.getTourById(tourId);
            if (tour != null) {
                updateTourFromForm(tour);
                tourService.updateTour(tour);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, tour));
            }
        } else {
            // Create new tour
            tour = createTourFromForm();
            tourService.addTour(tour);
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, tour));
        }
        
        return true;
    }

    private Tour createTourFromForm() {
        return new Tour(
                UUID.randomUUID().toString(),
                tourName.get(),
                tourDescription.get(),
                tourOrigin.get(),
                tourDestination.get(),
                tourTransportType.get(),
                tourDistance.get(),
                tourEstimatedTime.get(),
                tourMapImage.get()
        );
    }

    private void updateTourFromForm(Tour tour) {
        tour.setName(tourName.get());
        tour.setDescription(tourDescription.get());
        tour.setOrigin(tourOrigin.get());
        tour.setDestination(tourDestination.get());
        tour.setTransportType(tourTransportType.get());
        tour.setDistance(tourDistance.get());
        tour.setEstimatedTime(tourEstimatedTime.get());
        tour.setMapImage(tourMapImage.get());
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

    public ObjectProperty<TransportType> tourTransportTypeProperty() {
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

    public StringProperty nameErrorProperty() {
        return nameError;
    }

    public StringProperty descriptionErrorProperty() {
        return descriptionError;
    }

    public StringProperty originErrorProperty() {
        return originError;
    }

    public StringProperty destinationErrorProperty() {
        return destinationError;
    }

    public StringProperty distanceErrorProperty() {
        return distanceError;
    }

    public StringProperty estimatedTimeErrorProperty() {
        return estimatedTimeError;
    }

    public BooleanProperty formValidProperty() {
        return formValid;
    }

    public StringProperty formTitleProperty() {
        return formTitle;
    }

    public ObservableList<TransportType> getTransportTypes() {
        return transportTypes;
    }
}
