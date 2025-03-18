package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.presentation.TourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TourLogView implements Initializable {
    private final TourLogViewModel viewModel;

    @FXML
    private TableView<TourLog> logTableView;

    @FXML
    private TableColumn<TourLog, LocalDateTime> dateTimeColumn;

    @FXML
    private TableColumn<TourLog, String> commentColumn;

    @FXML
    private TableColumn<TourLog, Difficulty> difficultyColumn;

    @FXML
    private TableColumn<TourLog, Integer> totalTimeColumn;

    @FXML
    private TableColumn<TourLog, Integer> ratingColumn;

    @FXML
    private TableColumn<TourLog, Double> distanceColumn;

    @FXML
    private HBox buttonContainer;

    @FXML
    private Button newLogButton;

    @FXML
    private Button editLogButton;

    @FXML
    private Button deleteLogButton;

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
    private TextField distanceField;

    @FXML
    private Label distanceErrorLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    public TourLogView(TourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
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

        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        totalTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " min");
                }
            }
        });

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    StringBuilder stars = new StringBuilder();
                    for (int i = 0; i < item; i++) {
                        stars.append("★");
                    }
                    setText(stars.toString());
                }
            }
        });

        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        distanceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f km", item));
                }
            }
        });

        logTableView.setItems(viewModel.getTourLogs());

        logTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.setSelectedLog(newVal);
        });

        editLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        deleteLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        newLogButton.disableProperty().bind(viewModel.tourSelectedProperty().not());

        buttonContainer.visibleProperty().bind(viewModel.formVisibleProperty().not());
        buttonContainer.managedProperty().bind(viewModel.formVisibleProperty().not());

        logFormContainer.visibleProperty().bind(viewModel.formVisibleProperty());
        logFormContainer.managedProperty().bind(viewModel.formVisibleProperty());

        dateField.textProperty().bindBidirectional(viewModel.dateTextProperty());
        timeField.textProperty().bindBidirectional(viewModel.timeTextProperty());
        commentArea.textProperty().bindBidirectional(viewModel.commentTextProperty());

        difficultyComboBox.setItems(viewModel.getDifficultyLevels());
        difficultyComboBox.valueProperty().bindBidirectional(viewModel.difficultyValueProperty());
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

        totalTimeField.textProperty().bindBidirectional(viewModel.totalTimeTextProperty());

        ratingSlider.setMin(1);
        ratingSlider.setMax(5);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.valueProperty().bindBidirectional(viewModel.ratingValueProperty());

        ratingValueLabel.textProperty().bind(viewModel.ratingStarsProperty());

        dateErrorLabel.textProperty().bind(viewModel.dateErrorProperty());
        timeErrorLabel.textProperty().bind(viewModel.timeErrorProperty());
        totalTimeErrorLabel.textProperty().bind(viewModel.totalTimeErrorProperty());
        ratingErrorLabel.textProperty().bind(viewModel.ratingErrorProperty());

        distanceField.textProperty().bindBidirectional(viewModel.distanceTextProperty());
        distanceErrorLabel.textProperty().bind(viewModel.distanceErrorProperty());

        saveButton.disableProperty().bind(viewModel.formValidProperty().not());
    }

    @FXML
    public void handleNewLog() {
        viewModel.showNewLogForm();
    }

    @FXML
    public void handleEditLog() {
        viewModel.showEditLogForm();
    }

    @FXML
    public void handleDeleteLog() {
        viewModel.deleteSelectedLog();
    }

    @FXML
    public void handleSave() {
        if (viewModel.saveLog()) {
            logTableView.refresh();
        }
    }

    @FXML
    public void handleCancel() {
        viewModel.hideForm();
    }
}
