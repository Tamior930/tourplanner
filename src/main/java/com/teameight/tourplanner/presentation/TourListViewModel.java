package com.teameight.tourplanner.presentation;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * ViewModel für die Tourlisten-Ansicht
 * Verwaltet beobachtbare Eigenschaften, an die die View binden kann
 */
public class TourListViewModel implements ViewModel {
    // Beobachtbare Eigenschaften
    private final ListProperty<String> tourListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty selectedTourDetailsProperty = new SimpleStringProperty("");
    
    // Die SearchViewModel-Instanz
    private final SearchViewModel searchViewModel;
    
    // Gefilterte Liste für die Suchfunktionalität
    private FilteredList<String> filteredTourList;
    
    // Beispieldaten für Demonstrationszwecke
    private final ObservableList<String> allTours = FXCollections.observableArrayList(
            "Test1", "Test2", "Test3"
    );
    
    public TourListViewModel(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }
    
    @Override
    public void initialize() {
        // Initialisierung mit Beispieldaten
        tourListProperty.set(FXCollections.observableArrayList(allTours));
        
        // Gefilterte Liste einrichten
        filteredTourList = new FilteredList<>(allTours);
        
        // Suchtext an Filter binden
        searchViewModel.searchTextProperty().addListener((observable, oldValue, newValue) -> {
            filterTours(newValue);
        });
    }
    
    /**
     * Touren basierend auf Suchtext filtern
     */
    private void filterTours(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            // Wenn die Suche leer ist, alle Touren anzeigen
            tourListProperty.set(FXCollections.observableArrayList(allTours));
        } else {
            // Touren nach Suchtext filtern
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            for (String tour : allTours) {
                if (tour.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(tour);
                }
            }
            tourListProperty.set(filteredList);
        }
    }
    
    /**
     * Details der ausgewählten Tour aktualisieren
     */
    public void setSelectedTour(String tourName) {
        if (tourName != null) {
            selectedTourDetailsProperty.set("Details für: " + tourName);
        } else {
            selectedTourDetailsProperty.set("");
        }
    }
    
    // Getter für Eigenschaften (für Binding)
    public ListProperty<String> tourListProperty() {
        return tourListProperty;
    }
    
    public StringProperty selectedTourDetailsProperty() {
        return selectedTourDetailsProperty;
    }
    
    // Eine neue Tour hinzufügen
    public void addTour(String tourName) {
        if (!tourName.trim().isEmpty()) {
            // Tour tatsächlich zur Liste aller Touren hinzufügen
            allTours.add(tourName);
            
            // Aktualisiere die angezeigten Touren
            filterTours(searchViewModel.getSearchText());
            
            System.out.println("Tour hinzugefügt: " + tourName);
            System.out.println("Alle Touren: " + allTours);
        }
    }

    /**
     * Zugriff auf das SearchViewModel
     */
    public SearchViewModel getSearchViewModel() {
        return searchViewModel;
    }
} 