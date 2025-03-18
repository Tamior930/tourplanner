package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.*;
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
        // Bind the list view to the view model's tours list
        tourListView.setItems(viewModel.getTours());
        
        // Bind the selected item to the view model's selectedTour property
        tourListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedTour(newValue);
        });
        
        // Set cell factory to display tour names
        tourListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tour tour, boolean empty) {
                super.updateItem(tour, empty);
                if (empty || tour == null) {
                    setText(null);
                } else {
                    setText(tour.getName());
                }
            }
        });
    }

    @FXML
    public void handleAddTour() {
        viewModel.addNewTour();
    }
}
