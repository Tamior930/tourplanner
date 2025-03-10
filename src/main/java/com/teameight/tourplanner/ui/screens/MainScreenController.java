package com.teameight.tourplanner.ui.screens;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.SearchViewModel;
import com.teameight.tourplanner.presentation.TourDetailsViewModel;
import com.teameight.tourplanner.presentation.TourListViewModel;
import com.teameight.tourplanner.ui.components.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the main screen that coordinates component controllers
 */
public class MainScreenController implements Initializable {
    
    // FXML injected component controllers
    @FXML
    private NavbarComponentController navbarController;
    
    @FXML
    private SearchComponentController searchController;
    
    @FXML
    private TourListComponentController tourListController;
    
    @FXML
    private TourDetailsComponentController tourDetailsController;
    
    @FXML
    private StatusBarComponentController statusBarController;
    
    // ViewModels
    private final TourListViewModel tourListViewModel;
    private final SearchViewModel searchViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    
    /**
     * Constructor with dependency injection
     */
    public MainScreenController(TourListViewModel tourListViewModel, 
                               SearchViewModel searchViewModel,
                               TourDetailsViewModel tourDetailsViewModel) {
        this.tourListViewModel = tourListViewModel;
        this.searchViewModel = searchViewModel;
        this.tourDetailsViewModel = tourDetailsViewModel;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Connect the included components to their ViewModels
        if (navbarController != null) {
            navbarController.setEventListener(this::updateStatus);
        }
        
        if (searchController != null) {
            searchController.bindViewModel(searchViewModel);
        }
        
        if (tourListController != null) {
            tourListController.bindViewModel(tourListViewModel);
        }
        
        if (tourDetailsController != null) {
            tourDetailsController.bindViewModel(tourDetailsViewModel);
        }
        
        // Für Ereignisse anmelden
        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, this::handleTourAddedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, this::handleTourUpdatedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, this::handleTourDeletedEvent);
    }
    
    /**
     * Update status bar
     */
    private void updateStatus(String status) {
        // Statusleiste aktualisieren
        if (statusBarController != null) {
            statusBarController.updateStatus(status);
        }
    }
    
    /**
     * Handle tour added event
     */
    private void handleTourAddedEvent(Event<Tour> event) {
        // Tour-Hinzufügen-Ereignis behandeln
        showTourForm(null);
    }
    
    /**
     * Handle tour updated event
     */
    private void handleTourUpdatedEvent(Event<Tour> event) {
        // Tour-Aktualisierungs-Ereignis behandeln
        Tour selectedTour = tourListViewModel.getSelectedTour();
        if (selectedTour != null) {
            showTourForm(selectedTour);
        } else {
            showAlert("No Tour Selected", "Please select a tour to edit.");
        }
    }
    
    /**
     * Handle tour deleted event
     */
    private void handleTourDeletedEvent(Event<Tour> event) {
        // Tour-Lösch-Ereignis behandeln
        Tour selectedTour = tourListViewModel.getSelectedTour();
        if (selectedTour != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Tour");
            alert.setHeaderText("Delete Tour: " + selectedTour.getName());
            alert.setContentText("Are you sure you want to delete this tour?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tourListViewModel.deleteTour(selectedTour);
                updateStatus("Tour deleted: " + selectedTour.getName());
            }
        } else {
            showAlert("No Tour Selected", "Please select a tour to delete.");
        }
    }
    
    /**
     * Show the tour form dialog
     */
    private void showTourForm(Tour tour) {
        // Tourformular-Dialog anzeigen
        // Create a new stage for the form
        Stage formStage = new Stage();
        formStage.setTitle(tour == null ? "Create New Tour" : "Edit Tour");
        formStage.initModality(Modality.APPLICATION_MODAL);
        
        // Create the form component
        TourFormComponent formComponent = new TourFormComponent();
        
        // Set the tour if editing
        if (tour != null) {
            formComponent.setTour(tour);
        }
        
        // Set callbacks
        formComponent.setOnSave(savedTour -> {
            if (tour == null) {
                // Adding a new tour
                tourListViewModel.addTour(savedTour);
                updateStatus("Tour created: " + savedTour.getName());
            } else {
                // Updating an existing tour
                tourListViewModel.updateTour(savedTour);
                updateStatus("Tour updated: " + savedTour.getName());
            }
            formStage.close();
        });
        
        formComponent.setOnCancel(() -> formStage.close());
        
        // Create scene and show the stage
        Scene scene = new Scene(new BorderPane(formComponent), 500, 400);
        formStage.setScene(scene);
        formStage.showAndWait();
    }
    
    /**
     * Show an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 