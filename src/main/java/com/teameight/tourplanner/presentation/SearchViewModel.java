package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class SearchViewModel {
    private final EventManager eventManager;

    private final StringProperty searchText = new SimpleStringProperty("");

    public SearchViewModel(TourService tourService, EventManager eventManager) {
        this.eventManager = eventManager;

        // Listen for changes to search text and trigger search
        searchText.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                performSearch(newValue);
            }
        });
    }

    private void performSearch(String query) {
        eventManager.publish(Events.SEARCH_TOURS, query);
    }

    public void clearSearch() {
        searchText.set("");
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }
}
