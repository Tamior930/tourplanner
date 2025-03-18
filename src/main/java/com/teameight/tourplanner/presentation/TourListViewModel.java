package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourListViewModel {
    private final TourService tourService;
    private final SearchViewModel searchViewModel;
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourListViewModel(SearchViewModel searchViewModel, TourService tourService) {
        this.searchViewModel = searchViewModel;
        this.tourService = tourService;
        
        // Load initial tours
        loadTours();
        
        // Subscribe to events
        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, event -> {
            loadTours();
        });
        
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, event -> {
            loadTours();
        });
        
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, event -> {
            loadTours();
        });
        
        EventBus.getInstance().subscribe(EventType.SEARCH_TOURS, event -> {
            String searchQuery = (String) event.getData();
            if (searchQuery == null || searchQuery.isEmpty()) {
                loadTours();
            } else {
                searchTours(searchQuery);
            }
        });
        
        // When a tour is selected, publish an event
        selectedTour.addListener((observable, oldValue, newValue) -> {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_SELECTED, newValue));
        });
    }

    private void loadTours() {
        tours.setAll(tourService.getAllTours());
    }

    private void searchTours(String query) {
        tours.setAll(tourService.searchTours(query));
    }

    public void addNewTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public ObjectProperty<Tour> selectedTourProperty() {
        return selectedTour;
    }

    public Tour getSelectedTour() {
        return selectedTour.get();
    }

    public void setSelectedTour(Tour tour) {
        selectedTour.set(tour);
    }
}
