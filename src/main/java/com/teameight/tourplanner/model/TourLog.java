package com.teameight.tourplanner.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TourLog {
    private final String id;
    private final String tourId;
    private LocalDateTime dateTime;
    private String comment;
    private Difficulty difficulty;
    private int totalTime;
    private int rating;

    public TourLog(String id, String tourId, LocalDateTime dateTime, String comment,
                   Difficulty difficulty, int totalTime, int rating) {
        this.id = id;
        this.tourId = tourId;
        this.dateTime = dateTime;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.rating = rating;
    }

    public static TourLog createNew(String tourId) {
        return new TourLog(
                UUID.randomUUID().toString(),
                tourId,
                LocalDateTime.now(),
                "",
                Difficulty.MEDIUM,
                0,
                0
        );
    }

    public String getId() {
        return id;
    }

    public String getTourId() {
        return tourId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
} 