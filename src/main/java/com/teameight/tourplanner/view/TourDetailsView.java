package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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
    public void initialize(URL location, ResourceBundle resources) {
        // Bind labels to view model properties
        tourNameLabel.textProperty().bind(viewModel.tourNameProperty());
        tourDescriptionLabel.textProperty().bind(viewModel.tourDescriptionProperty());
        tourOriginLabel.textProperty().bind(viewModel.tourOriginProperty());
        tourDestinationLabel.textProperty().bind(viewModel.tourDestinationProperty());
        tourTransportTypeLabel.textProperty().bind(viewModel.tourTransportTypeProperty());
        tourDistanceLabel.textProperty().bind(viewModel.tourDistanceProperty());
        tourEstimatedTimeLabel.textProperty().bind(viewModel.tourEstimatedTimeProperty());
        
        // Bind image view to view model property
        tourMapImageView.imageProperty().bind(viewModel.tourMapImageProperty());
    }
}
