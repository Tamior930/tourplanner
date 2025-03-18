package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NavbarViewModel {

    private final TourService tourService;
    private final StringProperty statusMessageProperty = new SimpleStringProperty("");
    private final BooleanProperty tourSelectedProperty = new SimpleBooleanProperty(false);
    private Tour selectedTour;

    public NavbarViewModel(TourService tourService) {
        this.tourService = tourService;
    }

    public void initialize() {
        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, this::handleTourSelected);
        EventBus.getInstance().subscribe(EventType.TOUR_CREATED, this::handleTourCreated);
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, this::handleTourDeleted);

        selectedTour = null;
        tourSelectedProperty.set(false);
    }

    private void handleTourSelected(Event<?> event) {
        if (event.getData() instanceof Tour) {
            selectedTour = (Tour) event.getData();
            tourSelectedProperty.set(true);
        }
    }

    private void handleTourCreated(Event<?> event) {
        updateStatus("Tour created successfully");
    }

    private void handleTourDeleted(Event<?> event) {
        selectedTour = null;
        tourSelectedProperty.set(false);
        updateStatus("Tour deleted successfully");
    }

    public void createNewTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
        updateStatus("Creating new tour...");
    }

    public void editTour() {
        if (selectedTour != null) {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, selectedTour));
            updateStatus("Editing tour: " + selectedTour.getName());
        } else {
            updateStatus("Please select a tour to edit");
        }
    }

    public void deleteTour() {
        if (selectedTour != null) {
            EventBus.getInstance().publish(new Event<>(EventType.CONFIRM_DELETE_TOUR, selectedTour));
        } else {
            updateStatus("Please select a tour to delete");
        }
    }

    public void exit() {
        Platform.exit();
    }

    public void showAbout() {
        updateStatus("Tour Planner - Version 1.0");
    }

    public void help() {
        updateStatus("Help documentation available at: help.tourplanner.com");
    }

    private void updateStatus(String message) {
        statusMessageProperty.set(message);
        EventBus.getInstance().publish(new Event<>(EventType.STATUS_UPDATED, message));
    }

    public StringProperty statusMessageProperty() {
        return statusMessageProperty;
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelectedProperty;
    }
} 