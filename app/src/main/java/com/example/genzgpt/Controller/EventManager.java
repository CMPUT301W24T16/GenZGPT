package com.example.genzgpt.Controller;


import com.example.genzgpt.Model.User;
import com.example.genzgpt.Model.Event;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

/**
 * Manages a collection of events.
 */
public class EventManager {
    private List<Event> events;

    public EventManager() {
        this.events = new ArrayList<>();
    }

    // Add a new event
    public void addEvent(Event event) {
        events.add(event);
    }

    // Remove an event
    public void removeEvent(Integer eventId) {
        events.removeIf(event -> event.getEventId().equals(eventId));
    }

    // Update an existing event
    public void updateEvent(Integer eventId, String newName, Date newDate, String newLocation) {
        findEventById(eventId).ifPresent(event -> event.updateEventDetails(newName, newDate, newLocation));
    }

    // Register an attendee to an event
    public void registerAttendeeToEvent(Integer eventId, User attendee) {
        findEventById(eventId).ifPresent(event -> event.registerAttendee(attendee));
    }

    // Check in an attendee to an event
    public void checkInAttendeeToEvent(Integer eventId, User attendee) {
        findEventById(eventId).ifPresent(event -> event.checkInAttendee(attendee));
    }

    // Remove an attendee from an event
    public void removeAttendeeFromEvent(Integer eventId, User attendee) {
        findEventById(eventId).ifPresent(event -> event.removeAttendee(attendee));
    }

    // Helper method to find an event by ID
    private Optional<Event> findEventById(Integer eventId) {
        return events.stream().filter(event -> event.getEventId().equals(eventId)).findFirst();
    }

    // Additional methods as needed
    // - Get a list of all events
    // - Get details of a specific event
    // - Methods to manage organizers of events, etc.
}
