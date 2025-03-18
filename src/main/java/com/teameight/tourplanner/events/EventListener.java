package com.teameight.tourplanner.events;


public interface EventListener<T> {
    void onEvent(Event<T> event);
} 