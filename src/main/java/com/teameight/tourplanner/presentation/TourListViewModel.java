package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.Image;
import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;

import java.util.UUID;

// ViewModel für die Tour-Liste Ansicht
public class TourListViewModel implements ViewModel {
    // Observable properties
    private final ListProperty<Tour> tourListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Tour> selectedTourProperty = new SimpleObjectProperty<>();
    
    // Die SearchViewModel Instanz
    private final SearchViewModel searchViewModel;
    
    // Beispiel Daten für Demonstration
    private final ObservableList<Tour> allTours = FXCollections.observableArrayList();
    
    // Filtered List für Suchfunktionalität
    private FilteredList<Tour> filteredTourList;
    
    private final TourService tourService;
    
    public TourListViewModel(SearchViewModel searchViewModel, TourService tourService) {
        this.searchViewModel = searchViewModel;
        this.tourService = tourService;
    }
    
    @Override
    public void initialize() {
        // Initialisieren mit allen Touren aus dem Service
        tourListProperty.set(tourService.getAllTours());
        
        // Für Suchereignisse anmelden
        EventBus.getInstance().subscribe(EventType.SEARCH_PERFORMED, this::handleSearchResults);
    }
    
    // Suchergebnisse verarbeiten
    @SuppressWarnings("unchecked")
    private void handleSearchResults(Event<?> event) {
        if (event.getData() instanceof ObservableList) {
            tourListProperty.set((ObservableList<Tour>) event.getData());
        }
    }
    
    // Beispiel Touren erstellen
    private void createSampleTours() {
        // Platzhalterbild laden
        Image placeholderImage = new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png"));
        
        // Beispiel Touren erstellen
        allTours.add(new Tour("1", "Vienna to Salzburg", "A beautiful journey through Austria", 
                "Vienna", "Salzburg", TransportType.CAR, "295 km", "3 hours", placeholderImage));
        
        allTours.add(new Tour("2", "Munich to Berlin", "Explore Germany's countryside", 
                "Munich", "Berlin", TransportType.TRAIN, "504 km", "4 hours", placeholderImage));
        
        allTours.add(new Tour("3", "Paris to Lyon", "French countryside tour", 
                "Paris", "Lyon", TransportType.CAR, "463 km", "4.5 hours", placeholderImage));
    }
    
    // Neue Tour hinzufügen
    public void addTour(Tour tour) {
        if (tour != null && !tour.getName().trim().isEmpty()) {
            // Tour zur Liste aller Touren hinzufügen
            allTours.add(tour);
            
            // Angezeigte Touren aktualisieren
            searchViewModel.search();
        }
    }
    
    // Bestehende Tour aktualisieren
    public void updateTour(Tour updatedTour) {
        if (updatedTour != null && !updatedTour.getName().trim().isEmpty()) {
            // Bestehende Tour finden und aktualisieren
            for (int i = 0; i < allTours.size(); i++) {
                if (allTours.get(i).getId().equals(updatedTour.getId())) {
                    allTours.set(i, updatedTour);
                    break;
                }
            }
            
            // Angezeigte Touren aktualisieren
            searchViewModel.search();
            
            // Wenn die aktualisierte Tour ausgewählt war, aktualisieren
            if (selectedTourProperty.get() != null && 
                selectedTourProperty.get().getId().equals(updatedTour.getId())) {
                selectedTourProperty.set(updatedTour);
            }
        }
    }
    
    // Tour löschen
    public void deleteTour(Tour tour) {
        if (tour != null) {
            // Tour aus der Liste entfernen
            allTours.removeIf(t -> t.getId().equals(tour.getId()));
            
            // Angezeigte Touren aktualisieren
            searchViewModel.search();
            
            // Wenn die gelöschte Tour ausgewählt war, auswählen
            if (selectedTourProperty.get() != null && 
                selectedTourProperty.get().getId().equals(tour.getId())) {
                selectedTourProperty.set(null);
            }
        }
    }
    
    // Getter für Properties
    public ListProperty<Tour> tourListProperty() {
        return tourListProperty;
    }
    
    public ObjectProperty<Tour> selectedTourProperty() {
        return selectedTourProperty;
    }
    
    public Tour getSelectedTour() {
        return selectedTourProperty.get();
    }
    
    public void setSelectedTour(Tour tour) {
        selectedTourProperty.set(tour);
    }
    
    public SearchViewModel getSearchViewModel() {
        return searchViewModel;
    }
} 