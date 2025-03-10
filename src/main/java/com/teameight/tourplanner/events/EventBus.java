package com.teameight.tourplanner.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event Bus für die Veröffentlichung und Anmeldung von Events
 */
public class EventBus {
    private static final EventBus instance = new EventBus();
    
    private final Map<EventType, List<EventListener<?>>> listeners = new HashMap<>();
    
    private EventBus() {
        // Privater Konstruktor für Singleton
    }
    
    public static EventBus getInstance() {
        return instance;
    }
    
    /**
     * Anmeldung für Events eines bestimmten Typs
     */
    public <T> void subscribe(EventType eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }
    
    /**
     * Abmeldung von Events eines bestimmten Typs
     */
    public <T> void unsubscribe(EventType eventType, EventListener<T> listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }
    
    /**
     * Event an alle Abonnenten veröffentlichen
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(Event<T> event) {
        if (listeners.containsKey(event.getType())) {
            for (EventListener<?> listener : listeners.get(event.getType())) {
                ((EventListener<T>) listener).onEvent(event);
            }
        }
    }
} 