package com.teameight.tourplanner.events;

/**
 * Interface für Event Listener
 */
public interface EventListener<T> {
    void onEvent(Event<T> event);
} 