module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    opens com.teameight.tourplanner to javafx.fxml;
    opens com.teameight.tourplanner.view to javafx.fxml;
    opens com.teameight.tourplanner.presentation to javafx.fxml, javafx.base;
    opens com.teameight.tourplanner.model to javafx.base;

    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.model;
    exports com.teameight.tourplanner.presentation;
    exports com.teameight.tourplanner.events;
    exports com.teameight.tourplanner.service;
    exports com.teameight.tourplanner.view;
    exports com.teameight.tourplanner.service.impl;
}