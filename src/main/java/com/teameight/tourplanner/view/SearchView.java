package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.SearchViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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
        setupBindings();
        setupKeyHandlers();
    }

    private void setupBindings() {
        // Bind search text to view model
        searchField.textProperty().bindBidirectional(viewModel.searchTextProperty());

        // Disable clear button when search field is empty
        clearButton.disableProperty().bind(searchField.textProperty().isEmpty());

        // Set tooltip for search field
        searchField.setPromptText("Search tours by name, description, location...");
    }

    private void setupKeyHandlers() {
        // Handle key events for better usability
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                handleClearSearch();
            }
        });
    }

    @FXML
    public void handleClearSearch() {
        viewModel.clearSearch();
        searchField.requestFocus(); // Return focus to search field after clearing
    }
}
