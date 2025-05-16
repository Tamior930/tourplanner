package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.MainViewModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    private final MainViewModel viewModel;

    public MainView(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
