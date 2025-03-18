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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.UUID;

public class TourLogViewModel {
    private final TourLogService tourLogService;

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private final ObjectProperty<TourLog> selectedLog = new SimpleObjectProperty<>();

    private final ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private final ObservableList<Difficulty> difficultyLevels = FXCollections.observableArrayList(
            Arrays.asList(Difficulty.values())
    );

    private final StringProperty dateText = new SimpleStringProperty("");
    private final StringProperty timeText = new SimpleStringProperty("");
    private final StringProperty commentText = new SimpleStringProperty("");
    private final ObjectProperty<Difficulty> difficultyValue = new SimpleObjectProperty<>(Difficulty.MEDIUM);
    private final StringProperty totalTimeText = new SimpleStringProperty("");
    private final IntegerProperty ratingValue = new SimpleIntegerProperty(3);
    private final StringProperty ratingStars = new SimpleStringProperty("★★★");

    private final StringProperty dateError = new SimpleStringProperty("");
    private final StringProperty timeError = new SimpleStringProperty("");
    private final StringProperty totalTimeError = new SimpleStringProperty("");
    private final StringProperty ratingError = new SimpleStringProperty("");

    private final BooleanProperty formVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty formValid = new SimpleBooleanProperty(false);
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty logSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final StringProperty distanceText = new SimpleStringProperty("");
    private final StringProperty distanceError = new SimpleStringProperty("");
    private String logId;

    public TourLogViewModel(TourLogService tourLogService) {
        this.tourLogService = tourLogService;

        EventBus.getInstance().subscribe(EventType.TOUR_SELECTED, event -> {
            Tour tour = (Tour) event.getData();
            selectedTour.set(tour);
            tourSelected.set(tour != null);

            if (tour != null) {
                loadLogsForTour(tour.getId());
            } else {
                tourLogs.clear();
            }

            selectedLog.set(null);
            logSelected.set(false);
            hideForm();
        });

        dateText.addListener((obs, oldVal, newVal) -> validateForm());
        timeText.addListener((obs, oldVal, newVal) -> validateForm());
        totalTimeText.addListener((obs, oldVal, newVal) -> validateForm());
        ratingValue.addListener((obs, oldVal, newVal) -> {
            updateRatingStars(newVal.intValue());
            validateForm();
        });

        resetForm();
    }

    private void loadLogsForTour(String tourId) {
        tourLogs.setAll(tourLogService.getLogsForTour(tourId));
    }

    public void showNewLogForm() {
        resetForm();
        editMode.set(false);
        formVisible.set(true);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        dateText.set(today.format(dateFormatter));
        timeText.set(now.format(timeFormatter));
        difficultyValue.set(Difficulty.MEDIUM);
        ratingValue.set(3);
        updateRatingStars(3);

        validateForm();
    }

    public void showEditLogForm() {
        TourLog log = selectedLog.get();
        if (log == null) return;

        resetForm();
        editMode.set(true);
        formVisible.set(true);

        logId = log.getId();
        LocalDateTime dateTime = log.getDateTime();
        dateText.set(dateTime.toLocalDate().format(dateFormatter));
        timeText.set(dateTime.toLocalTime().format(timeFormatter));
        commentText.set(log.getComment());
        difficultyValue.set(log.getDifficulty());
        totalTimeText.set(String.valueOf(log.getTotalTime()));
        ratingValue.set(log.getRating());
        updateRatingStars(log.getRating());
        distanceText.set(String.valueOf(log.getDistance()));

        validateForm();
    }

    public void hideForm() {
        formVisible.set(false);
        resetForm();
    }

    private void resetForm() {
        logId = null;
        dateText.set("");
        timeText.set("");
        commentText.set("");
        difficultyValue.set(Difficulty.MEDIUM);
        totalTimeText.set("");
        ratingValue.set(3);
        updateRatingStars(3);

        dateError.set("");
        timeError.set("");
        totalTimeError.set("");
        ratingError.set("");
        distanceError.set("");

        formValid.set(false);
        distanceText.set("");
    }

    private void updateRatingStars(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        ratingStars.set(stars.toString());
    }

