package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.UUID;

public class TourFormViewModel {
    private final TourService tourService;
    private final EventManager eventManager;

    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty tourOrigin = new SimpleStringProperty("");
    private final StringProperty tourDestination = new SimpleStringProperty("");
    private final ObjectProperty<TransportType> tourTransportType = new SimpleObjectProperty<>(TransportType.CAR);
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTime = new SimpleStringProperty("");

    private final StringProperty errorMessage = new SimpleStringProperty("");

    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final StringProperty formTitle = new SimpleStringProperty("New Tour");
    private final ObservableList<TransportType> transportTypes = FXCollections.observableArrayList(
            Arrays.asList(TransportType.values())
    );

    private String tourId;
    private boolean isEditMode = false;

    public TourFormViewModel(TourService tourService, EventManager eventManager) {
        this.tourService = tourService;
        this.eventManager = eventManager;

        // Reset form when a tour is added
        eventManager.subscribe(Events.TOUR_ADDED, message -> {
            resetForm();
            isEditMode = false;
            formTitle.set("New Tour");
        });

        // Load tour for editing when requested
        eventManager.subscribe(Events.TOUR_UPDATED, tourId -> {
            if (tourId != null && !tourId.isEmpty()) {
                Tour tour = tourService.getTourById(tourId);
                if (tour != null) {
                    loadTourForEditing(tour);
                    isEditMode = true;
                    formTitle.set("Edit Tour: " + tour.getName());
                }
            }
        });
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

        clearErrorMessage();
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
        clearErrorMessage();
        formValid.set(false);
    }

    public void validateForm() {
        // Clear any previous error message
        clearErrorMessage();

        // Check for missing required fields
        if (tourName.get() == null || tourName.get().trim().isEmpty()) {
            errorMessage.set("Name is required");
            formValid.set(false);
            return;
        }

        if (tourOrigin.get() == null || tourOrigin.get().trim().isEmpty()) {
            errorMessage.set("Origin is required");
            formValid.set(false);
            return;
        }

        if (tourDestination.get() == null || tourDestination.get().trim().isEmpty()) {
            errorMessage.set("Destination is required");
            formValid.set(false);
            return;
        }

        // All validations passed
        formValid.set(true);
    }

    public void clearErrorMessage() {
        errorMessage.set("");
    }

    public boolean saveTour() {
        validateForm();

        if (!formValid.get()) {
            return false;
        }

        Tour tour;
        if (isEditMode && tourId != null) {
            tour = tourService.getTourById(tourId);
            if (tour != null) {
                updateTourFromForm(tour);
                tourService.updateTour(tour);
                eventManager.publish(Events.TOUR_UPDATED, tour.getId());
            }
        } else {
            tour = createTourFromForm();
            tourService.addTour(tour);
            eventManager.publish(Events.TOUR_ADDED, tour.getId());
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

    public StringProperty errorMessageProperty() {
        return errorMessage;
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
