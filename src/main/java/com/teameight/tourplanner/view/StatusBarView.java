package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.StatusBarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusBarView implements Initializable {

    private final StatusBarViewModel viewModel;

    @FXML
    private Label statusLabel;

    public StatusBarView(StatusBarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusLabel.textProperty().bind(viewModel.statusMessageProperty());

        viewModel.initialize();
    }
} 