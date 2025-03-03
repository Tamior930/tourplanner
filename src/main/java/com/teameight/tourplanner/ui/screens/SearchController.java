package com.teameight.tourplanner.ui.screens;

import java.net.URL;
import java.util.ResourceBundle;

import com.teameight.tourplanner.presentation.SearchViewModel;
import com.teameight.tourplanner.presentation.TourListViewModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class SearchController implements Initializable {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private HBox searchBox;
    
    private SearchViewModel searchViewModel;
    private TourListViewModel tourListViewModel;
    
    public SearchController() {
        // Leerer Konstruktor für FXML-Loader
    }
    
    /**
     * Initialisiert den Controller mit einem SearchViewModel
     */
    public void initViewModel(SearchViewModel searchViewModel, TourListViewModel tourListViewModel) {
        this.searchViewModel = searchViewModel;
        this.tourListViewModel = tourListViewModel;
        
        // Bidirectional binding für das Suchfeld
        searchField.textProperty().bindBidirectional(searchViewModel.searchTextProperty());
        
        // Button-Status an das ViewModel binden
        clearButton.disableProperty().bind(searchViewModel.searchDisabledProperty());
        
        // Event-Handler für Tastatureingaben
        searchField.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Behandelt Tastatureingaben im Suchfeld
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !searchField.getText().trim().isEmpty()) {
            // Bei Enter einfach das Suchfeld leeren (ohne Tour hinzuzufügen)
            searchViewModel.search();
            
            event.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Wird vom FXML-Loader aufgerufen
        // ViewModel wird später gesetzt
    }
    
    /**
     * Handler für den Clear-Button
     * Leere Implementierung - Button nur zur Demonstration
     */
    @FXML
    private void handleClearSearch() {
        // Keine Aktion - Button nur für UI-Demonstration
        System.out.println("Clear button pressed - no action");
    }
    
    /**
     * Gibt das Root-Element für diese Komponente zurück
     */
    public HBox getRoot() {
        return searchBox;
    }
} 