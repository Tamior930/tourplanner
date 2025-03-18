package com.teameight.tourplanner.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private static final EventBus instance = new EventBus();

    private final Map<EventType, List<EventListener<?>>> listeners = new HashMap<>();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return instance;
    }

    public <T> void subscribe(EventType eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T> void unsubscribe(EventType eventType, EventListener<T> listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public <T> void publish(Event<T> event) {
        if (listeners.containsKey(event.getType())) {
            for (EventListener<?> listener : listeners.get(event.getType())) {
                ((EventListener<T>) listener).onEvent(event);
            }
        }
    }
} 