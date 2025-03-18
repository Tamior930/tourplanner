package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.service.TourLogService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TourLogViewModel {
    private final TourLogService tourLogService;

    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    private final ObjectProperty<TourLog> selectedLog = new SimpleObjectProperty<>();
    private final StringProperty logDate = new SimpleStringProperty("");
    private final StringProperty logTime = new SimpleStringProperty("");
    private final StringProperty logComment = new SimpleStringProperty("");
    private final ObjectProperty<Difficulty> logDifficulty = new SimpleObjectProperty<>(Difficulty.MEDIUM);
    private final StringProperty logTotalTime = new SimpleStringProperty("");
    private final IntegerProperty logRating = new SimpleIntegerProperty(3);

    private final StringProperty dateError = new SimpleStringProperty("");
    private final StringProperty timeError = new SimpleStringProperty("");
    private final StringProperty totalTimeError = new SimpleStringProperty("");
    private final StringProperty ratingError = new SimpleStringProperty("");

    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty logSelected = new SimpleBooleanProperty(false);

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private Tour selectedTour;

    public TourLogViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, event -> {
            selectedTour = (Tour) event.getData();
            if (selectedTour != null) {
                loadLogsForTour(selectedTour.getId());
                tourSelected.set(true);
            } else {
                tourLogs.clear();
                tourSelected.set(false);
            }
            selectedLog.set(null);
            logSelected.set(false);
            editMode.set(false);
            resetForm();
        });

        selectedLog.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFormFromLog(newValue);
                logSelected.set(true);
            } else {
                logSelected.set(false);
            }
        });

        logDate.addListener((observable, oldValue, newValue) -> validateForm());
        logTime.addListener((observable, oldValue, newValue) -> validateForm());
        logTotalTime.addListener((observable, oldValue, newValue) -> validateForm());
        logRating.addListener((observable, oldValue, newValue) -> validateForm());
    }

    private void loadLogsForTour(String tourId) {
        tourLogs.setAll(tourLogService.getLogsForTour(tourId));
    }

    private void updateFormFromLog(TourLog log) {
        logDate.set(log.getDateTime().format(dateFormatter));
        logTime.set(log.getDateTime().format(timeFormatter));
        logComment.set(log.getComment());
        logDifficulty.set(log.getDifficulty());
        logTotalTime.set(String.valueOf(log.getTotalTime()) + " min");
        logRating.set(log.getRating());

        validateForm();
    }

    public void validateForm() {
        boolean valid = true;

        if (logDate.get() == null || logDate.get().trim().isEmpty()) {
            dateError.set("Date is required");
            valid = false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                formatter.parse(logDate.get());
                dateError.set("");
            } catch (DateTimeParseException e) {
                dateError.set("Invalid date format (DD.MM.YYYY)");
                valid = false;
            }
        }

        if (logTime.get() == null || logTime.get().trim().isEmpty()) {
            timeError.set("Time is required");
            valid = false;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                formatter.parse(logTime.get());
                timeError.set("");
            } catch (DateTimeParseException e) {
                timeError.set("Invalid time format (HH:MM)");
                valid = false;
            }
        }

        String time = logTotalTime.get();
        if (time == null || time.trim().isEmpty()) {
            totalTimeError.set("Total time is required");
            valid = false;
        } else if (!time.matches("\\d+\\s*(min)")) {
            totalTimeError.set("Format: 30 min");
            valid = false;
        } else {
            totalTimeError.set("");
        }

        if (logRating.get() < 1 || logRating.get() > 5) {
            ratingError.set("Rating must be between 1 and 5");
            valid = false;
        } else {
            ratingError.set("");
        }

        formValid.set(valid);
    }

    public void createNewLog() {
        if (selectedTour == null) {
            return;
        }

        TourLog newLog = TourLog.createNew(selectedTour.getId());

        updateFormFromLog(newLog);

        editMode.set(true);
        selectedLog.set(null);
    }

    public void editSelectedLog() {
        if (selectedLog.get() != null) {
            editMode.set(true);
        }
    }

    public void saveLog() {
        if (!formValid.get()) {
            return;
        }

        try {
            LocalDateTime dateTime = parseDateTime(logDate.get(), logTime.get());
            
            String timeStr = logTotalTime.get().trim();
            int minutes;
            if (timeStr.endsWith("min")) {
                timeStr = timeStr.substring(0, timeStr.indexOf("min")).trim();
            }
            minutes = Integer.parseInt(timeStr);

            if (selectedLog.get() == null) {
                TourLog newLog = new TourLog(
                        null,
                        selectedTour.getId(),
                        dateTime,
                        logComment.get(),
                        logDifficulty.get(),
                        minutes,
                        logRating.get()
                );
                
                tourLogService.addLog(newLog);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_ADDED, newLog));
            } else {
                TourLog updatedLog = new TourLog(
                        selectedLog.get().getId(),
                        selectedTour.getId(),
                        dateTime,
                        logComment.get(),
                        logDifficulty.get(),
                        minutes,
                        logRating.get()
                );
                
                tourLogService.updateLog(updatedLog);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_UPDATED, updatedLog));
            }
            
            loadLogsForTour(selectedTour.getId());
            editMode.set(false);
            selectedLog.set(null);
            
        } catch (Exception e) {
            System.err.println("Error saving log: " + e.getMessage());
        }
    }

    private LocalDateTime parseDateTime(String dateStr, String timeStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        String day = dateStr.split("\\.")[0];
        String month = dateStr.split("\\.")[1];
        String year = dateStr.split("\\.")[2];
        
        String hour = timeStr.split(":")[0];
        String minute = timeStr.split(":")[1];
        
        String isoDateTime = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00";
        return LocalDateTime.parse(isoDateTime);
    }

    public void cancelEdit() {
        editMode.set(false);
        if (selectedLog.get() != null) {
            updateFormFromLog(selectedLog.get());
        } else {
            resetForm();
        }
    }

    public void deleteSelectedLog() {
        if (selectedLog.get() != null) {
            TourLog log = selectedLog.get();
            tourLogService.deleteLog(log);
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_DELETED, log));

            // Reload logs
            loadLogsForTour(selectedTour.getId());

            // Reset selection
            selectedLog.set(null);
            resetForm();
        }
    }

    private void resetForm() {
        logDate.set(LocalDateTime.now().format(dateFormatter));
        logTime.set(LocalDateTime.now().format(timeFormatter));
        logComment.set("");
        logDifficulty.set(Difficulty.MEDIUM);
        logTotalTime.set("0 min");
        logRating.set(3);

        dateError.set("");
        timeError.set("");
        totalTimeError.set("");
        ratingError.set("");
    }

    // Getters for properties
    public ObservableList<TourLog> getTourLogs() {
        return tourLogs;
    }

    public ObjectProperty<TourLog> selectedLogProperty() {
        return selectedLog;
    }

    public StringProperty logDateProperty() {
        return logDate;
    }

    public StringProperty logTimeProperty() {
        return logTime;
    }

    public StringProperty logCommentProperty() {
        return logComment;
    }

    public ObjectProperty<Difficulty> logDifficultyProperty() {
        return logDifficulty;
    }

    public StringProperty logTotalTimeProperty() {
        return logTotalTime;
    }

    public IntegerProperty logRatingProperty() {
        return logRating;
    }

    public StringProperty dateErrorProperty() {
        return dateError;
    }

    public StringProperty timeErrorProperty() {
        return timeError;
    }

    public StringProperty totalTimeErrorProperty() {
        return totalTimeError;
    }

    public StringProperty ratingErrorProperty() {
        return ratingError;
    }

    public BooleanProperty formValidProperty() {
        return formValid;
    }

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }

    public BooleanProperty logSelectedProperty() {
        return logSelected;
    }

    public ObservableList<Difficulty> getDifficultyValues() {
        return FXCollections.observableArrayList(Difficulty.values());
    }
}