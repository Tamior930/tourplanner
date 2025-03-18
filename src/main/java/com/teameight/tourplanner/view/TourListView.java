package com.teameight.tourplanner.view;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.TourListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class TourListView implements Initializable {

    private final TourListViewModel viewModel;

    @FXML
    private ListView<Tour> tourListView;

    @FXML
    private Button addTourButton;

    public TourListView(TourListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tourListView.setItems(viewModel.tourListProperty());

        tourListView.setCellFactory(param -> new ListCell<>() {
            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem editItem = new MenuItem("Edit");
            private final MenuItem deleteItem = new MenuItem("Delete");

            {
                editItem.setOnAction(event -> {
                    Tour selectedTour = getItem();
                    if (selectedTour != null) {
                        EventBus.getInstance().publish(new Event<>(EventType.TOUR_UPDATED, selectedTour));
                    }
                });

                deleteItem.setOnAction(event -> {
                    Tour selectedTour = getItem();
                    if (selectedTour != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Tour");
                        alert.setHeaderText("Delete Tour: " + selectedTour.getName());
                        alert.setContentText("Are you sure you want to delete this tour?");

                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                viewModel.deleteTour(selectedTour);
                            }
                        });
                    }
                });

                contextMenu.getItems().addAll(editItem, deleteItem);
            }

            @Override
            protected void updateItem(Tour item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setContextMenu(null);
                } else {
                    setText(item.getName());
                    setContextMenu(contextMenu);
                }
            }
        });

        tourListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedTour(newValue);

            EventBus.getInstance().publish(new Event<>(EventType.TOUR_SELECTED, newValue));
        });

        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, this::handleTourAddedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, this::handleTourUpdatedEvent);
        EventBus.getInstance().subscribe(EventType.TOUR_DELETED, this::handleTourDeletedEvent);

        viewModel.initialize();
    }

    @FXML
    public void handleAddTour() {
        EventBus.getInstance().publish(new Event<>(EventType.TOUR_ADDED, null));
    }

    private void handleTourAddedEvent(Event<?> event) {

    }

    private void handleTourUpdatedEvent(Event<?> event) {
        Tour selectedTour = tourListView.getSelectionModel().getSelectedItem();
        if (selectedTour == null) {

        }
    }

    private void handleTourDeletedEvent(Event<?> event) {

        tourListView.getSelectionModel().clearSelection();
        viewModel.refreshTours();
    }
} 