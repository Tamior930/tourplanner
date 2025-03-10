package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;
import javafx.collections.ObservableList;

// Service-Interface für Tour-Operationen
public interface TourService {
    
    // Alle Touren abrufen
    ObservableList<Tour> getAllTours();
    
    // Tour anhand ihrer ID abrufen
    Tour getTourById(String id);
    
    // Neue Tour erstellen
    Tour createTour(Tour tour);
    
    // Bestehende Tour aktualisieren
    Tour updateTour(Tour tour);
    
    // Tour löschen
    boolean deleteTour(String id);
    
    // Nach Touren suchen, die dem Suchtext entsprechen
    ObservableList<Tour> searchTours(String searchText);
} 