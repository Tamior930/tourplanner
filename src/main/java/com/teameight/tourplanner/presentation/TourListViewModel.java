package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class TourListViewModel {
    private final ListProperty<Tour> tourListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Tour> selectedTourProperty = new SimpleObjectProperty<>();

    private final SearchViewModel searchViewModel;

    private final TourService tourService;

    public TourListViewModel(SearchViewModel searchViewModel, TourService tourService) {
        this.searchViewModel = searchViewModel;
        this.tourService = tourService;
    }

    public void initialize() {
        tourListProperty.set(tourService.getAllTours());

        EventBus.getInstance().subscribe(EventType.SEARCH_PERFORMED, this::handleSearchResults);

        EventBus.getInstance().subscribe(EventType.TOUR_CREATED, e -> refreshTours());
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, e -> refreshTours());
    }

    private void handleSearchResults(Event<?> event) {
        if (event.getData() instanceof java.util.List) {
            tourListProperty.clear();
            tourListProperty.addAll((java.util.List<Tour>) event.getData());
        }
    }

    public void refreshTours() {
        tourListProperty.setAll(tourService.getAllTours());
    }

    public void addTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
    }

    public void deleteTour(Tour tour) {
        if (tour != null) {
            boolean deleted = tourService.deleteTour(tour);
            if (deleted) {
                if (tour.equals(selectedTourProperty.get())) {
                    selectedTourProperty.set(null);
                }
    
                EventBus.getInstance().publish(new Event<>(EventType.TOUR_DELETED, tour));
            }
        }
    }

    public void editTour(Tour tour) {
        if (tour != null) {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, tour));
        }
    }

    public void selectTour(Tour tour) {
        selectedTourProperty.set(tour);
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_SELECTED, tour));
    }

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