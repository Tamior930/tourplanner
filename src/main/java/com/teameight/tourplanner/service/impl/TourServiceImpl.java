package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementierung des TourService Interface
 * Dies ist eine einfache Implementierung im Arbeitsspeicher ohne Datenbank
 */
public class TourServiceImpl implements TourService {
    
    // Speicherung der Touren im RAM
    private final Map<String, Tour> tourMap = new HashMap<>();
    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();
    
    public TourServiceImpl() {
        // Test Data
        loadSampleData();
    }
    
    // Beispieldaten für Tests laden
    private void loadSampleData() {
        // Platzhalterbild laden
        Image placeholderImage = new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png"));
        
        // Beispieltouren erstellen
        createTour(new Tour(UUID.randomUUID().toString(), "Vienna to Salzburg", "A beautiful journey through Austria", 
                "Vienna", "Salzburg", TransportType.CAR, "295 km", "3 hours", placeholderImage));
        
        createTour(new Tour(UUID.randomUUID().toString(), "Munich to Berlin", "Explore Germany's countryside", 
                "Munich", "Berlin", TransportType.TRAIN, "504 km", "4 hours", placeholderImage));
        
        createTour(new Tour(UUID.randomUUID().toString(), "Paris to Lyon", "French countryside tour", 
                "Paris", "Lyon", TransportType.CAR, "463 km", "4.5 hours", placeholderImage));
    }
    
    @Override
    public ObservableList<Tour> getAllTours() {
        return tourList;
    }
    
    @Override
    public Tour getTourById(String id) {
        return tourMap.get(id);
    }
    
    @Override
    public Tour createTour(Tour tour) {
        // ID generieren, wenn nicht vorhanden
        if (tour.getId() == null || tour.getId().isEmpty()) {
            tour.setId(UUID.randomUUID().toString());
        }
        
        // In Map und Liste speichern
        tourMap.put(tour.getId(), tour);
        tourList.add(tour);
        
        return tour;
    }
    
    @Override
    public Tour updateTour(Tour tour) {
        if (tour.getId() == null || !tourMap.containsKey(tour.getId())) {
            return null; // Kann nicht aktualisieren, wenn Tour nicht existiert
        }
        
        // Update in map
        tourMap.put(tour.getId(), tour);
        
        // Update in list
        for (int i = 0; i < tourList.size(); i++) {
            if (tourList.get(i).getId().equals(tour.getId())) {
                tourList.set(i, tour);
                break;
            }
        }
        
        return tour;
    }
    
    @Override
    public boolean deleteTour(String id) {
        Tour tour = tourMap.remove(id);
        if (tour != null) {
            tourList.remove(tour);
            return true;
        }
        return false;
    }
    
    @Override
    public ObservableList<Tour> searchTours(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return getAllTours();
        }
        
        // Suche nach Text (case-insensitive)
        String searchLower = searchText.toLowerCase();
        return tourList.stream()
                .filter(tour -> 
                    tour.getName().toLowerCase().contains(searchLower) ||
                    tour.getDescription().toLowerCase().contains(searchLower) ||
                    tour.getOrigin().toLowerCase().contains(searchLower) ||
                    tour.getDestination().toLowerCase().contains(searchLower))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}

