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

    @FXML
    private Label distanceErrorLabel;

    @FXML
    private Label estimatedTimeErrorLabel;

    public TourFormView(TourFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formTitleLabel.textProperty().bind(viewModel.formTitleProperty());

        tourDistanceField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[\\d\\s\\.]*k?m?")) {
                return change;
            }
            return null;
        }));

        tourEstimatedTimeField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[\\d\\s]*h?[\\d\\s]*m?i?n?")) {
                return change;
            }
            return null;
        }));

        tourNameField.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescriptionArea.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourOriginField.textProperty().bindBidirectional(viewModel.tourOriginProperty());
        tourDestinationField.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
        tourDistanceField.textProperty().bindBidirectional(viewModel.tourDistanceProperty());
        tourEstimatedTimeField.textProperty().bindBidirectional(viewModel.tourEstimatedTimeProperty());

        nameErrorLabel.textProperty().bind(viewModel.nameErrorProperty());
        descriptionErrorLabel.textProperty().bind(viewModel.descriptionErrorProperty());
        originErrorLabel.textProperty().bind(viewModel.originErrorProperty());
        destinationErrorLabel.textProperty().bind(viewModel.destinationErrorProperty());
        distanceErrorLabel.textProperty().bind(viewModel.distanceErrorProperty());
        estimatedTimeErrorLabel.textProperty().bind(viewModel.estimatedTimeErrorProperty());

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
                return null;
            }
        });

        tourMapImageView.imageProperty().bind(viewModel.tourMapImageProperty());

        tourNameField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourDescriptionArea.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourOriginField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourDestinationField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourDistanceField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        tourEstimatedTimeField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());

        saveButton.disableProperty().bind(viewModel.formValidProperty().not());
    }

    private String formatTransportType(TransportType transportType) {
        String name = transportType.name().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    @FXML
    public void handleSave() {
        viewModel.validateForm();

        if (viewModel.formValidProperty().get()) {
            viewModel.saveTour();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
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
