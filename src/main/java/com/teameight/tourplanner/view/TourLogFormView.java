package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.presentation.TourLogFormViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View for the tour log form in a separate window.
 */
public class TourLogFormView implements Initializable {
    private final TourLogFormViewModel viewModel;

    // Root container
    @FXML
    private VBox rootContainer;

    // Form title
    @FXML
    private Label formTitleLabel;

    // Form fields
    @FXML
    private TextField dateField;
    @FXML
    private TextField timeField;
    @FXML
    private TextArea commentArea;
    @FXML
    private ComboBox<Difficulty> difficultyComboBox;
    @FXML
    private TextField totalTimeField;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label ratingValueLabel;
    @FXML
    private TextField distanceField;

    // Form controls
    @FXML
    private Label errorMessageLabel;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public TourLogFormView(TourLogFormViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFormControls();
        setupBindings();
        setupFieldChangeListeners();

        // Initialize validation
        viewModel.validateForm();
    }

    // Set up the form controls and their formatting
    private void setupFormControls() {
        // Configure the difficulty dropdown
        difficultyComboBox.setItems(viewModel.getDifficultyLevels());
        difficultyComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Difficulty difficulty) {
                if (difficulty == null) return "";
                return switch (difficulty) {
                    case EASY -> "Easy";
                    case MEDIUM -> "Medium";
                    case HARD -> "Hard";
                };
            }

            @Override
            public Difficulty fromString(String string) {
                return switch (string) {
                    case "Easy" -> Difficulty.EASY;
                    case "Medium" -> Difficulty.MEDIUM;
                    case "Hard" -> Difficulty.HARD;
                    default -> Difficulty.MEDIUM;
                };
            }
        });

        // Configure the rating slider
        ratingSlider.setMin(1);
        ratingSlider.setMax(5);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);
    }

    // Set up data bindings between view and view model
    private void setupBindings() {
        formTitleLabel.textProperty().bind(viewModel.formTitleProperty());

        dateField.textProperty().bindBidirectional(viewModel.dateTextProperty());
        timeField.textProperty().bindBidirectional(viewModel.timeTextProperty());
        commentArea.textProperty().bindBidirectional(viewModel.commentTextProperty());
        difficultyComboBox.valueProperty().bindBidirectional(viewModel.difficultyValueProperty());
        totalTimeField.textProperty().bindBidirectional(viewModel.totalTimeTextProperty());
        ratingSlider.valueProperty().bindBidirectional(viewModel.ratingValueProperty());
        ratingValueLabel.textProperty().bind(viewModel.ratingStarsProperty());
        distanceField.textProperty().bindBidirectional(viewModel.distanceTextProperty());

        errorMessageLabel.textProperty().bind(viewModel.errorMessageProperty());

        saveButton.disableProperty().bind(viewModel.formValidProperty().not());
    }

    // Set up field change listeners for validation
    private void setupFieldChangeListeners() {
        // Validate form when any field changes
        dateField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        timeField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        totalTimeField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
        distanceField.textProperty().addListener((obs, oldVal, newVal) -> viewModel.validateForm());
    }

    // Handle the save button action
    @FXML
    public void handleSave() {
        if (viewModel.saveLog()) {
            closeForm();
        }
    }

    // Handle the cancel button action
    @FXML
    public void handleCancel() {
        closeForm();
    }

    // Close the form window
    private void closeForm() {
        Stage stage = (Stage) rootContainer.getScene().getWindow();
        stage.close();
    }
} 