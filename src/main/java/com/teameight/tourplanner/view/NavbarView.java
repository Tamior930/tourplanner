package com.teameight.tourplanner.view;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.presentation.NavbarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class NavbarView implements Initializable {
    private final NavbarViewModel viewModel;
    private final EventBus eventBus;

    @FXML
    private MenuItem newTourMenuItem;

    @FXML
    private MenuItem editTourMenuItem;

    @FXML
    private MenuItem deleteTourMenuItem;

    @FXML
    private MenuItem exportMapMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    public NavbarView(NavbarViewModel viewModel) {
        this.viewModel = viewModel;
        this.eventBus = EventBus.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    public void handleExportMap() {
        // Trigger map export via event system
        eventBus.publish(new Event<>(EventType.MAP_EXPORT_REQUESTED, null));
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
