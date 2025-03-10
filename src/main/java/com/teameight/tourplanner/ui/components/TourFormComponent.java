package com.teameight.tourplanner.ui.components;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.ui.FXMLDependencyInjector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Consumer;


public class TourFormComponent extends VBox implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextArea descriptionField;
    
    @FXML
    private TextField originField;
    
    @FXML
    private TextField destinationField;
    
    @FXML
    private ComboBox<TransportType> transportTypeCombo;
    
    @FXML
    private TextField distanceField;
    
    @FXML
    private TextField estimatedTimeField;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label nameErrorLabel;
    
    @FXML
    private Label originErrorLabel;
    
    @FXML
    private Label destinationErrorLabel;
    
    @FXML
    private Label distanceErrorLabel;
    
    @FXML
    private Label estimatedTimeErrorLabel;
    
    private final ObjectProperty<Tour> tourProperty = new SimpleObjectProperty<>();
    
    private Consumer<Tour> onSave;
    private Runnable onCancel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transportTypeCombo.getItems().addAll(TransportType.values());
        
        
        setupValidation();
        
        // Tour-Eigenschaft an Formularfelder binden
        tourProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateForm(newValue);
            } else {
                clearForm();
            }
        });
        
        // Speichern-Button-Aktion festlegen
        saveButton.setOnAction(event -> handleSave());
        
        // Abbrechen-Button-Aktion festlegen
        cancelButton.setOnAction(event -> handleCancel());
        
        // Fehlermeldungen initialisieren (standardmäßig ausgeblendet)
        nameErrorLabel.setVisible(false);
        originErrorLabel.setVisible(false);
        destinationErrorLabel.setVisible(false);
        distanceErrorLabel.setVisible(false);
        estimatedTimeErrorLabel.setVisible(false);
    
        // Textfeld-Eigenschaften hinzufügen
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(newValue);
        });
        
        originField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateOrigin(newValue);
        });
        
        destinationField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateDestination(newValue);
        });
        
        distanceField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateDistance(newValue);
        });
        
        estimatedTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEstimatedTime(newValue);
        });
    }
    
    // Konstruktor, der die FXML-Datei lädt
    public TourFormComponent() {
        try {
            FXMLLoader loader = FXMLDependencyInjector.loader("components/tour-form-component.fxml", Locale.ENGLISH);
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TourFormComponent FXML", e);
        }
    }
    
    // Formular-Validierung einrichten
    private void setupValidation() {
        // Speichern-Button deaktivieren, wenn erforderliche Felder leer sind
        saveButton.disableProperty().bind(
            nameField.textProperty().isEmpty()
            .or(originField.textProperty().isEmpty())
            .or(destinationField.textProperty().isEmpty())
            .or(transportTypeCombo.valueProperty().isNull())
            .or(distanceField.textProperty().isEmpty())
            .or(estimatedTimeField.textProperty().isEmpty())
        );
    }
    
    // Formular mit Tour-Daten füllen
    private void populateForm(Tour tour) {
        nameField.setText(tour.getName());
        descriptionField.setText(tour.getDescription());
        originField.setText(tour.getOrigin());
        destinationField.setText(tour.getDestination());
        transportTypeCombo.setValue(tour.getTransportType());
        distanceField.setText(formatDistance(tour.getDistance()));
        estimatedTimeField.setText(formatTime(tour.getEstimatedTime()));
    }
    
    // Formularfelder löschen
    private void clearForm() {
        nameField.clear();
        descriptionField.clear();
        originField.clear();
        destinationField.clear();
        transportTypeCombo.setValue(null);
        distanceField.clear();
        estimatedTimeField.clear();
    }
    
    // Speichern-Button-Klick-Ereignis verarbeiten
    private void handleSave() {
        // Formular vor Speichern überprüfen
        if (validateForm()) {
            // Tour erstellen oder aktualisieren
            Tour tour = tourProperty.get();
            if (tour == null) {
                tour = new Tour();
                tour.setId(UUID.randomUUID().toString());
            }
            
            // Tour-Eigenschaften aus Formularfeldern setzen
            tour.setName(nameField.getText());
            tour.setDescription(descriptionField.getText());
            tour.setOrigin(originField.getText());
            tour.setDestination(destinationField.getText());
            tour.setTransportType(transportTypeCombo.getValue());
            tour.setDistance(distanceField.getText());
            tour.setEstimatedTime(estimatedTimeField.getText());
            
            // onSave-Callback aufrufen
            if (onSave != null) {
                onSave.accept(tour);
            }
        }
    }
    
    // Abbrechen-Button-Klick-Ereignis verarbeiten
    private void handleCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }
    
    // Formular-Eingaben überprüfen
    private boolean validateForm() {
        boolean isValid = true;
        
        // Jedes Feld überprüfen
        isValid &= validateName(nameField.getText());
        isValid &= validateOrigin(originField.getText());
        isValid &= validateDestination(destinationField.getText());
        isValid &= validateDistance(distanceField.getText());
        isValid &= validateEstimatedTime(estimatedTimeField.getText());
        
        return isValid;
    }
    
    // Name-Feld überprüfen
    private boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            nameErrorLabel.setText("Name is required");
            nameErrorLabel.setVisible(true);
            return false;
        } else if (name.length() < 3) {
            nameErrorLabel.setText("Name must be at least 3 characters");
            nameErrorLabel.setVisible(true);
            return false;
        } else {
            nameErrorLabel.setVisible(false);
            return true;
        }
    }
    
    // Ursprungsfeld überprüfen
    private boolean validateOrigin(String origin) {
        if (origin == null || origin.trim().isEmpty()) {
            originErrorLabel.setText("Origin is required");
            originErrorLabel.setVisible(true);
            return false;
        } else {
            originErrorLabel.setVisible(false);
            return true;
        }
    }
    
    // Zielfeld überprüfen
    private boolean validateDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            destinationErrorLabel.setText("Destination is required");
            destinationErrorLabel.setVisible(true);
            return false;
        } else {
            destinationErrorLabel.setVisible(false);
            return true;
        }
    }
    
    // Distanzfeld überprüfen
    private boolean validateDistance(String distance) {
        if (distance == null || distance.trim().isEmpty()) {
            distanceErrorLabel.setText("Distance is required");
            distanceErrorLabel.setVisible(true);
            return false;
        } 
        
        // Try to parse numeric part (remove any non-numeric chars except decimal point)
        String numericPart = distance.replaceAll("[^0-9.]", "");
        try {
            double value = Double.parseDouble(numericPart);
            if (value <= 0) {
                distanceErrorLabel.setText("Distance must be greater than 0");
                distanceErrorLabel.setVisible(true);
                return false;
            }
            // Add maximum distance validation (e.g., 20000 km for intercontinental routes)
            if (value > 20000) {
                distanceErrorLabel.setText("Distance cannot exceed 20000 km");
                distanceErrorLabel.setVisible(true);
                return false;
            }
            distanceErrorLabel.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            distanceErrorLabel.setText("Invalid distance format");
            distanceErrorLabel.setVisible(true);
            return false;
        }
    }
    
    // Zeitfeld überprüfen
    private boolean validateEstimatedTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            estimatedTimeErrorLabel.setText("Estimated time is required");
            estimatedTimeErrorLabel.setVisible(true);
            return false;
        }
        
        // Check for HH:MM format
        String[] parts = time.split(":");
        try {
            if (parts.length != 2) {
                throw new NumberFormatException();
            }
            
            int hours = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());
            
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                estimatedTimeErrorLabel.setText("Invalid time format (use HH:MM, 00:00-23:59)");
                estimatedTimeErrorLabel.setVisible(true);
                return false;
            }
            
            estimatedTimeErrorLabel.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            estimatedTimeErrorLabel.setText("Invalid time format (use HH:MM)");
            estimatedTimeErrorLabel.setVisible(true);
            return false;
        }
    }
    
    // Distanz mit keiner Einheit formatieren
    private String formatDistance(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        
        // Try to parse and format with km unit
        try {
            String numericPart = value.replaceAll("[^0-9.]", "");
            double distance = Double.parseDouble(numericPart);
            return String.format("%.1f km", distance);
        } catch (NumberFormatException ignored) {
            // If parsing fails, return original value
        }
        
        return value;
    }
    
    // Zeit in HH:MM-Format formatieren
    private String formatTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        
        // Try to parse and format as HH:MM
        try {
            String[] parts = value.split(":");
            if (parts.length == 2) {
                int hours = Integer.parseInt(parts[0].trim());
                int minutes = Integer.parseInt(parts[1].trim());
                return String.format("%02d:%02d", hours, minutes);
            }
        } catch (NumberFormatException ignored) {
            // If parsing fails, return original value
        }
        
        return value;
    }
    
    // Die zu bearbeitende Tour setzen
    public void setTour(Tour tour) {
        tourProperty.set(tour);
    }
    
    // Callback für Speichern-Aktion setzen
    public void setOnSave(Consumer<Tour> onSave) {
        this.onSave = onSave;
    }
    
    // Callback für Abbrechen-Aktion setzen
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }
} 