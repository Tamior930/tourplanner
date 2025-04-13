package com.teameight.tourplanner.impl;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.repository.TourLogRepository;
import com.teameight.tourplanner.repository.TourLogRepositoryOrm;
import com.teameight.tourplanner.service.TourLogService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TourLogServiceImpl implements TourLogService {

    private final TourLogRepository tourLogRepository;
    private final ObservableList<TourLog> allTourLogs = FXCollections.observableArrayList();

    public TourLogServiceImpl() {
        this.tourLogRepository = new TourLogRepositoryOrm();
        reloadLogsFromDatabase();
    }

    private void reloadLogsFromDatabase() {
        allTourLogs.clear();
        allTourLogs.addAll(tourLogRepository.findAll());

        // If no logs in database, add sample data
        if (allTourLogs.isEmpty()) {
            addSampleData();
        }
    }

    private void addSampleData() {
        TourLog log1 = new TourLog(
                UUID.randomUUID().toString(),
                "1",
                LocalDateTime.now().minusDays(5),
                "Beautiful scenery along the way",
                Difficulty.EASY,
                15.5,
                180,
                4
        );

        TourLog log2 = new TourLog(
                UUID.randomUUID().toString(),
                "1",
                LocalDateTime.now().minusDays(30),
                "Rainy day, but still enjoyable",
                Difficulty.MEDIUM,
                12.3,
                210,
                3
        );

        TourLog log3 = new TourLog(
                UUID.randomUUID().toString(),
                "2",
                LocalDateTime.now().minusDays(10),
                "Long journey but worth it",
                Difficulty.HARD,
                25.7,
                300,
                5
        );

        // Save to database and update observable list
        addLog(log1);
        addLog(log2);
        addLog(log3);
    }

    @Override
    public ObservableList<TourLog> getLogsForTour(String tourId) {
        List<TourLog> logs = tourLogRepository.findByTourId(tourId);
        return FXCollections.observableArrayList(logs);
    }

    @Override
    public TourLog addLog(TourLog log) {
        log = tourLogRepository.save(log);
        allTourLogs.add(log);
        return log;
    }

    @Override
    public boolean updateLog(TourLog log) {
        if (log == null || log.getId() == null) {
            return false;
        }

        log = tourLogRepository.save(log);

        // Update the observable list
        int index = -1;
        for (int i = 0; i < allTourLogs.size(); i++) {
            if (allTourLogs.get(i).getId().equals(log.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            allTourLogs.set(index, log);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteLog(TourLog log) {
        if (log == null) {
            return false;
        }

        tourLogRepository.delete(log);
        return allTourLogs.remove(log);
    }
} 