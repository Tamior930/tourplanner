package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.repository.TourRepository;
import com.teameight.tourplanner.repository.TourRepositoryOrm;
import com.teameight.tourplanner.service.TourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

public class TourServiceImpl implements TourService {

    private static final Logger LOGGER = LogManager.getLogger(TourServiceImpl.class);
    private final TourRepository tourRepository;
    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public TourServiceImpl() {
        this.tourRepository = new TourRepositoryOrm();
        LOGGER.info("TourServiceImpl initialized");
        reloadToursFromDatabase();
    }

    private void reloadToursFromDatabase() {
        LOGGER.debug("Reloading tours from database");
        tourList.clear();
        tourList.addAll(tourRepository.findAll());
        LOGGER.info("Loaded {} tours from database", tourList.size());
    }

    @Override
    public void refreshTours() {
        LOGGER.debug("Refreshing tours from database");
        reloadToursFromDatabase();
    }

    @Override
    public ObservableList<Tour> getAllTours() {
        return tourList;
    }

    @Override
    public Tour getTourById(String id) {
        if (id == null) {
            LOGGER.warn("getTourById called with null id");
            return null;
        }
        LOGGER.debug("Getting tour with ID: {}", id);
        return tourRepository.find(id).orElse(null);
    }

    @Override
    public Tour addTour(Tour tour) {
        if (tour == null) {
            LOGGER.error("Cannot add tour: tour is null");
            return null;
        }

        tour = tourRepository.save(tour);
        LOGGER.info("Added new tour with ID: {}, name: {}", tour.getId(), tour.getName());
        tourList.add(tour);
        return tour;
    }

    @Override
    public boolean updateTour(Tour tour) {
        if (tour == null || tour.getId() == null) {
            LOGGER.error("Cannot update tour: tour is null or has null id");
            return false;
        }

        tour = tourRepository.save(tour);
        LOGGER.info("Updated tour with ID: {}, name: {}", tour.getId(), tour.getName());

        int index = findTourIndexById(tour.getId());
        if (index != -1) {
            tourList.set(index, tour);
            return true;
        }

        LOGGER.warn("Updated tour with ID: {} not found in local cache", tour.getId());
        return false;
    }

    @Override
    public boolean deleteTour(Tour tour) {
        if (tour == null || tour.getId() == null) {
            LOGGER.error("Cannot delete tour: tour is null or has null id");
            return false;
        }

        // Delete from repository
        tourRepository.delete(tour);
        LOGGER.info("Deleted tour with ID: {}, name: {}", tour.getId(), tour.getName());

        // Find and remove from the observable list by ID
        int index = findTourIndexById(tour.getId());
        if (index != -1) {
            tourList.remove(index);
            return true;
        }

        LOGGER.warn("Deleted tour with ID: {} not found in local cache", tour.getId());
        return false;
    }

    // Find the index of a tour in the tourList by its ID
    private int findTourIndexById(String tourId) {
        for (int i = 0; i < tourList.size(); i++) {
            if (tourList.get(i).getId().equals(tourId)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ObservableList<Tour> searchTours(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            LOGGER.debug("Empty search text, returning all tours");
            return tourList;
        }

        LOGGER.debug("Searching tours with text: '{}'", searchText);
        String searchLower = searchText.toLowerCase();

        ObservableList<Tour> result = tourList.stream()
                .filter(tour ->
                        tour.getName().toLowerCase().contains(searchLower) ||
                                tour.getDescription().toLowerCase().contains(searchLower) ||
                                tour.getOrigin().toLowerCase().contains(searchLower) ||
                                tour.getDestination().toLowerCase().contains(searchLower) ||
                                tour.getTransportType().toString().toLowerCase().contains(searchLower)
                )
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        LOGGER.debug("Found {} tours matching search text: '{}'", result.size(), searchText);
        return result;
    }
}