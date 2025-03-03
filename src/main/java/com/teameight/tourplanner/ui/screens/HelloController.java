package com.teameight.tourplanner.ui.screens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.teameight.tourplanner.HelloApplication;
import com.teameight.tourplanner.presentation.SearchViewModel;
import com.teameight.tourplanner.presentation.TourListViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class HelloController implements Initializable {
    
    @FXML
    private VBox searchContainer;
    
    @FXML
    private ListView<String> tourList;
    
    @FXML
    private TextArea tourDetails;
    
    private SearchController searchController;
    private SearchViewModel searchViewModel;
    private TourListViewModel tourListViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // SearchViewModel erstellen und initialisieren
            searchViewModel = new SearchViewModel();
            searchViewModel.initialize();
            
            // TourListViewModel erstellen und initialisieren
            tourListViewModel = new TourListViewModel(searchViewModel);
            tourListViewModel.initialize();
            
            // Suchkomponente laden
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("search-view.fxml"));
            loader.load();
            searchController = loader.getController();
            
            // WICHTIG: Jetzt übergeben wir beide ViewModels an den SearchController
            searchController.initViewModel(searchViewModel, tourListViewModel);
            
            // Suchkomponente einfügen
            searchContainer.getChildren().add(searchController.getRoot());
            
            // UI-Elemente an ViewModel-Eigenschaften binden
            tourList.itemsProperty().bind(tourListViewModel.tourListProperty());
            tourDetails.textProperty().bind(tourListViewModel.selectedTourDetailsProperty());
            
            // Auswahllistener einrichten
            tourList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> tourListViewModel.setSelectedTour(newValue)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler für den Add Tour Button
     * Leere Implementierung - Button nur zur Demonstration
     */
    @FXML
    private void handleAddTour() {
        // Keine Aktion - Button nur für UI-Demonstration
        System.out.println("Add Tour button pressed - no action");
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