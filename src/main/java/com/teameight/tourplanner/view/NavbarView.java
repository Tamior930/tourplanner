package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.NavbarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class NavbarView implements Initializable {

    private final NavbarViewModel viewModel;

    @FXML
    private MenuItem newTourMenuItem;

    @FXML
    private MenuItem editTourMenuItem;

    @FXML
    private MenuItem deleteTourMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem helpMenuItem;

    public NavbarView(NavbarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel.initialize();
    }

    @FXML
    public void handleNewTour() {
        viewModel.createNewTour();
    }

    @FXML
    public void handleEditTour() {
        viewModel.editTour();
    }

    @FXML
    public void handleDeleteTour() {
        viewModel.deleteTour();
    }

    @FXML
    public void handleExit() {
        viewModel.exit();
    }

    @FXML
    public void handleAbout() {
        viewModel.showAbout();
    }

    @FXML
    public void handleHelp() {
        viewModel.help();
    }
} 