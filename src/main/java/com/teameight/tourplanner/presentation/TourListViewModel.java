package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class TourListViewModel {
    private final TourService tourService;

    private final EventManager eventManager;

    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourListViewModel(SearchViewModel searchViewModel, TourService tourService, EventManager eventManager) {
        this.tourService = tourService;
        this.eventManager = eventManager;

        loadTours();

        eventManager.subscribe(Events.TOUR_ADDED, message -> loadTours());
        eventManager.subscribe(Events.TOUR_UPDATED, message -> loadTours());
        eventManager.subscribe(Events.TOUR_DELETED, message -> loadTours());

        // Listen for search events
        eventManager.subscribe(Events.SEARCH_TOURS, this::handleSearch);

        // Publish selection changes
        selectedTour.addListener((observable, oldValue, newValue) -> {
            String tourId = (newValue != null) ? newValue.getId() : null;
            eventManager.publish(Events.TOUR_SELECTED, tourId);
        });
    }

    private void handleSearch(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            loadTours();
        } else {
            tours.setAll(tourService.searchTours(searchQuery));
        }
    }

    private void loadTours() {
        tours.setAll(tourService.getAllTours());
    }

    public void addNewTour() {
        // Notify other components about adding a new tour
        eventManager.publish(Events.TOUR_ADDED, null);
        openTourForm();
    }

    private void openTourForm() {
        try {
            // Load the tour form
            Parent formView = FXMLDependencyInjector.load(
                    "components/tour-form.fxml",
                    Locale.ENGLISH
            );

            // Create and configure the stage
            Stage formStage = new Stage();
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle("Create New Tour");
            formStage.setScene(new Scene(formView));
            formStage.setMinWidth(600);
            formStage.setMinHeight(500);
            formStage.centerOnScreen();

            // Show the form
            formStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Failed to open tour form: " + e.getMessage());
        }
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public ObjectProperty<Tour> selectedTourProperty() {
        return selectedTour;
    }

    public void setSelectedTour(Tour tour) {
        selectedTour.set(tour);
    }
}
