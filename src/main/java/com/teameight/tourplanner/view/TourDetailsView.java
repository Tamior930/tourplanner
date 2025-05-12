package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.TourDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsView implements Initializable {
    private final TourDetailsViewModel viewModel;

    @FXML
    private VBox detailsContainer;

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

    public TourDetailsView(TourDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupBindings();
    }

    private void setupBindings() {
        // Bind tour property values to labels
        tourNameLabel.textProperty().bind(viewModel.tourNameProperty());
        tourDescriptionLabel.textProperty().bind(viewModel.tourDescriptionProperty());
        tourOriginLabel.textProperty().bind(viewModel.tourOriginProperty());
        tourDestinationLabel.textProperty().bind(viewModel.tourDestinationProperty());
        tourTransportTypeLabel.textProperty().bind(viewModel.tourTransportTypeProperty());
        tourDistanceLabel.textProperty().bind(viewModel.tourDistanceProperty());
        tourEstimatedTimeLabel.textProperty().bind(viewModel.tourEstimatedTimeProperty());

        // Show/hide details based on selection state
        if (detailsContainer != null) {
            detailsContainer.visibleProperty().bind(viewModel.tourSelectedProperty());
            detailsContainer.managedProperty().bind(viewModel.tourSelectedProperty());
        }
    }
}
