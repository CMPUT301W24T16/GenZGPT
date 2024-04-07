package com.example.genzgpt.Controller;


import com.example.genzgpt.Model.User;
import com.example.genzgpt.Model.Event;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

/**
 * This class manages a collection of events. It provides functionalities to add, update, and remove events,
 * as well as to manage event attendees by registering them, checking them in, and removing them from events.
 */
public class EventManager {
    private final List<Event> events;

    /**
     * Constructs an EventManager with an empty list of events.
     */
    public EventManager() {
        this.events = new ArrayList<>();
    }

    /**
     * Adds a new event to the collection.
     * @param event The event to be added.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Removes an event from the collection based on its event ID.
     * @param eventId The ID of the event to be removed.
     */
    public void removeEvent(Integer eventId) {
        events.removeIf(event -> Integer.parseInt(event.getEventId()) == (eventId));
    }


    /**
     * Finds an event by its ID.
     * @param eventId The ID of the event to find.
     * @return An Optional containing the found event if present, or an empty Optional if not found.
     */
    private Optional<Event> findEventById(Integer eventId) {
        return events.stream().filter(event -> Integer.parseInt(event.getEventId()) == (eventId)).findFirst();
    }

    /**
     * Retrieves a list of all events managed by this EventManager.
     * @return A list of all events.
     */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events); // Returns a copy to prevent modification
    }

    /**
     * Retrieves details of a specific event by its ID.
     * @param eventId The ID of the event to retrieve.
     * @return An Optional containing the event details if found, or an empty Optional if not found.
     */
    public Optional<Event> getEventDetails(Integer eventId) {
        return findEventById(eventId);
    }
}
