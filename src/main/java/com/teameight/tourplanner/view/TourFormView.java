package com.teameight.tourplanner.view;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.presentation.TourFormViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TourFormView implements Initializable {

    private final TourFormViewModel viewModel;

    @FXML
    private TextField tourNameField;

    @FXML
    private TextArea tourDescriptionArea;

    @FXML
    private TextField tourOriginField;

    @FXML
    private TextField tourDestinationField;

    @FXML
    private ComboBox<TransportType> tourTransportTypeCombo;

    @FXML
    private TextField tourDistanceField;

    @FXML
    private TextField tourEstimatedTimeField;

    @FXML
    private ImageView tourMapImageView;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label formTitleLabel;

    @FXML
    private VBox rootContainer;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private Label originErrorLabel;

    @FXML
    private Label destinationErrorLabel;

    @FXML
    private Label distanceErrorLabel;

    @FXML
    private Label estimatedTimeErrorLabel;

    public TourFormView(TourFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public VBox getRootContainer() {
        return rootContainer;
    }

    @FXML
    private void handleGenerateMap() {
        if (tourOriginField.getText().isEmpty() || tourDestinationField.getText().isEmpty()) {
            EventBus.getInstance().publish(new Event<>(EventType.STATUS_UPDATED, 
                    "Please enter both origin and destination to generate a map"));
            return;
        }
        
        Image placeholderImage = new Image(getClass().getResourceAsStream(
                "/com/teameight/tourplanner/images/map-placeholder.png"));
        tourMapImageView.setImage(placeholderImage);
        viewModel.tourMapImageProperty().set(placeholderImage);
        
        EventBus.getInstance().publish(new Event<>(EventType.STATUS_UPDATED, 
                "Map generated for route from " + tourOriginField.getText() + 
                " to " + tourDestinationField.getText()));
    }

    @FXML
    private void handleSave() {
        viewModel.saveTour();
    }

    @FXML
    private void handleCancel() {
        viewModel.cancelForm();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tourNameField.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescriptionArea.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourOriginField.textProperty().bindBidirectional(viewModel.tourOriginProperty());
        tourDestinationField.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
        tourTransportTypeCombo.valueProperty().bindBidirectional(viewModel.tourTransportTypeProperty());
        tourDistanceField.textProperty().bindBidirectional(viewModel.tourDistanceProperty());
        tourEstimatedTimeField.textProperty().bindBidirectional(viewModel.tourEstimatedTimeProperty());
        tourMapImageView.imageProperty().bind(viewModel.tourMapImageProperty());

        nameErrorLabel.textProperty().bind(viewModel.nameErrorProperty());
        descriptionErrorLabel.textProperty().bind(viewModel.descriptionErrorProperty());
        originErrorLabel.textProperty().bind(viewModel.originErrorProperty());
        destinationErrorLabel.textProperty().bind(viewModel.destinationErrorProperty());
        distanceErrorLabel.textProperty().bind(viewModel.distanceErrorProperty());
        estimatedTimeErrorLabel.textProperty().bind(viewModel.estimatedTimeErrorProperty());

        tourTransportTypeCombo.setItems(viewModel.transportTypesProperty());

        saveButton.disableProperty().bind(viewModel.formValidProperty().not());

        formTitleLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> viewModel.editModeProperty().get() ? "Edit Tour" : "Create New Tour",
                        viewModel.editModeProperty()
                )
        );

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED_FOR_EDIT, this::handleTourSelectedForEdit);

        viewModel.initialize();
    }

    private void handleTourSelectedForEdit(Event<?> event) {
        if (event.getData() instanceof Tour) {
            viewModel.loadTour((Tour) event.getData());
        }
    }

    public void loadTourForEditing(Tour tour) {
        viewModel.loadTour(tour);
    }

    public void prepareForNewTour() {
        viewModel.resetForm();
    }
} 