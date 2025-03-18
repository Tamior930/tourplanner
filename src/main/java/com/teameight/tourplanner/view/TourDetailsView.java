package com.teameight.tourplanner.view;

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

public class TourDetailsView implements Initializable {

    private final TourDetailsViewModel viewModel;

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

    public TourDetailsView(TourDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tourNameLabel.textProperty().bind(viewModel.tourNameProperty());
        tourDescriptionLabel.textProperty().bind(viewModel.tourDescriptionProperty());
        tourOriginLabel.textProperty().bind(viewModel.tourOriginProperty());
        tourDestinationLabel.textProperty().bind(viewModel.tourDestinationProperty());
        tourTransportTypeLabel.textProperty().bind(viewModel.tourTransportTypeProperty());
        tourDistanceLabel.textProperty().bind(viewModel.tourDistanceProperty());
        tourEstimatedTimeLabel.textProperty().bind(viewModel.tourEstimatedTimeProperty());
        tourMapImageView.imageProperty().bind(viewModel.tourMapImageProperty());

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, this::handleTourSelectedEvent);

        viewModel.initialize();
    }

    private void handleTourSelectedEvent(Event<Tour> event) {
        Tour tour = event.getData();
        if (tour != null) {
            viewModel.updateTourDetails(
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
            viewModel.clearTourDetails();
        }
    }
} 