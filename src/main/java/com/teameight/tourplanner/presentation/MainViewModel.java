package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.Event;
import com.teameight.tourplanner.events.EventBus;
import com.teameight.tourplanner.events.EventType;
import com.teameight.tourplanner.model.Tour;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class MainViewModel {

    private final BooleanProperty tourFormVisibleProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty contentVisibleProperty = new SimpleBooleanProperty(true);

    public void initialize() {
        EventBus.getInstance().subscribe(EventType.TOUR_ADDED, this::handleShowAddTourForm);
        EventBus.getInstance().subscribe(EventType.TOUR_UPDATED, this::handleShowEditTourForm);
        EventBus.getInstance().subscribe(EventType.CLOSE_TOUR_FORM, this::handleCloseForm);
        EventBus.getInstance().subscribe(EventType.TOUR_CREATED, this::handleTourCreated);
    }

    private void handleShowAddTourForm(Event<?> event) {
        showForm();
    }

    private void handleShowEditTourForm(Event<?> event) {
        if (event.getData() instanceof Tour) {
            EventBus.getInstance().publish(new Event<>(EventType.TOUR_SELECTED_FOR_EDIT, event.getData()));
            showForm();
        }
    }

    private void handleCloseForm(Event<?> event) {
        hideForm();
    }

    private void handleTourCreated(Event<?> event) {
        hideForm();
    }

    private void showForm() {
        tourFormVisibleProperty.set(true);
        contentVisibleProperty.set(false);
    }

    private void hideForm() {
        tourFormVisibleProperty.set(false);
        contentVisibleProperty.set(true);
    }

    public BooleanProperty tourFormVisibleProperty() {
        return tourFormVisibleProperty;
    }

    public BooleanProperty contentVisibleProperty() {
        return contentVisibleProperty;
    }
} 