package com.teameight.tourplanner.ui.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField searchField;
    
    @FXML
    private ListView<String> tourList;
    
    @FXML
    private TextArea tourDetails;

    @FXML
    public void initialize() {
        // test data
        tourList.getItems().addAll(
            "Test1",
            "Test2",
            "Test3"
        );

        // listener für Listenauswahl
        tourList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showTourDetails(newValue)
        );

        // Suchfunktion funktioniert hihi
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filterTours(newValue);
            }
        });
    }

    private void showTourDetails(String tourName) {
        if (tourName != null) {
            tourDetails.setText("Details for: " + tourName);
        }
    }

    private void filterTours(String searchText) {
        // Tour nach Suchtext filtern
        tourList.getItems().clear();
        var items = tourList.getItems();
        items.addAll(
            "Test1",
            "Test2",
            "Test3"
        );
        items.removeIf(tour -> !tour.toLowerCase().contains(searchText.toLowerCase()));
    }

    @FXML
    private void handleAddTour() {
        System.out.println("Add new tour");
    }

    @FXML
    private void handleNew(ActionEvent event) {
        System.out.println("New File action triggered");
    }

    @FXML
    private void handleOpen(ActionEvent event) {
        System.out.println("Open File action triggered");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        System.out.println("Save File action triggered");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        System.out.println("Help -> About action triggered");
    }

    @FXML
    private void handleUndo(ActionEvent event) {
        System.out.println("Undo action triggered");
    }

    @FXML
    private void handleRedo(ActionEvent event) {
        System.out.println("Redo action triggered");
    }

    @FXML
    private void handlePreferences(ActionEvent event) {
        System.out.println("Preferences action triggered");
    }
}