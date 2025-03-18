package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.SearchViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchView implements Initializable {
    private final SearchViewModel viewModel;

    @FXML
    private TextField searchField;
    
    @FXML
    private Button clearButton;

    public SearchView(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind the search field to the view model's searchText property
        searchField.textProperty().bindBidirectional(viewModel.searchTextProperty());
        
        // Disable clear button when search field is empty
        clearButton.disableProperty().bind(searchField.textProperty().isEmpty());
    }

    @FXML
    public void handleClearSearch() {
        viewModel.clearSearch();
    }
}
