package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.NavbarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;


public class NavbarView implements Initializable {
    private final NavbarViewModel viewModel;

    // File menu items
    @FXML
    private MenuItem newTourMenuItem;
    @FXML
    private MenuItem editTourMenuItem;
    @FXML
    private MenuItem deleteTourMenuItem;
    @FXML
    private MenuItem importTourMenuItem;
    @FXML
    private MenuItem exportTourMenuItem;
    @FXML
    private MenuItem exitMenuItem;

    // Map menu items
    @FXML
    private MenuItem exportMapMenuItem;

    // Help menu items
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private MenuItem aboutMenuItem;

    public NavbarView(NavbarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMenuBindings();
    }

    private void setupMenuBindings() {
        // Disable edit and delete menu items when no tour is selected
        editTourMenuItem.disableProperty().bind(viewModel.tourSelectedProperty().not());
        deleteTourMenuItem.disableProperty().bind(viewModel.tourSelectedProperty().not());
        exportMapMenuItem.disableProperty().bind(viewModel.tourSelectedProperty().not());
        exportTourMenuItem.disableProperty().bind(viewModel.hasTourProperty().not());
    }

    // File menu handlers
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
    public void handleImportTours() {
        viewModel.importTours();
    }

    @FXML
    public void handleExportTours() {
        viewModel.exportTours();
    }

    @FXML
    public void handleExit() {
        viewModel.exitApplication();
    }

    // Map menu handlers
    @FXML
    public void handleMapExport() {
        viewModel.exportMap();
    }

    // Help menu handlers
    @FXML
    public void handleHelp() {
        viewModel.showHelp();
    }

    @FXML
    public void handleAbout() {
        viewModel.showAbout();
    }
}
