package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;
import javafx.collections.ObservableList;

public interface TourService {

    ObservableList<Tour> getAllTours();

    Tour getTourById(String id);

    Tour addTour(Tour tour);

    boolean updateTour(Tour originalTour);

    boolean deleteTour(Tour tour);

    ObservableList<Tour> searchTours(String searchText);
} 