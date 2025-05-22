package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.FXMLDependencyInjector;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.service.ReportService;
import com.teameight.tourplanner.service.TourImportExportService;
import com.teameight.tourplanner.service.TourService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NavbarViewModel {
    private final TourService tourService;
    private final EventManager eventManager;
    private final TourImportExportService importExportService;
    private final ReportService reportService;

    private final BooleanProperty tourSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty hasTour = new SimpleBooleanProperty(false);
    private Tour selectedTour;

    public NavbarViewModel(TourService tourService, EventManager eventManager, TourImportExportService importExportService, ReportService reportService) {
        this.tourService = tourService;
        this.eventManager = eventManager;
        this.importExportService = importExportService;
        this.reportService = reportService;

        eventManager.subscribe(Events.TOUR_SELECTED, this::handleTourSelected);

        // Check if there are any tours initially and update hasTour property
        updateHasTourProperty();

        // Listen for changes to the tour list to update hasTour property
        tourService.getAllTours().addListener((ListChangeListener<Tour>) change -> {
            updateHasTourProperty();
        });
    }

    private void updateHasTourProperty() {
        hasTour.set(!tourService.getAllTours().isEmpty());
    }

    private void handleTourSelected(String tourId) {
        if (tourId != null && !tourId.isEmpty()) {
            selectedTour = tourService.getTourById(tourId);
            tourSelected.set(selectedTour != null);
        } else {
            selectedTour = null;
            tourSelected.set(false);
        }
    }

    public void createNewTour() {
        eventManager.publish(Events.TOUR_ADDED, null);
        openTourForm();
    }

    public void editSelectedTour() {
        if (selectedTour != null) {
            eventManager.publish(Events.TOUR_UPDATED, selectedTour.getId());
            openTourForm();
        }
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            String tourId = selectedTour.getId();
            tourService.deleteTour(selectedTour);
            eventManager.publish(Events.TOUR_DELETED, tourId);
            selectedTour = null;
            tourSelected.set(false);
        }
    }

    public void importTours() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tours with Logs");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));

        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            eventManager.publish(Events.TOUR_IMPORT, null);

            boolean success = importExportService.importToursWithLogs(selectedFile);

            if (success) {
                // Explicitly refresh tour data from database
                tourService.refreshTours();

                // Explicitly publish events to refresh the UI
                eventManager.publish(Events.TOUR_ADDED, "imported");
                eventManager.publish(Events.TOUR_LOG_ADDED, "imported");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Import Successful");
                alert.setHeaderText(null);
                alert.setContentText("Tours with logs successfully imported.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Import Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to import tours with logs from the file.");
                alert.showAndWait();
            }
        }
    }

    public void exportTours() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Tours with Logs");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        fileChooser.setInitialFileName("tours.json");

        Stage stage = new Stage();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            // Publish event before export
            eventManager.publish(Events.TOUR_EXPORT, null);

            List<Tour> allTours = tourService.getAllTours();
            boolean success = importExportService.exportToursWithLogs(allTours, selectedFile);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Tours with logs successfully exported.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to export tours with logs to the file.");
                alert.showAndWait();
            }
        }
    }

    public void generateTourReport() {
        if (selectedTour != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Generate Tour Report");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
            fileChooser.setInitialFileName(selectedTour.getName() + "_report.pdf");

            Stage stage = new Stage();
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                boolean success = reportService.generateTourReport(selectedTour, selectedFile);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Report Generated");
                    alert.setHeaderText(null);
                    alert.setContentText("Tour report successfully generated.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Report Generation Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to generate tour report.");
                    alert.showAndWait();
                }
            }
        }
    }

    public void generateSummaryReport() {
        List<Tour> allTours = tourService.getAllTours();
        if (!allTours.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Generate Summary Report");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf"));
            fileChooser.setInitialFileName("tours_summary_report.pdf");

            Stage stage = new Stage();
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                boolean success = reportService.generateSummarizeReport(allTours, selectedFile);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Report Generated");
                    alert.setHeaderText(null);
                    alert.setContentText("Summary report successfully generated.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Report Generation Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to generate summary report.");
                    alert.showAndWait();
                }
            }
        }
    }

    public void exportMap() {
        if (selectedTour != null) {
            eventManager.publish(Events.MAP_EXPORT_CLICKED, selectedTour.getId());
        }
    }

    private void openTourForm() {
        try {
            Parent formView = FXMLDependencyInjector.load(
                    "components/tour-form.fxml",
                    Locale.ENGLISH
            );

            Stage formStage = new Stage();
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle(selectedTour != null ?
                    "Edit Tour: " + selectedTour.getName() :
                    "Create New Tour");

            formStage.setScene(new Scene(formView));
            formStage.setMinWidth(600);
            formStage.setMinHeight(500);
            formStage.centerOnScreen();

            formStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Could not open tour form");
        }
    }

    public void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Tour Planner Help");
        alert.setContentText(
                "This application helps you plan and manage your tours.\n\n" +
                        "• Create new tours with the 'Add Tour' button\n" +
                        "• Select a tour to view its details\n" +
                        "• Edit or delete tours using the menu options\n" +
                        "• Search for tours using the search bar\n" +
                        "• Import/Export tours using the File menu\n" +
                        "• Generate reports using the Reports menu"
        );

        alert.showAndWait();
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Tour Planner");
        alert.setContentText("Version 1.0\nDeveloped by Berkant and Ereza");

        alert.showAndWait();
    }

    public void exitApplication() {
        System.exit(0);
    }

    public BooleanProperty tourSelectedProperty() {
        return tourSelected;
    }

    public BooleanProperty hasTourProperty() {
        return hasTour;
    }
}
