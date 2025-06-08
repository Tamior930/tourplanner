package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.presentation.TourFormViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorMessageLabel;

    public TourFormView(TourFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up bindings between UI elements and view model
        setupBindings();

        // Set up field change listeners for validation
        setupFieldChangeListeners();

        // Initialize form
        viewModel.validateForm();
    }

    private void setupBindings() {
        // Bind form fields
        formTitleLabel.textProperty().bind(viewModel.formTitleProperty());
        tourNameField.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescriptionArea.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourOriginField.textProperty().bindBidirectional(viewModel.tourOriginProperty());
        tourDestinationField.textProperty().bindBidirectional(viewModel.tourDestinationProperty());

        // Bind error message
        errorMessageLabel.textProperty().bind(viewModel.errorMessageProperty());

        // Bind save button state
        saveButton.disableProperty().bind(viewModel.formValidProperty().not());

        // Set up transport type combo box
        tourTransportTypeCombo.setItems(viewModel.getTransportTypes());
        tourTransportTypeCombo.valueProperty().bindBidirectional(viewModel.tourTransportTypeProperty());
    }

    private void setupFieldChangeListeners() {
        // Validate form when any field changes
        tourNameField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourOriginField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourDestinationField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
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