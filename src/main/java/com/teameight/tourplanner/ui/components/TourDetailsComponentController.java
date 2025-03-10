package com.teameight.tourplanner.ui.components;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.TourDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsComponentController implements Initializable {
    
    @FXML
    private Label tourNameLabel;
    
    @FXML
    private Label tourDescriptionLabel;
    
    @FXML
    private Label tourOriginLabel;
    
    @FXML
    private Label tourDestinationLabel;
    
    @FXML
    private Label tourTransportTypeLabel;
    
    @FXML
    private Label tourDistanceLabel;
    
    @FXML
    private Label tourEstimatedTimeLabel;
    
    @FXML
    private ImageView tourMapImageView;
    
    private TourDetailsViewModel tourDetailsViewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Wird bei Bindung des ViewModels initialisiert
        
        // Für Tour-Auswahl-Ereignisse anmelden
        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, this::handleTourSelectedEvent);
    }
    
    // Binden der ViewModel an das Komponente
    public void bindViewModel(TourDetailsViewModel tourDetailsViewModel) {
        this.tourDetailsViewModel = tourDetailsViewModel;
        
        // ViewModel an die Komponente binden
        tourNameLabel.textProperty().bind(tourDetailsViewModel.tourNameProperty());
        tourDescriptionLabel.textProperty().bind(tourDetailsViewModel.tourDescriptionProperty());
        tourOriginLabel.textProperty().bind(tourDetailsViewModel.tourOriginProperty());
        tourDestinationLabel.textProperty().bind(tourDetailsViewModel.tourDestinationProperty());
        tourTransportTypeLabel.textProperty().bind(tourDetailsViewModel.tourTransportTypeProperty());
        tourDistanceLabel.textProperty().bind(tourDetailsViewModel.tourDistanceProperty());
        tourEstimatedTimeLabel.textProperty().bind(tourDetailsViewModel.tourEstimatedTimeProperty());
        tourMapImageView.imageProperty().bind(tourDetailsViewModel.tourMapImageProperty());
    }
    
    // Behandlung des Tour-Auswahl-Ereignisses
    private void handleTourSelectedEvent(Event<Tour> event) {
        Tour tour = event.getData();
        if (tour != null) {
            tourDetailsViewModel.updateTourDetails(
                    tour.getName(),
                    tour.getDescription(),
                    tour.getOrigin(),
                    tour.getDestination(),
                    tour.getTransportType(),
                    tour.getDistance(),
                    tour.getEstimatedTime(),
                    tour.getMapImage()
            );
        } else {
            tourDetailsViewModel.clearTourDetails();
        }
    }
} 