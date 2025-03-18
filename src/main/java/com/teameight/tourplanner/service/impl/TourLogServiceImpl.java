package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.service.TourLogService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.UUID;

public class TourLogServiceImpl implements TourLogService {

    private final ObservableList<TourLog> allTourLogs = FXCollections.observableArrayList();

    public TourLogServiceImpl() {
        addSampleData();
    }

    private void addSampleData() {
        allTourLogs.add(new TourLog(
                UUID.randomUUID().toString(),
                "1",
                LocalDateTime.now().minusDays(5),
                "Beautiful scenery along the way",
                Difficulty.EASY,
                15.5,
                180,
                4
        ));
        allTourLogs.add(new TourLog(
                UUID.randomUUID().toString(),
                "1",
                LocalDateTime.now().minusDays(30),
                "Rainy day, but still enjoyable",
                Difficulty.MEDIUM,
                12.3,
                210,
                3
        ));
        allTourLogs.add(new TourLog(
                UUID.randomUUID().toString(),
                "2",
                LocalDateTime.now().minusDays(10),
                "Long journey but worth it",
                Difficulty.HARD,
                25.7,
                300,
                5
        ));
    }

    @Override
    public ObservableList<TourLog> getLogsForTour(String tourId) {
        return allTourLogs.filtered(log -> log.getTourId().equals(tourId));
    }

    @Override
    public TourLog addLog(TourLog log) {
        allTourLogs.add(log);
        return log;
    }

    @Override
    public boolean updateLog(TourLog log) {
        for (int i = 0; i < allTourLogs.size(); i++) {
            if (allTourLogs.get(i).getId().equals(log.getId())) {
                allTourLogs.set(i, log);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteLog(TourLog log) {
        return allTourLogs.removeIf(l -> l.getId().equals(log.getId()));
    }
} 