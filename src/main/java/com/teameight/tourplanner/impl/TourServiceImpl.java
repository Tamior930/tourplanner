package com.teameight.tourplanner.impl;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.repository.TourRepository;
import com.teameight.tourplanner.repository.TourRepositoryOrm;
import com.teameight.tourplanner.service.TourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;
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

        // If no tours in database, add sample data
        if (tourList.isEmpty()) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Image placeholderImage = new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png"));

        Tour tour1 = new Tour(
                UUID.randomUUID().toString(),
                "Vienna to Salzburg",
                "A beautiful journey through Austria",
                "Vienna", "Salzburg",
                TransportType.CAR, "295 km", "3 h");

        Tour tour2 = new Tour(
                UUID.randomUUID().toString(),
                "Munich to Berlin",
                "Trip from Bavaria to the capital",
                "Munich", "Berlin",
                TransportType.TRAIN, "504 km", "4 h");

        Tour tour3 = new Tour(
                UUID.randomUUID().toString(),
                "Salzburg to Innsbruck",
                "Crossing the Alps in Austria",
                "Salzburg", "Innsbruck",
                TransportType.CAR, "185 km", "2 h 15 min");

        // Save to database and update observable list
        addTour(tour1);
        addTour(tour2);
        addTour(tour3);
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

        // Update the observable list
        int index = -1;
        for (int i = 0; i < tourList.size(); i++) {
            if (tourList.get(i).getId().equals(tour.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            tourList.set(index, tour);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteTour(Tour tour) {
        if (tour == null) {
            return false;
        }

        tourRepository.delete(tour);
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

