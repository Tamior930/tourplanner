module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires java.desktop;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.teameight.tourplanner to javafx.fxml;
    opens com.teameight.tourplanner.view to javafx.fxml;
    opens com.teameight.tourplanner.model;
    opens com.teameight.tourplanner.service.impl to com.fasterxml.jackson.databind;

    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.model;
    exports com.teameight.tourplanner.presentation;
    exports com.teameight.tourplanner.events;
    exports com.teameight.tourplanner.service;
    exports com.teameight.tourplanner.service.impl;
    exports com.teameight.tourplanner.view;
    exports com.teameight.tourplanner.impl;
    exports com.teameight.tourplanner.repository;
    exports com.teameight.tourplanner.provider;
}