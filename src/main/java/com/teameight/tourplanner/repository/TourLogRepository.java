package com.teameight.tourplanner.repository;

import com.teameight.tourplanner.model.TourLog;

import java.util.List;
import java.util.Optional;

public interface TourLogRepository {
    Optional<TourLog> find(String id);

    List<TourLog> findAll();

    TourLog save(TourLog entity);

    TourLog delete(TourLog entity);

    List<TourLog> deleteAll();

    List<TourLog> findByTourId(String tourId);

    List<TourLog> deleteByTourId(String tourId);
}
