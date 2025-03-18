package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchViewModel {

    private final TourService tourService;
    private final StringProperty searchTextProperty = new SimpleStringProperty("");
    private final BooleanProperty searchDisabledProperty = new SimpleBooleanProperty(true);

    public SearchViewModel(TourService tourService) {
        this.tourService = tourService;

        searchDisabledProperty.bind(searchTextProperty.isEmpty());

        searchTextProperty.addListener((observable, oldValue, newValue) -> {
            search();
        });
    }

    public void initialize() {
        search();
    }

    public void search() {
        String searchText = searchTextProperty.get();
        EventBus.getInstance().publish(new Event<>(EventType.SEARCH_PERFORMED,
                tourService.searchTours(searchText)));
    }

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