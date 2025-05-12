package com.teameight.tourplanner.repository;

import com.teameight.tourplanner.model.Tour;

import java.util.List;
import java.util.Optional;

public interface TourRepository {

    Optional<Tour> find(String id);

    List<Tour> findAll();

    Tour save(Tour entity);

    Tour delete(Tour entity);
}
