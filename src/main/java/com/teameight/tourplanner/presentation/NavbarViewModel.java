package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
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
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    private Tour selectedTour;

    public NavbarViewModel(TourService tourService) {
        this.tourService = tourService;

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, event -> {
            selectedTour = (Tour) event.getData();
            tourSelected.set(selectedTour != null);
        });
    }

    public void createNewTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
        openTourForm();
    }

    public void editSelectedTour() {
        if (selectedTour != null) {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, selectedTour));
            openTourForm();
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
            formStage.setTitle("Tour");
            formStage.setScene(new Scene(formView));

            formStage.setMinWidth(500);
            formStage.setMinHeight(500);

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
                "- Create new tours with the 'Add Tour' button\n" +
                "- Select a tour to view its details\n" +
                "- Edit or delete tours using the menu options\n" +
                "- Search for tours using the search bar");
        alert.showAndWait();
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Tour Planner");
        alert.setContentText("Developed by Berkant and Ereza\n\n");
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
