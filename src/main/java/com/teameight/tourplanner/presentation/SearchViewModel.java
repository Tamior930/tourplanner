package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchViewModel {
    private final TourService tourService;
    private final StringProperty searchText = new SimpleStringProperty("");

    public SearchViewModel(TourService tourService) {
        this.tourService = tourService;

        searchText.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                search(newValue);
            }
        });
    }

    public void search(String query) {
        EventBus.getInstance().publish(new Event<>(EventType.SEARCH_TOURS, query));
    }

    public void clearSearch() {
        searchText.set("");
        EventBus.getInstance().publish(new Event<>(EventType.SEARCH_TOURS, ""));
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }
}
