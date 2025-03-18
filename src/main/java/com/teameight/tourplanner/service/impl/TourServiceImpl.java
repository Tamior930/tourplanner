package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.stream.Collectors;

public class TourServiceImpl implements TourService {

    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public TourServiceImpl() {
        loadSampleData();
    }

    private void loadSampleData() {
        Image placeholderImage = new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png"));

        addTour(new Tour(
                "1",
                "Vienna to Salzburg",
                "A beautiful journey through Austria",
                "Vienna", "Salzburg",
                TransportType.CAR, "295 km", "3 h",
                placeholderImage));

        addTour(new Tour(
                "2",
                "Munich to Berlin",
                "Trip from Bavaria to the capital",
                "Munich", "Berlin",
                TransportType.TRAIN, "504 km", "4 h",
                placeholderImage));

        addTour(new Tour(
                "3",
                "Salzburg to Innsbruck",
                "Crossing the Alps in Austria",
                "Salzburg", "Innsbruck",
                TransportType.CAR, "185 km", "2 h 15 min",
                placeholderImage));
    }

    @Override
    public ObservableList<Tour> getAllTours() {
        return tourList;
    }

    @Override
    public Tour getTourById(String id) {
        return tourList.stream()
                .filter(tour -> tour.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Tour addTour(Tour tour) {
        tourList.add(tour);
        return tour;
    }

    @Override
    public boolean updateTour(Tour originalTour) {
        if (originalTour == null || originalTour.getId() == null) {
            return false;
        }
        
        // Find the tour in the list
        int index = -1;
        for (int i = 0; i < tourList.size(); i++) {
            if (tourList.get(i).getId().equals(originalTour.getId())) {
                index = i;
                break;
            }
        }
        
        // If found, update it
        if (index != -1) {
            tourList.set(index, originalTour);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean deleteTour(Tour tour) {
        return tourList.remove(tour);
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

