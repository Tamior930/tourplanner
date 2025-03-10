package com.teameight.tourplanner.ui.components;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.TourListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourListComponentController implements Initializable {
    
    @FXML
    private ListView<Tour> tourListView;
    
    @FXML
    private Button addTourButton;
    
    private TourListViewModel tourListViewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Wird bei Bindung des ViewModels initialisiert
        
        // Für Ereignisse anmelden
        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, this::handleTourAddedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, this::handleTourUpdatedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, this::handleTourDeletedEvent);
    }
    
    // Binden der ViewModel an das Komponente
    public void bindViewModel(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
        
        // Tourliste an ViewModel binden
        tourListView.setItems(tourListViewModel.tourListProperty());
        
        // Zellfactory für Tourliste setzen
        tourListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tour item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        
        // Auswahl an ViewModel binden
        tourListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tourListViewModel.setSelectedTour(newValue);
            
            // Tour-Auswahl-Ereignis veröffentlichen
            if (newValue != null) {
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_SELECTED, newValue));
            }
        });
    }
    
    // Handle add tour button click
    @FXML
    private void handleAddTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
    }
    
    // Handle tour added event
    private void handleTourAddedEvent(Event<Tour> event) {
        // Dies wird vom MainScreenController behandelt
    }
    
    // Handle tour updated event
    private void handleTourUpdatedEvent(Event<Tour> event) {
        Tour selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            // Show alert that no tour is selected
        }
    }
    
    // Handle tour deleted event
    private void handleTourDeletedEvent(Event<Tour> event) {
        Tour selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {
            // Show alert that no tour is selected
        }
    }
} 