package com.teameight.tourplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class TourLog {

    @Id
    private String id;

    private String tourId;

    private LocalDateTime dateTime;

    private String comment;

    private Difficulty difficulty;

    private double distance;

    private int totalTime;

    private int rating;

    public TourLog() {
    }

    public TourLog(String id, String tourId, LocalDateTime dateTime, String comment,
                   Difficulty difficulty, double distance, int totalTime, int rating) {
        this.id = id;
        this.tourId = tourId;
        this.dateTime = dateTime;
        this.comment = comment;
        this.difficulty = difficulty;
        this.distance = distance;
        this.totalTime = totalTime;
        this.rating = rating;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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