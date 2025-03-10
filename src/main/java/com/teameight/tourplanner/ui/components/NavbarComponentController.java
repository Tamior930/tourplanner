package com.teameight.tourplanner.ui.components;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class NavbarComponentController implements Initializable {
    
    private Consumer<String> statusUpdateListener;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    // Setzen eines Listeners für Status-Updates
    public void setEventListener(Consumer<String> statusUpdateListener) {
        this.statusUpdateListener = statusUpdateListener;
    }
    
    // Menüpunkt für neue Tour verarbeiten
    @FXML
    private void handleNewTour() {
        // Publish event to create a new tour
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
        updateStatus("Creating new tour...");
    }
    
    // Menüpunkt für bearbeitete Tour verarbeiten
    @FXML
    private void handleEditTour() {
        // Publish event to edit the selected tour
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, null));
        updateStatus("Editing tour...");
    }
    
    // Menüpunkt für gelöschte Tour verarbeiten
    @FXML
    private void handleDeleteTour() {
        // Publish event to delete the selected tour
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_DELETED, null));
        updateStatus("Deleting tour...");
    }
    
    // Menüpunkt für Beenden verarbeiten
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    // Menüpunkt für Informationen verarbeiten
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Tour Planner");
        alert.setHeaderText("Tour Planner Application");
        alert.setContentText("A JavaFX application for planning tours.\nDeveloped by Team Eight.");
        alert.showAndWait();
    }
    
    // Statusleiste aktualisieren
    private void updateStatus(String status) {
        if (statusUpdateListener != null) {
            statusUpdateListener.accept(status);
        }
    }
} 