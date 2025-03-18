package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatusBarViewModel {

    private final StringProperty statusMessageProperty = new SimpleStringProperty("Ready");

    public void initialize() {
        EventBus.getInstance().subscribe(EventType.STATUS_UPDATED, this::handleStatusUpdate);
    }

    private void handleStatusUpdate(Event<?> event) {
        if (event.getData() instanceof String) {
            statusMessageProperty.set((String) event.getData());
        }
    }

    public StringProperty statusMessageProperty() {
        return statusMessageProperty;
    }
}