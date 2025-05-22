package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.repository.TourLogRepository;
import com.teameight.tourplanner.repository.TourLogRepositoryOrm;
import com.teameight.tourplanner.service.TourLogService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TourLogServiceImpl implements TourLogService {

    private static final Logger LOGGER = LogManager.getLogger(TourLogServiceImpl.class);
    private final TourLogRepository tourLogRepository;
    private final Map<String, ObservableList<TourLog>> tourLogCache = new HashMap<>();

    public TourLogServiceImpl() {
        this.tourLogRepository = new TourLogRepositoryOrm();
    }

    @Override
    public ObservableList<TourLog> getLogsForTour(String tourId) {
        if (tourId == null) {
            LOGGER.warn("getLogsForTour called with null tourId");
            return FXCollections.observableArrayList();
        }

        if (!tourLogCache.containsKey(tourId)) {
            LOGGER.debug("Tour logs not in cache for tourId: {}, fetching from repository", tourId);
            List<TourLog> logs = tourLogRepository.findByTourId(tourId);
            ObservableList<TourLog> observableLogs = FXCollections.observableArrayList(logs);
            tourLogCache.put(tourId, observableLogs);
        }

        return tourLogCache.get(tourId);
    }

    @Override
    public TourLog getLogById(String logId) {
        if (logId == null) {
            LOGGER.warn("getLogById called with null logId");
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
            LOGGER.error("Cannot add log: log is null or has null tourId");
            return null;
        }

        log = tourLogRepository.save(log);
        LOGGER.info("Added new tour log with ID: {} for tour: {}", log.getId(), log.getTourId());

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
            LOGGER.error("Cannot update log: log is null or has null id or tourId");
            return false;
        }

        log = tourLogRepository.save(log);
        LOGGER.info("Updated tour log with ID: {} for tour: {}", log.getId(), log.getTourId());

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
            LOGGER.error("Cannot delete log: log is null or has null id or tourId");
            return false;
        }

        tourLogRepository.delete(log);
        LOGGER.info("Deleted tour log with ID: {} for tour: {}", log.getId(), log.getTourId());

        String tourId = log.getTourId();
        if (tourLogCache.containsKey(tourId)) {
            return tourLogCache.get(tourId).removeIf(l -> l.getId().equals(log.getId()));
        }

        return true;
    }
}