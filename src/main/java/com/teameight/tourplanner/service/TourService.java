package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;
import javafx.collections.ObservableList;

public interface TourService {

    // Alle Touren abrufen
    ObservableList<Tour> getAllTours();

    // Tour nach ID abrufen
    Tour getTourById(String id);

    // Neue Tour erstellen
    Tour addTour(Tour tour);

    // Bestehende Tour aktualisieren
    boolean updateTour(Tour originalTour);

    // Tour löschen
    boolean deleteTour(Tour tour);

    // Nach Touren suchen, die dem Suchtext entsprechen
    ObservableList<Tour> searchTours(String searchText);
} 