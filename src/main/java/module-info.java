module com.teameight.tourplanner {
    requires java.net.http;

    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.swing;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires org.apache.logging.log4j;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;

    opens com.teameight.tourplanner to javafx.fxml;
    opens com.teameight.tourplanner.model;
    opens com.teameight.tourplanner.view to javafx.fxml;
    opens com.teameight.tourplanner.presentation;
    opens com.teameight.tourplanner.service;
    opens com.teameight.tourplanner.service.impl;
    opens com.teameight.tourplanner.service.openrouteservice;
    opens com.teameight.tourplanner.repository;
    opens com.teameight.tourplanner.provider;
    opens com.teameight.tourplanner.events;

    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.model;
    exports com.teameight.tourplanner.events;
    exports com.teameight.tourplanner.presentation;
    exports com.teameight.tourplanner.provider;
    exports com.teameight.tourplanner.repository;
    exports com.teameight.tourplanner.view;
    exports com.teameight.tourplanner.service;
    exports com.teameight.tourplanner.service.impl;
    exports com.teameight.tourplanner.service.openrouteservice;
}

Enhance application by adding map export functionality and integrating new components. 
Update module dependencies, refine ViewFactory, and improve event handling for map-related actions. 
Adjust UI elements in FXML files and update localization properties for new map features.