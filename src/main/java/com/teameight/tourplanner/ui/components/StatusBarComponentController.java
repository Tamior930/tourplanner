package com.teameight.tourplanner.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusBarComponentController implements Initializable {
    
    @FXML
    private Label statusLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisieren mit Standardstatus
        statusLabel.setText("Ready");
    }
    
    // Statusnachricht aktualisieren
    public void updateStatus(String status) {
        statusLabel.setText(status);
    }
} 