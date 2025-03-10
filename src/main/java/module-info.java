module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base; // Das ist Property-Bindung


    opens com.teameight.tourplanner to javafx.fxml;
    opens com.teameight.tourplanner.ui.screens to javafx.fxml;
    opens com.teameight.tourplanner.ui.components to javafx.fxml;
    
    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.ui.screens;
    exports com.teameight.tourplanner.ui.components;
    exports com.teameight.tourplanner.model;
    exports com.teameight.tourplanner.presentation;
    exports com.teameight.tourplanner.events;
    exports com.teameight.tourplanner.service;
}