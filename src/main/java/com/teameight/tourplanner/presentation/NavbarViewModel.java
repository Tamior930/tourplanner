package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class NavbarViewModel {
    private final TourService tourService;
    private final EventManager eventManager;

    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    private Tour selectedTour;

    public NavbarViewModel(TourService tourService, EventManager eventManager) {
        this.tourService = tourService;
        this.eventManager = eventManager;

        eventManager.subscribe(Events.TOUR_SELECTED, this::handleTourSelected);
    }

    private void handleTourSelected(String tourId) {
        if (tourId != null && !tourId.isEmpty()) {
            selectedTour = tourService.getTourById(tourId);
            tourSelected.set(selectedTour != null);
        } else {
            selectedTour = null;
            tourSelected.set(false);
        }
    }

    public void createNewTour() {
        eventManager.publish(Events.TOUR_ADDED, null);
        openTourForm();
    }

    public void editSelectedTour() {
        if (selectedTour != null) {
            eventManager.publish(Events.TOUR_UPDATED, selectedTour.getId());
            openTourForm();
        }
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            String tourId = selectedTour.getId();
            tourService.deleteTour(selectedTour);
            eventManager.publish(Events.TOUR_DELETED, tourId);
            selectedTour = null;
            tourSelected.set(false);
        }
    }

    public void exportMap() {
        if (selectedTour != null) {
            eventManager.publish(Events.MAP_EXPORT_CLICKED, selectedTour.getId());
        }
    }

    private void openTourForm() {
        try {
            Parent formView = FXMLDependencyInjector.load(
                    "components/tour-form.fxml",
                    Locale.ENGLISH
            );

            Stage formStage = new Stage();
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle(selectedTour != null ?
                    "Edit Tour: " + selectedTour.getName() :
                    "Create New Tour");

            formStage.setScene(new Scene(formView));
            formStage.setMinWidth(600);
            formStage.setMinHeight(500);
            formStage.centerOnScreen();

            formStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Could not open tour form");
        }
    }

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Tour Planner Help");
        alert.setContentText(
                "This application helps you plan and manage your tours.\n\n" +
                        "• Create new tours with the 'Add Tour' button\n" +
                        "• Select a tour to view its details\n" +
                        "• Edit or delete tours using the menu options\n" +
                        "• Search for tours using the search bar"
        );

        alert.showAndWait();
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Tour Planner");
        alert.setContentText("Version 1.0\nDeveloped by Berkant and Ereza");

        alert.showAndWait();
    }

    public void exitApplication() {
        System.exit(0);
    }

    // Get the property indicating whether a tour is selected
    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }
}
