package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class TourListViewModel {
    private final TourService tourService;
    private final SearchViewModel searchViewModel;
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourListViewModel(SearchViewModel searchViewModel, TourService tourService) {
        this.searchViewModel = searchViewModel;
        this.tourService = tourService;

        loadTours();

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
        openTourForm();
    }

    private void openTourForm() {
        try {
            Parent formView = FXMLDependencyInjector.load(
                    "components/tour-form.fxml",
                    Locale.ENGLISH
            );
            Stage formStage = new Stage();
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle("Tour");
            formStage.setScene(new Scene(formView));

            formStage.setMinWidth(500);
            formStage.setMinHeight(500);

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
