package com.teameight.tourplanner.ui.components;

import com.teameight.tourplanner.presentation.SearchViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchComponentController implements Initializable {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button clearButton;
    
    private SearchViewModel searchViewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Wird initialisiert, wenn die ViewModel gebunden wird
    }
    
    // Binden der ViewModel an das Komponente
    public void bindViewModel(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        
        // Binden der UI-Elemente an die ViewModel-Eigenschaften
        searchField.textProperty().bindBidirectional(searchViewModel.searchTextProperty());
        clearButton.disableProperty().bind(searchViewModel.searchDisabledProperty());
    }
    
    // Behandlung des Suchbuttons-Klick-Ereignisses
    @FXML
    private void handleSearch() {
        if (searchViewModel != null) {
            searchViewModel.search();
        }
    }
    
    // Behandlung des Suchbuttons-Klick-Ereignisses
    @FXML
    private void handleClearSearch() {
        if (searchViewModel != null) {
            searchViewModel.clearSearch();
        }
    }
} 