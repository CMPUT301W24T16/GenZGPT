package com.example.genzgpt;

import java.util.ArrayList;

/**
 * Implements Attendee Role
 */

public class Attendee extends Role {
    private List<Event> registeredEvents;
    public Attendee(String id, String name, String email) {
        super("Attendee");
        this.registeredEvents = new ArrayList<>();
    }

    public void registerEvent(Event event) {
        registeredEvents.add(event);
    }

    public void checkIn(Event event) {
        // QR code scanning and check-in logic
    }

    // More Attendee methods here...
}