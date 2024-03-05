package com.example.genzgpt.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Attendee role in the event management system.
 * */

public class Attendee extends Role {
    private List<Event> registeredEvents;
    public Attendee(String id, String name, String email) {
        super("Attendee");
        this.registeredEvents = new ArrayList<>();
    }

    /**
     * Registers the attendee for a specific event.
     *
     * @param event The event to be registered.
     */
    public void registerEvent(Event event) {
        registeredEvents.add(event);
    }

    /**
     * Simulates the check-in process for an attendee at a specific event.
     * Includes QR code scanning and check-in logic.
     *
     * @param event The event for which the attendee is checking in.
     */
    public void checkIn(Event event) {
        // QR code scanning and check-in logic
    }

    // More Attendee methods here...
}