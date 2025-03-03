package com.teameight.tourplanner.presentation;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * ViewModel for the tour list screen
 * Maintains observable properties that the view can bind to
 */
public class TourListViewModel implements ViewModel {
    // Observable properties
    private final ListProperty<String> tourListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty selectedTourDetailsProperty = new SimpleStringProperty("");
    
    // Die SearchViewModel-Instanz
    private final SearchViewModel searchViewModel;
    
    // Filtered list for search functionality
    private FilteredList<String> filteredTourList;
    
    // Mock data for demonstration
    private final ObservableList<String> allTours = FXCollections.observableArrayList(
            "Test1", "Test2", "Test3"
    );
    
    public TourListViewModel(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }
    
    @Override
    public void initialize() {
        // Initialize with mock data
        tourListProperty.set(FXCollections.observableArrayList(allTours));
        
        // Setup filtered list
        filteredTourList = new FilteredList<>(allTours);
        
        // Bind search text to filter
        searchViewModel.searchTextProperty().addListener((observable, oldValue, newValue) -> {
            filterTours(newValue);
        });
    }
    
    /**
     * Filter tours based on search text
     */
    private void filterTours(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            // If search is empty, show all tours
            tourListProperty.set(FXCollections.observableArrayList(allTours));
        } else {
            // Filter tours by search text
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
     * Update the selected tour details
     */
    public void setSelectedTour(String tourName) {
        if (tourName != null) {
            selectedTourDetailsProperty.set("Details for: " + tourName);
        } else {
            selectedTourDetailsProperty.set("");
        }
    }
    
    // Getters for properties (for binding)
    public ListProperty<String> tourListProperty() {
        return tourListProperty;
    }
    
    public StringProperty selectedTourDetailsProperty() {
        return selectedTourDetailsProperty;
    }
    
    // Add a new tour
    public void addTour(String tourName) {
        if (!tourName.trim().isEmpty()) {
            // Tour tatsächlich zur Liste aller Touren hinzufügen
            allTours.add(tourName);
            
            // Aktualisiere die angezeigten Touren
            filterTours(searchViewModel.getSearchText());
            
            System.out.println("Tour added: " + tourName);
            System.out.println("All tours: " + allTours);
        }
    }

    /**
     * Zugriff auf das SearchViewModel
     */
    public SearchViewModel getSearchViewModel() {
        return searchViewModel;
    }
} 