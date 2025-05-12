package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.TourListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

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
    public void initialize(URL location, ResourceBundle resources) {
        setupTourListView();
    }

    private void setupTourListView() {
        // Bind the list to the view model's data
        tourListView.setItems(viewModel.getTours());

        // Handle selection changes
        tourListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> viewModel.setSelectedTour(newValue)
        );

        // Configure how each tour is displayed in the list
        tourListView.setCellFactory(listView -> new TourListCell());
    }

    @FXML
    public void handleAddTour() {
        viewModel.addNewTour();
    }

    /**
     * Custom cell for displaying tours in the list
     */
    private static class TourListCell extends ListCell<Tour> {
        @Override
        protected void updateItem(Tour tour, boolean empty) {
            super.updateItem(tour, empty);

            if (empty || tour == null) {
                setText(null);
                setStyle("-fx-font-weight: normal;");
            } else {
                setText(tour.getName());

                // Optional: You could add styling for selected items here
                // if (isSelected()) setStyle("-fx-font-weight: bold;");
            }
        }
    }
}
