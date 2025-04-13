module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.teameight.tourplanner to javafx.fxml;
    opens com.teameight.tourplanner.view to javafx.fxml;
    opens com.teameight.tourplanner.model;

    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.model;
    exports com.teameight.tourplanner.presentation;
    exports com.teameight.tourplanner.events;
    exports com.teameight.tourplanner.service;
    exports com.teameight.tourplanner.view;
    exports com.teameight.tourplanner.impl;
    exports com.teameight.tourplanner.repository;
}