package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.model.TransportType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class TourDetailsViewModel {

    private final StringProperty tourNameProperty = new SimpleStringProperty("");
    private final StringProperty tourDescriptionProperty = new SimpleStringProperty("");
    private final StringProperty tourOriginProperty = new SimpleStringProperty("");
    private final StringProperty tourDestinationProperty = new SimpleStringProperty("");
    private final StringProperty tourTransportTypeProperty = new SimpleStringProperty("");
    private final StringProperty tourDistanceProperty = new SimpleStringProperty("");
    private final StringProperty tourEstimatedTimeProperty = new SimpleStringProperty("");
    private final ObjectProperty<Image> tourMapImageProperty = new SimpleObjectProperty<>();

    public void initialize() {
        tourMapImageProperty.set(new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png")));
    }

    public void updateTourDetails(String name, String description, String origin, String destination,
                                  TransportType transportType, String distance, String estimatedTime, Image mapImage) {
        tourNameProperty.set(name);
        tourDescriptionProperty.set(description);
        tourOriginProperty.set(origin);
        tourDestinationProperty.set(destination);
        tourTransportTypeProperty.set(transportType != null ? transportType.toString() : "");
        tourDistanceProperty.set(distance);
        tourEstimatedTimeProperty.set(estimatedTime);

        if (mapImage != null) {
            tourMapImageProperty.set(mapImage);
        }
    }

    public void clearTourDetails() {
        tourNameProperty.set("");
        tourDescriptionProperty.set("");
        tourOriginProperty.set("");
        tourDestinationProperty.set("");
        tourTransportTypeProperty.set("");
        tourDistanceProperty.set("");
        tourEstimatedTimeProperty.set("");
        tourMapImageProperty.set(new Image(getClass().getResourceAsStream("/com/teameight/tourplanner/images/map-placeholder.png")));
    }

    public StringProperty tourNameProperty() {
        return tourNameProperty;
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescriptionProperty;
    }

    public StringProperty tourOriginProperty() {
        return tourOriginProperty;
    }

    public StringProperty tourDestinationProperty() {
        return tourDestinationProperty;
    }

    public StringProperty tourTransportTypeProperty() {
        return tourTransportTypeProperty;
    }

    public StringProperty tourDistanceProperty() {
        return tourDistanceProperty;
    }

    public StringProperty tourEstimatedTimeProperty() {
        return tourEstimatedTimeProperty;
    }

    public ObjectProperty<Image> tourMapImageProperty() {
        return tourMapImageProperty;
    }
} 