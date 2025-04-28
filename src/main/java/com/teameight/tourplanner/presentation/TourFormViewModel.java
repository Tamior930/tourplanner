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

public class TourFormViewModel {
    private final TourService tourService;

    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty tourOrigin = new SimpleStringProperty("");
    private final StringProperty tourDestination = new SimpleStringProperty("");
    private final ObjectProperty<TransportType> tourTransportType = new SimpleObjectProperty<>(TransportType.CAR);
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTime = new SimpleStringProperty("");
    private final ObjectProperty<Image> tourMapImage = new SimpleObjectProperty<>();

    private final StringProperty nameError = new SimpleStringProperty("");
    private final StringProperty descriptionError = new SimpleStringProperty("");
    private final StringProperty originError = new SimpleStringProperty("");
    private final StringProperty destinationError = new SimpleStringProperty("");
    private final StringProperty distanceError = new SimpleStringProperty("");
    private final StringProperty estimatedTimeError = new SimpleStringProperty("");

    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final StringProperty formTitle = new SimpleStringProperty("New Tour");
    private final ObservableList<TransportType> transportTypes = FXCollections.observableArrayList(
            Arrays.asList(TransportType.values())
    );

    private String tourId;
    private boolean isEditMode = false;

    public TourFormViewModel(TourService tourService) {
        this.tourService = tourService;

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

        tourName.addListener((observable, oldValue, newValue) -> validateForm());
        tourOrigin.addListener((observable, oldValue, newValue) -> validateForm());
        tourDestination.addListener((observable, oldValue, newValue) -> validateForm());

        try {
            Image placeholderImage = new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png"));
            tourMapImage.set(placeholderImage);
        } catch (Exception e) {
            System.err.println("Error loading placeholder map: " + e.getMessage());
        }
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

        nameError.set("");
        descriptionError.set("");
        originError.set("");
        destinationError.set("");
        distanceError.set("");
        estimatedTimeError.set("");

        formValid.set(false);
    }

    public void validateForm() {
        boolean valid = true;

        if (tourName.get() == null || tourName.get().trim().isEmpty()) {
            nameError.set("Name is required");
            valid = false;
        } else {
            nameError.set("");
        }

        if (tourDescription.get() == null || tourDescription.get().trim().isEmpty()) {
            descriptionError.set("Description is required");
            valid = false;
        } else {
            descriptionError.set("");
        }

        if (tourOrigin.get() == null || tourOrigin.get().trim().isEmpty()) {
            originError.set("Origin is required");
            valid = false;
        } else {
            originError.set("");
        }

        if (tourDestination.get() == null || tourDestination.get().trim().isEmpty()) {
            destinationError.set("Destination is required");
            valid = false;
        } else {
            destinationError.set("");
        }

        String distance = tourDistance.get();
        if (distance == null || distance.trim().isEmpty()) {
            distanceError.set("Distance is required");
            valid = false;
        } else if (!distance.matches("\\d+(\\.\\d+)?\\s*(km)")) {
            distanceError.set("Format: 10 km, 10.5 km");
            valid = false;
        } else {
            distanceError.set("");
        }

        String time = tourEstimatedTime.get();
        if (time == null || time.trim().isEmpty()) {
            estimatedTimeError.set("Estimated time is required");
            valid = false;
        } else if (!time.matches("\\d+\\s*h(\\s*\\d+\\s*min)?")) {
            estimatedTimeError.set("Format: 2h, 2 h, 1h 30min");
            valid = false;
        } else {
            estimatedTimeError.set("");
        }

        formValid.set(valid);
    }

    public boolean saveTour() {
        if (!formValid.get()) {
            return false;
        }

        Tour tour;
        if (isEditMode && tourId != null) {
            tour = tourService.getTourById(tourId);
            if (tour != null) {
                updateTourFromForm(tour);
                tourService.updateTour(tour);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, tour));
            }
        } else {
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
                tourEstimatedTime.get()
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
