package com.teameight.tourplanner.repository;

import com.teameight.tourplanner.model.TourLog;

import java.util.List;
import java.util.Optional;

public interface TourLogRepository {
    Optional<TourLog> find(String id);

    TourLog save(TourLog entity);

    TourLog delete(TourLog entity);

    List<TourLog> findByTourId(String tourId);
}
