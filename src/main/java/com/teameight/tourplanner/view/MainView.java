package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    private final MainViewModel viewModel;

    @FXML
    private BorderPane rootPane;

    public MainView(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind properties from the view model to the view
        // For now, we're just setting up the structure
    }
}
