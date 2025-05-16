package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.service.TourLogService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

public class TourLogFormViewModel {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final TourLogService tourLogService;
    private final EventManager eventManager;

    private final ObjectProperty<Tour> tour = new SimpleObjectProperty<>();

    private final StringProperty dateText = new SimpleStringProperty("");
    private final StringProperty timeText = new SimpleStringProperty("");
    private final StringProperty commentText = new SimpleStringProperty("");
    private final ObjectProperty<Difficulty> difficultyValue = new SimpleObjectProperty<>(Difficulty.MEDIUM);
    private final StringProperty distanceText = new SimpleStringProperty("");
    private final StringProperty totalTimeText = new SimpleStringProperty("");
    private final IntegerProperty ratingValue = new SimpleIntegerProperty(3);
    private final StringProperty ratingStars = new SimpleStringProperty("★★★");

    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);

    private final StringProperty formTitle = new SimpleStringProperty("New Tour Log");
    private final ObservableList<Difficulty> difficultyLevels =
            FXCollections.observableArrayList(Arrays.asList(Difficulty.values()));

    private String logId;
    private boolean isEditMode = false;

    public TourLogFormViewModel(TourLogService tourLogService, EventManager eventManager) {
        this.tourLogService = tourLogService;
        this.eventManager = eventManager;

        // Initialize rating stars display
        ratingValue.addListener((obs, oldVal, newVal) -> updateRatingStars(newVal.intValue()));
    }

    // Prepare the form for creating a new log for the given tour
    public void prepareForNewLog(Tour tour) {
        this.tour.set(tour);
        resetForm();
        isEditMode = false;
        formTitle.set("New Tour Log");
    }

    // Prepare the form for editing an existing log
    public void prepareForEditLog(TourLog log, Tour tour) {
        this.tour.set(tour);
        resetForm();
        isEditMode = true;

        // Populate form with log data
        logId = log.getId();
        LocalDateTime dateTime = log.getDateTime();
        dateText.set(dateTime.toLocalDate().format(DATE_FORMAT));
        timeText.set(dateTime.toLocalTime().format(TIME_FORMAT));
        commentText.set(log.getComment());
        difficultyValue.set(log.getDifficulty());
        distanceText.set(String.valueOf(log.getDistance()));
        totalTimeText.set(String.valueOf(log.getTotalTime()));
        ratingValue.set(log.getRating());

        formTitle.set("Edit Tour Log");
        validateForm();
    }

    // Reset form fields to default values
    private void resetForm() {
        logId = null;
        dateText.set("");
        timeText.set("");
        commentText.set("");
        difficultyValue.set(Difficulty.MEDIUM);
        distanceText.set("");
        totalTimeText.set("");
        ratingValue.set(3);
        updateRatingStars(3);

        // Clear validation state
        errorMessage.set("");
        formValid.set(false);
    }

    // Update the rating stars display based on the rating value
    private void updateRatingStars(int rating) {
        ratingStars.set("★".repeat(Math.max(0, rating)));
    }

    // Validate the form fields
    public void validateForm() {
        // Clear previous error message
        errorMessage.set("");

        try {
            // Validate date
            if (dateText.get() == null || dateText.get().trim().isEmpty()) {
                errorMessage.set("Date is required");
                formValid.set(false);
                return;
            }
            LocalDate.parse(dateText.get(), DATE_FORMAT);

            // Validate time
            if (timeText.get() == null || timeText.get().trim().isEmpty()) {
                errorMessage.set("Time is required");
                formValid.set(false);
                return;
            }
            LocalTime.parse(timeText.get(), TIME_FORMAT);

            // Validate distance
            if (distanceText.get() == null || distanceText.get().trim().isEmpty()) {
                errorMessage.set("Distance is required");
                formValid.set(false);
                return;
            }
            double distance = Double.parseDouble(distanceText.get());
            if (distance <= 0) {
                errorMessage.set("Distance must be greater than 0");
                formValid.set(false);
                return;
            }

            // Validate total time
            if (totalTimeText.get() == null || totalTimeText.get().trim().isEmpty()) {
                errorMessage.set("Total time is required");
                formValid.set(false);
                return;
            }
            int totalTime = Integer.parseInt(totalTimeText.get());
            if (totalTime <= 0) {
                errorMessage.set("Total time must be greater than 0");
                formValid.set(false);
                return;
            }

            // All validations passed
            formValid.set(true);
        } catch (Exception e) {
            errorMessage.set("Invalid input: " + e.getMessage());
            formValid.set(false);
        }
    }

    // Save the tour log
    public boolean saveLog() {
        validateForm();

        if (!formValid.get()) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(dateText.get(), DATE_FORMAT);
            LocalTime time = LocalTime.parse(timeText.get(), TIME_FORMAT);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            double distance = 0.0;
            if (distanceText.get() != null && !distanceText.get().trim().isEmpty()) {
                distance = Double.parseDouble(distanceText.get());
            }

            int totalTime = Integer.parseInt(totalTimeText.get());

            if (isEditMode && logId != null) {
                // Update existing log
                TourLog log = tourLogService.getLogById(logId);
                if (log != null) {
                    log.setDateTime(dateTime);
                    log.setComment(commentText.get());
                    log.setDifficulty(difficultyValue.get());
                    log.setDistance(distance);
                    log.setTotalTime(totalTime);
                    log.setRating(ratingValue.get());

                    tourLogService.updateLog(log);
                    eventManager.publish(Events.TOUR_LOG_UPDATED, log.getId());
                }
            } else {
                // Create new log
                TourLog newLog = new TourLog(
                        UUID.randomUUID().toString(),
                        tour.get().getId(),
                        dateTime,
                        commentText.get(),
                        difficultyValue.get(),
                        distance,
                        totalTime,
                        ratingValue.get()
                );
                tourLogService.addLog(newLog);
                eventManager.publish(Events.TOUR_LOG_ADDED, newLog.getId());
            }

            return true;
        } catch (Exception e) {
            errorMessage.set("Failed to save log: " + e.getMessage());
            return false;
        }
    }

    // Property getters
    public StringProperty dateTextProperty() {
        return dateText;
    }

    public StringProperty timeTextProperty() {
        return timeText;
    }

    public StringProperty commentTextProperty() {
        return commentText;
    }

    public ObjectProperty<Difficulty> difficultyValueProperty() {
        return difficultyValue;
    }

    public StringProperty distanceTextProperty() {
        return distanceText;
    }

    public StringProperty totalTimeTextProperty() {
        return totalTimeText;
    }

    public IntegerProperty ratingValueProperty() {
        return ratingValue;
    }

    public StringProperty ratingStarsProperty() {
        return ratingStars;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public BooleanProperty formValidProperty() {
        return formValid;
    }

    public StringProperty formTitleProperty() {
        return formTitle;
    }

    public ObservableList<Difficulty> getDifficultyLevels() {
        return difficultyLevels;
    }
} 