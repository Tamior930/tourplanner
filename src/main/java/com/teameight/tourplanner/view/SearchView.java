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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchField.textProperty().bindBidirectional(viewModel.searchTextProperty());
        clearButton.disableProperty().bind(viewModel.searchDisabledProperty());

        viewModel.initialize();
    }

    @FXML
    public void handleClearSearch() {
        viewModel.clearSearch();
    }
} 