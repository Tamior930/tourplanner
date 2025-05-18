package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.repository.TourRepository;
import com.teameight.tourplanner.repository.TourRepositoryOrm;
import com.teameight.tourplanner.service.TourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public TourServiceImpl() {
        this.tourRepository = new TourRepositoryOrm();
        reloadToursFromDatabase();
    }

    private void reloadToursFromDatabase() {
        tourList.clear();
        tourList.addAll(tourRepository.findAll());
    }

    @Override
    public void refreshTours() {
        reloadToursFromDatabase();
    }

    @Override
    public ObservableList<Tour> getAllTours() {
        return tourList;
    }

    @Override
    public Tour getTourById(String id) {
        return tourRepository.find(id).orElse(null);
    }

    @Override
    public Tour addTour(Tour tour) {
        tour = tourRepository.save(tour);
        tourList.add(tour);
        return tour;
    }

    @Override
    public boolean updateTour(Tour tour) {
        if (tour == null || tour.getId() == null) {
            return false;
        }

        tour = tourRepository.save(tour);

        int index = findTourIndexById(tour.getId());
        if (index != -1) {
            tourList.set(index, tour);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteTour(Tour tour) {
        if (tour == null || tour.getId() == null) {
            return false;
        }

        // Delete from repository
        tourRepository.delete(tour);

        // Find and remove from the observable list by ID
        int index = findTourIndexById(tour.getId());
        if (index != -1) {
            tourList.remove(index);
            return true;
        }

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
            return tourList;
        }

        String searchLower = searchText.toLowerCase();

        return tourList.stream()
                .filter(tour ->
                        tour.getName().toLowerCase().contains(searchLower) ||
                                tour.getDescription().toLowerCase().contains(searchLower) ||
                                tour.getOrigin().toLowerCase().contains(searchLower) ||
                                tour.getDestination().toLowerCase().contains(searchLower) ||
                                tour.getTransportType().toString().toLowerCase().contains(searchLower)
                )
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}