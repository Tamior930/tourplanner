package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.presentation.TourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * View for the tour logs functionality.
 * Handles user interaction with the logs list and form.
 */
public class TourLogView implements Initializable {
    private final TourLogViewModel viewModel;
    private final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    // Log table components
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

    // Button components
    @FXML
    private HBox buttonContainer;
    @FXML
    private Button newLogButton;
    @FXML
    private Button editLogButton;
    @FXML
    private Button deleteLogButton;

    public TourLogView(TourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();
        setupBindings();
    }

    /**
     * Set up the table view columns and cell factories
     */
    private void setupTableView() {
        // Configure date/time column
        setupDateTimeColumn();

        // Configure basic columns
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Configure formatted columns
        setupTotalTimeColumn();
        setupRatingColumn();
        setupDistanceColumn();

        // Connect table to data source
        logTableView.setItems(viewModel.getTourLogs());

        // Handle selection changes
        logTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> viewModel.setSelectedLog(newVal)
        );
    }

    /**
     * Set up the date/time column with formatted display
     */
    private void setupDateTimeColumn() {
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        dateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : DATE_TIME_FORMAT.format(item));
            }
        });
    }

    /**
     * Set up the total time column with formatted display
     */
    private void setupTotalTimeColumn() {
        totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        totalTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item + " min");
            }
        });
    }

    /**
     * Set up the rating column with star display
     */
    private void setupRatingColumn() {
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
    }

    /**
     * Set up the distance column with formatted display
     */
    private void setupDistanceColumn() {
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        distanceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.1f km", item));
            }
        });
    }

    /**
     * Set up data bindings between view and view model
     */
    private void setupBindings() {
        // Button states
        editLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        deleteLogButton.disableProperty().bind(viewModel.logSelectedProperty().not());
        newLogButton.disableProperty().bind(viewModel.tourSelectedProperty().not());
    }

    /**
     * Handle button actions
     */
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
}
