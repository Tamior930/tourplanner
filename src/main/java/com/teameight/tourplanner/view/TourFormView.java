package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.presentation.TourFormViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class TourFormView implements Initializable {
    private final TourFormViewModel viewModel;

    @FXML
    private VBox rootContainer;
    
    @FXML
    private Label formTitleLabel;
    
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
    private Button generateMapButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label nameErrorLabel;
    
    @FXML
    private Label descriptionErrorLabel;
    
    @FXML
    private Label originErrorLabel;
    
    @FXML
    private Label destinationErrorLabel;

    public TourFormView(TourFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind form title
        formTitleLabel.textProperty().bind(viewModel.formTitleProperty());
        
        // Bind text fields to view model properties
        tourNameField.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescriptionArea.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourOriginField.textProperty().bindBidirectional(viewModel.tourOriginProperty());
        tourDestinationField.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
        tourDistanceField.textProperty().bindBidirectional(viewModel.tourDistanceProperty());
        tourEstimatedTimeField.textProperty().bindBidirectional(viewModel.tourEstimatedTimeProperty());
        
        // Bind error labels
        nameErrorLabel.textProperty().bind(viewModel.nameErrorProperty());
        descriptionErrorLabel.textProperty().bind(viewModel.descriptionErrorProperty());
        originErrorLabel.textProperty().bind(viewModel.originErrorProperty());
        
        // Set up transport type combo box
        tourTransportTypeCombo.setItems(viewModel.getTransportTypes());
        tourTransportTypeCombo.valueProperty().bindBidirectional(viewModel.tourTransportTypeProperty());
        tourTransportTypeCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(TransportType transportType) {
                if (transportType == null) {
                    return "";
                }
                return formatTransportType(transportType);
            }

            @Override
            public TransportType fromString(String string) {
                // Not needed for combo box
                return null;
            }
        });
        
        // Bind map image
        tourMapImageView.imageProperty().bind(viewModel.tourMapImageProperty());
        
        // Bind save button disable property
        saveButton.disableProperty().bind(viewModel.formValidProperty().not());
        
        // Make distance and estimated time fields read-only
        tourDistanceField.setEditable(false);
        tourEstimatedTimeField.setEditable(false);
    }

    private String formatTransportType(TransportType transportType) {
        String name = transportType.name().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    @FXML
    public void handleGenerateMap() {
        viewModel.generateMap();
    }

    @FXML
    public void handleSave() {
        if (viewModel.saveTour()) {
            closeForm();
        }
    }

    @FXML
    public void handleCancel() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) rootContainer.getScene().getWindow();
        stage.close();
    }
}