    public void validateForm() {
        boolean valid = true;

        try {
            if (dateText.get() == null || dateText.get().trim().isEmpty()) {
                dateError.set("Date is required");
                valid = false;
            } else {
                LocalDate.parse(dateText.get(), dateFormatter);
                dateError.set("");
            }
        } catch (DateTimeParseException e) {
            dateError.set("Invalid date format (DD.MM.YYYY)");
            valid = false;
        }

        try {
            if (timeText.get() == null || timeText.get().trim().isEmpty()) {
                timeError.set("Time is required");
                valid = false;
            } else {
                LocalTime.parse(timeText.get(), timeFormatter);
                timeError.set("");
            }
        } catch (DateTimeParseException e) {
            timeError.set("Invalid time format (HH:MM)");
            valid = false;
        }

        try {
            if (totalTimeText.get() == null || totalTimeText.get().trim().isEmpty()) {
                totalTimeError.set("Total time is required");
                valid = false;
            } else {
                int totalTime = Integer.parseInt(totalTimeText.get());
                if (totalTime <= 0) {
                    totalTimeError.set("Must be greater than 0");
                    valid = false;
                } else {
                    totalTimeError.set("");
                }
            }
        } catch (NumberFormatException e) {
            totalTimeError.set("Must be a number");
            valid = false;
        }

        int rating = ratingValue.get();
        if (rating < 1 || rating > 5) {
            ratingError.set("Rating must be between 1 and 5");
            valid = false;
        } else {
            ratingError.set("");
        }

        try {
            if (distanceText.get() == null || distanceText.get().trim().isEmpty()) {
                distanceError.set("Distance is required");
                valid = false;
            } else {
                double distance = Double.parseDouble(distanceText.get());
                if (distance <= 0) {
                    distanceError.set("Must be greater than 0");
                    valid = false;
                } else {
                    distanceError.set("");
                }
            }
        } catch (NumberFormatException e) {
            distanceError.set("Must be a number");
            valid = false;
        }

        formValid.set(valid);
    }

    public boolean saveLog() {
        if (!formValid.get() || selectedTour.get() == null) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(dateText.get(), dateFormatter);
            LocalTime time = LocalTime.parse(timeText.get(), timeFormatter);
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            int totalTime = Integer.parseInt(totalTimeText.get());
            double distance = Double.parseDouble(distanceText.get());

            TourLog log;
            if (editMode.get() && logId != null) {

                log = selectedLog.get();
                log.setDateTime(dateTime);
                log.setComment(commentText.get());
                log.setDifficulty(difficultyValue.get());
                log.setTotalTime(totalTime);
                log.setRating(ratingValue.get());
                log.setDistance(distance);

                tourLogService.updateLog(log);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_UPDATED, log));
            } else {

                log = new TourLog(
                        UUID.randomUUID().toString(),
                        selectedTour.get().getId(),
                        dateTime,
                        commentText.get(),
                        difficultyValue.get(),
                        distance,
                        totalTime,
                        ratingValue.get()
                );

                tourLogService.addLog(log);
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_ADDED, log));
            }

            loadLogsForTour(selectedTour.get().getId());
            hideForm();
            return true;

        } catch (Exception e) {

            System.err.println("Error saving log: " + e.getMessage());
            return false;
        }
    }

    public void deleteSelectedLog() {
        TourLog log = selectedLog.get();
        if (log != null) {
            tourLogService.deleteLog(log);
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_DELETED, log));

            loadLogsForTour(selectedTour.get().getId());
            selectedLog.set(null);
            logSelected.set(false);
        }
    }

    public void setSelectedLog(TourLog log) {
        selectedLog.set(log);
        logSelected.set(log != null);
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_LOG_SELECTED, log));
    }

    public ObservableList<TourLog> getTourLogs() {
        return tourLogs;
    }

    public ObservableList<Difficulty> getDifficultyLevels() {
        return difficultyLevels;
    }

    public ObjectProperty<TourLog> selectedLogProperty() {
        return selectedLog;
    }

    public BooleanProperty formVisibleProperty() {
        return formVisible;
    }

    public BooleanProperty formValidProperty() {
        return formValid;
    }

    public BooleanProperty logSelectedProperty() {
        return logSelected;
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }

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

    public StringProperty totalTimeTextProperty() {
        return totalTimeText;
    }

    public IntegerProperty ratingValueProperty() {
        return ratingValue;
    }

    public StringProperty ratingStarsProperty() {
        return ratingStars;
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

    public StringProperty distanceTextProperty() {
        return distanceText;
    }

    public StringProperty distanceErrorProperty() {
        return distanceError;
    }
}
