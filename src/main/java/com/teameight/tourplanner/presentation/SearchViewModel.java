package com.teameight.tourplanner.presentation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel für die Suchfunktion
 * Trennt die Suchlogik vom TourListViewModel
 */
public class SearchViewModel implements ViewModel {
    
    // Observable properties
    private final StringProperty searchTextProperty = new SimpleStringProperty("");
    private final BooleanProperty searchDisabledProperty = new SimpleBooleanProperty(true);
    
    @Override
    public void initialize() {
        // Bindet den Button-Status an den Suchtext (deaktiviert wenn leer)
        searchDisabledProperty.bind(searchTextProperty.isEmpty());
    }
    
    /**
     * Getter für die searchTextProperty (für Binding)
     */
    public StringProperty searchTextProperty() {
        return searchTextProperty;
    }
    
    /**
     * Property die angibt, ob die Suche deaktiviert sein sollte
     */
    public ReadOnlyBooleanProperty searchDisabledProperty() {
        return searchDisabledProperty;
    }
    
    /**
     * Führt die Suche aus und leert das Suchfeld
     */
    public void search() {
        // Speichert den Text NICHT in einer Historie
        // Leert das Suchfeld
        searchTextProperty.set("");
    }
    
    /**
     * Getter für den Suchtext
     */
    public String getSearchText() {
        return searchTextProperty.get();
    }
    
    /**
     * Setter für den Suchtext
     */
    public void setSearchText(String searchText) {
        this.searchTextProperty.set(searchText);
    }
    
    /**
     * Leert das Suchfeld
     */
    public void clearSearch() {
        searchTextProperty.set("");
    }
} 