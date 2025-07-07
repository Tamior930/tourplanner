package com.teameight.tourplanner.view;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.presentation.TourListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
        
        // Removed custom cell factory to use default toString()
    }

    @FXML
    public void handleAddTour() {
        viewModel.addNewTour();
    }
}
