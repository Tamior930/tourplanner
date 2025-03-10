package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// ViewModel für die Suchfunktionalität
public class SearchViewModel implements ViewModel {
    
    private final TourService tourService;
    private final StringProperty searchTextProperty = new SimpleStringProperty("");
    private final BooleanProperty searchDisabledProperty = new SimpleBooleanProperty(true);
    
    public SearchViewModel(TourService tourService) {
        this.tourService = tourService;
    }
    
    @Override
    public void initialize() {
        // Bindung des deaktivierten Suchstatus an den Suchtext
        searchTextProperty.addListener((observable, oldValue, newValue) -> {
            searchDisabledProperty.set(newValue == null || newValue.trim().isEmpty());
            // Suche durchführen, während der Benutzer tippt
            search();
        });
    }
    
    // Suche mit aktuellem Suchtext durchführen
    public void search() {
        String searchText = searchTextProperty.get();
        // Suchereignis mit Ergebnissen aus TourService veröffentlichen
        EventBus.getInstance().publish(new Event<>(EventType.SEARCH_PERFORMED, 
            tourService.searchTours(searchText)));
    }
    
    // Suchtext löschen
    public void clearSearch() {
        searchTextProperty.set("");
    }
    
    public StringProperty searchTextProperty() {
        return searchTextProperty;
    }
    
    public BooleanProperty searchDisabledProperty() {
        return searchDisabledProperty;
    }
    
    public String getSearchText() {
        return searchTextProperty.get();
    }
    
    public void setSearchText(String searchText) {
        searchTextProperty.set(searchText);
    }
} 