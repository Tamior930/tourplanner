package com.teameight.tourplanner.events;

public class Event<T> {
    private final EventType type;
    private final T data;

    public Event(EventType type, T data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType() {
        return type;
    }

    public T getData() {
        return data;
    }
} 