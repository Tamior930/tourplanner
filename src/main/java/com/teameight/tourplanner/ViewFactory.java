package com.teameight.tourplanner.ui;

import com.teameight.tourplanner.presentation.SearchViewModel;
import com.teameight.tourplanner.presentation.TourDetailsViewModel;
import com.teameight.tourplanner.presentation.TourListViewModel;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import com.teameight.tourplanner.ui.screens.MainScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

/**
 * Factory for creating views and managing their lifecycle
 */
public class ViewFactory {

    private final Locale locale;

    // ViewModels
    private final SearchViewModel searchViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;

    /**
     * Constructor initializes ViewModels
     */
    public ViewFactory() {
        this.locale = Locale.getDefault();

        // Create service instances
        TourService tourService = new TourServiceImpl();

        // Initialize ViewModels
        this.searchViewModel = new SearchViewModel(tourService);
        this.tourDetailsViewModel = new TourDetailsViewModel();
        this.tourListViewModel = new TourListViewModel(searchViewModel, tourService);

        // Initialize ViewModels
        this.searchViewModel.initialize();
        this.tourListViewModel.initialize();
        this.tourDetailsViewModel.initialize();
    }

    /**
     * Creates and shows the main application window
     */
    public void showMainWindow() {
        try {
            // Create loader with dependency injection
            FXMLLoader loader = FXMLDependencyInjector.loader("main-view.fxml", locale);

            // Create controller with dependencies
            MainScreenController controller = new MainScreenController(
                    tourListViewModel,
                    searchViewModel,
                    tourDetailsViewModel
            );

            // Set controller
            loader.setController(controller);

            // Load view
            Parent root = loader.load();

            // Create and configure stage
            Stage stage = new Stage();
            stage.setTitle("Tour Planner");
            stage.setScene(new Scene(root, 1000, 700));
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            // Show the window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a component view with the specified FXML and controller
     */
    public <T> Parent createComponentView(String fxmlPath, T controller) throws IOException {
        FXMLLoader loader = FXMLDependencyInjector.loader(fxmlPath, locale);
        loader.setController(controller);
        return loader.load();
    }
}