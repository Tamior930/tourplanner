package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.TourLog;
import javafx.collections.ObservableList;

public interface TourLogService {

    ObservableList<TourLog> getLogsForTour(String tourId);

    TourLog getLogById(String logId);

    TourLog addLog(TourLog log);

    boolean updateLog(TourLog log);

    boolean deleteLog(TourLog log);
} 