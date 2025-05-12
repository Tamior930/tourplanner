package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.repository.TourLogRepository;
import com.teameight.tourplanner.repository.TourLogRepositoryOrm;
import com.teameight.tourplanner.service.TourLogService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TourLogServiceImpl implements TourLogService {

    private final TourLogRepository tourLogRepository;
    private final Map<String, ObservableList<TourLog>> tourLogCache = new HashMap<>();

    public TourLogServiceImpl() {
        this.tourLogRepository = new TourLogRepositoryOrm();
    }

    @Override
    public ObservableList<TourLog> getLogsForTour(String tourId) {
        if (tourId == null) {
            return FXCollections.observableArrayList();
        }

        if (!tourLogCache.containsKey(tourId)) {
            List<TourLog> logs = tourLogRepository.findByTourId(tourId);
            ObservableList<TourLog> observableLogs = FXCollections.observableArrayList(logs);
            tourLogCache.put(tourId, observableLogs);
        }

        return tourLogCache.get(tourId);
    }

    @Override
    public TourLog getLogById(String logId) {
        if (logId == null) {
            return null;
        }

        // Check if the log is in any of the cached tours first
        for (ObservableList<TourLog> logs : tourLogCache.values()) {
            for (TourLog log : logs) {
                if (log.getId().equals(logId)) {
                    return log;
                }
            }
        }

        // If not found in cache, fetch from repository
        Optional<TourLog> logOptional = tourLogRepository.find(logId);
        return logOptional.orElse(null);
    }

    @Override
    public TourLog addLog(TourLog log) {
        if (log == null || log.getTourId() == null) {
            return null;
        }

        log = tourLogRepository.save(log);

        String tourId = log.getTourId();
        if (!tourLogCache.containsKey(tourId)) {
            tourLogCache.put(tourId, FXCollections.observableArrayList());
        }
        tourLogCache.get(tourId).add(log);

        return log;
    }

    @Override
    public boolean updateLog(TourLog log) {
        if (log == null || log.getId() == null || log.getTourId() == null) {
            return false;
        }

        log = tourLogRepository.save(log);

        String tourId = log.getTourId();
        if (tourLogCache.containsKey(tourId)) {
            ObservableList<TourLog> logs = tourLogCache.get(tourId);

            for (int i = 0; i < logs.size(); i++) {
                if (logs.get(i).getId().equals(log.getId())) {
                    logs.set(i, log);
                    return true;
                }
            }
        }

        getLogsForTour(log.getTourId());
        return true;
    }

    @Override
    public boolean deleteLog(TourLog log) {
        if (log == null || log.getId() == null || log.getTourId() == null) {
            return false;
        }

        tourLogRepository.delete(log);

        String tourId = log.getTourId();
        if (tourLogCache.containsKey(tourId)) {
            return tourLogCache.get(tourId).removeIf(l -> l.getId().equals(log.getId()));
        }

        return true;
    }
}