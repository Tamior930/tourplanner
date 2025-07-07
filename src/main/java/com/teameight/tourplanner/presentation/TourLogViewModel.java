package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.service.TourLogService;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public class TourLogViewModel {
    private static final Logger LOGGER = LogManager.getLogger(TourLogViewModel.class);
    private final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final TourLogService tourLogService;
    private final EventManager eventManager;
    private final TourLogFormViewModel tourLogFormViewModel;

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedLog = new SimpleObjectProperty<>();

    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private final ObservableList<Difficulty> difficultyLevels =
            FXCollections.observableArrayList(Arrays.asList(Difficulty.values()));

    private final BooleanProperty logSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);

    public TourLogViewModel(TourLogService tourLogService, TourService tourService, EventManager eventManager, TourLogFormViewModel tourLogFormViewModel) {
        this.tourLogService = tourLogService;
        this.eventManager = eventManager;
        this.tourLogFormViewModel = tourLogFormViewModel;

        eventManager.subscribe(Events.TOUR_SELECTED, tourId -> {
            // Clear previous selection
            selectedLog.set(null);
            logSelected.set(false);

            // Load the selected tour
            Tour tour = null;
            if (tourId != null && !tourId.isEmpty()) {
                tour = tourService.getTourById(tourId);
            }

            // Update state based on tour selection
            selectedTour.set(tour);
            tourSelected.set(tour != null);

            // Load logs if a tour is selected
            if (tour != null) {
                loadLogsForTour(tour.getId());
            } else {
                tourLogs.clear();
            }
        });

        // Listen for log added/updated events
        eventManager.subscribe(Events.TOUR_LOG_ADDED, logId -> {
            if (selectedTour.get() != null) {
                loadLogsForTour(selectedTour.get().getId());
            }
        });

        eventManager.subscribe(Events.TOUR_LOG_UPDATED, logId -> {
            if (selectedTour.get() != null) {
                loadLogsForTour(selectedTour.get().getId());
            }
        });

        // Initialize with empty state
        selectedLog.addListener((obs, oldVal, newVal) -> logSelected.set(newVal != null));
    }

    // Loads the logs for the selected tour
    private void loadLogsForTour(String tourId) {
        tourLogs.setAll(tourLogService.getLogsForTour(tourId));
    }

    // Show the form for adding a new log in a separate window
    public void showNewLogForm() {
        Tour tour = selectedTour.get();
        if (tour == null) return;

        try {
            // Prepare the view model for a new log
            tourLogFormViewModel.prepareForNewLog(tour);

            // Open the form window
            openLogFormWindow("New Tour Log");
        } catch (IOException e) {
            LOGGER.error("Failed to open log form: {}", e.getMessage());
        }
    }

    // Show the form for editing an existing log in a separate window
    public void showEditLogForm() {
        TourLog log = selectedLog.get();
        Tour tour = selectedTour.get();
        if (log == null || tour == null) return;

        try {
            // Prepare the view model for editing
            tourLogFormViewModel.prepareForEditLog(log, tour);

            // Open the form window
            openLogFormWindow("Edit Tour Log: " + DATE_TIME_FORMAT.format(log.getDateTime()));
        } catch (IOException e) {
            LOGGER.error("Failed to open log form: {}", e.getMessage());
        }
    }

    // Helper method to open the log form window
    private void openLogFormWindow(String title) throws IOException {
        // Load the tour log form
        Parent formView = FXMLDependencyInjector.load(
                "components/tour-log-form.fxml",
                Locale.ENGLISH
        );

        // Create and configure the stage
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle(title);
        formStage.setScene(new Scene(formView));
        formStage.setMinWidth(550);
        formStage.setMinHeight(550);
        formStage.centerOnScreen();

        // Show the form
        formStage.showAndWait();
    }

    // Delete the selected log
    public void deleteSelectedLog() {
        TourLog log = selectedLog.get();
        if (log == null) return;

        tourLogService.deleteLog(log);

        // Publish event
        eventManager.publish(Events.TOUR_LOG_DELETED, log.getId());

        // Reload logs
        if (selectedTour.get() != null) {
            loadLogsForTour(selectedTour.get().getId());
        }
    }

    // Set the selected log
    public void setSelectedLog(TourLog log) {
        selectedLog.set(log);
    }

    public ObservableList<TourLog> getTourLogs() {
        return tourLogs;
    }

    public BooleanProperty logSelectedProperty() {
        return logSelected;
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }
}
