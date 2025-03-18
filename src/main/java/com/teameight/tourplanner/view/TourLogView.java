package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.presentation.TourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TourLogView implements Initializable {
    private final TourLogViewModel viewModel;

    @FXML
    private VBox logContainer;

    @FXML
    private TableView<TourLog> logTableView;

    @FXML
    private TableColumn<TourLog, LocalDateTime> dateTimeColumn;
    
    @FXML
    private TableColumn<TourLog, String> commentColumn;

    @FXML
    private TableColumn<TourLog, Difficulty> difficultyColumn;

    @FXML
    private TableColumn<TourLog, Number> totalTimeColumn;

    @FXML
    private TableColumn<TourLog, Number> ratingColumn;

    @FXML
    private VBox logFormContainer;

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
    private Label dateErrorLabel;

    @FXML
    private Label timeErrorLabel;

    @FXML
    private Label totalTimeErrorLabel;

    @FXML
    private Label ratingErrorLabel;

    @FXML
    private Button newLogButton;

    @FXML
    private Button editLogButton;

    @FXML
    private Button deleteLogButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    public TourLogView(TourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table view
        logTableView.setItems(viewModel.getTourLogs());

        // Configure date column
        dateTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateTime()));
        dateTimeColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        
        // Configure comment column
        commentColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getComment()));
        commentColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        // Configure difficulty column
        difficultyColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDifficulty()));

        // Configure total time column
        totalTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getTotalTime()));
        totalTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int minutes = item.intValue();
                    int hours = minutes / 60;
                    int mins = minutes % 60;
                    setText(hours + "h " + mins + "min");
                }
            }
        });

        // Configure rating column
        ratingColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getRating()));
        ratingColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("★".repeat(item.intValue()));
                }
            }
        });

        // Bind selected log
        logTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.selectedLogProperty().set(newVal);
        });

        // Set up form fields
        dateField.textProperty().bindBidirectional(viewModel.logDateProperty());
        timeField.textProperty().bindBidirectional(viewModel.logTimeProperty());
        commentArea.textProperty().bindBidirectional(viewModel.logCommentProperty());
        totalTimeField.textProperty().bindBidirectional(viewModel.logTotalTimeProperty());

        // Set up difficulty combo box
        difficultyComboBox.setItems(viewModel.getDifficultyValues());
        difficultyComboBox.valueProperty().bindBidirectional(viewModel.logDifficultyProperty());
        difficultyComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Difficulty difficulty) {
                if (difficulty == null) return "";
                return difficulty.name().charAt(0) + difficulty.name().substring(1).toLowerCase();
            }

            @Override
            public Difficulty fromString(String string) {
                return null; // Not needed for combo box
            }
        });

        // Set up rating slider
        ratingSlider.setMin(1);
        ratingSlider.setMax(5);
        ratingSlider.setValue(3);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.valueProperty().bindBidirectional(viewModel.logRatingProperty());

        // Update rating label when slider changes
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int stars = newVal.intValue();
            ratingValueLabel.setText("★".repeat(stars));
        });

        // Bind error labels
        dateErrorLabel.textProperty().bind(viewModel.dateErrorProperty());
        timeErrorLabel.textProperty().bind(viewModel.timeErrorProperty());
        totalTimeErrorLabel.textProperty().bind(viewModel.totalTimeErrorProperty());
        ratingErrorLabel.textProperty().bind(viewModel.ratingErrorProperty());

        // Bind button states
        newLogButton.disableProperty().bind(viewModel.tourSelectedProperty().not());
        editLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        deleteLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        saveButton.disableProperty().bind(viewModel.formValidProperty().not());

        // Show/hide form based on edit mode
        logFormContainer.visibleProperty().bind(viewModel.editModeProperty());
        logFormContainer.managedProperty().bind(viewModel.editModeProperty());

        // Show/hide buttons based on edit mode
        newLogButton.visibleProperty().bind(viewModel.editModeProperty().not());
        editLogButton.visibleProperty().bind(viewModel.editModeProperty().not());
        deleteLogButton.visibleProperty().bind(viewModel.editModeProperty().not());
        saveButton.visibleProperty().bind(viewModel.editModeProperty());
        cancelButton.visibleProperty().bind(viewModel.editModeProperty());

        // Add text formatters for validation
        totalTimeField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    public void handleNewLog() {
        viewModel.createNewLog();
    }

    @FXML
    public void handleEditLog() {
        viewModel.editSelectedLog();
    }

    @FXML
    public void handleDeleteLog() {
        viewModel.deleteSelectedLog();
    }

    @FXML
    public void handleSave() {
        viewModel.saveLog();
    }

    @FXML
    public void handleCancel() {
        viewModel.cancelEdit();
    }
} 