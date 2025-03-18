package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.*;
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
    private MenuItem helpMenuItem;
    
    @FXML
    private MenuItem aboutMenuItem;

    public NavbarView(NavbarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind menu item disable properties to the viewModel
        editTourMenuItem.disableProperty().bind(viewModel.tourSelectedProperty().not());
        deleteTourMenuItem.disableProperty().bind(viewModel.tourSelectedProperty().not());
    }

    @FXML
    public void handleNewTour() {
        viewModel.createNewTour();
    }

    @FXML
    public void handleEditTour() {
        viewModel.editSelectedTour();
    }

    @FXML
    public void handleDeleteTour() {
        viewModel.deleteSelectedTour();
    }

    @FXML
    public void handleExit() {
        viewModel.exitApplication();
    }

    @FXML
    public void handleHelp() {
        viewModel.showHelp();
    }

    @FXML
    public void handleAbout() {
        viewModel.showAbout();
    }
}
