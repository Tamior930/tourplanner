package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class NavbarViewModel {
    private final TourService tourService;
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    private Tour selectedTour;

    public NavbarViewModel(TourService tourService) {
        this.tourService = tourService;
        
        // Subscribe to tour selection events
        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, event -> {
            selectedTour = (Tour) event.getData();
            tourSelected.set(selectedTour != null);
        });
    }

    public void createNewTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
    }

    public void editSelectedTour() {
        if (selectedTour != null) {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, selectedTour));
        }
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            tourService.deleteTour(selectedTour);
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_DELETED, selectedTour));
            selectedTour = null;
            tourSelected.set(false);
        }
    }

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Tour Planner Help");
        alert.setContentText("This application helps you plan and manage your tours.\n\n" +
                "- Create new tours with the 'New Tour' button\n" +
                "- Select a tour to view its details\n" +
                "- Edit or delete tours using the menu options\n" +
                "- Search for tours using the search bar");
        alert.showAndWait();
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Tour Planner");
        alert.setContentText("Version 1.0\n\nDeveloped by Team Eight\n\n" +
                "A JavaFX application for planning and managing tours.");
        alert.showAndWait();
    }

    public void exitApplication() {
        // Close the application
        Stage stage = (Stage) tourSelected.getBean();
        if (stage != null) {
            stage.close();
        }
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }
}
