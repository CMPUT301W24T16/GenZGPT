package com.example.genzgpt;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Organizer Role
 */
public class Organizer extends Role {
    private List<Event> managedEvents;
    public Organizer() {
        super("Organizer");
        this.managedEvents = new ArrayList<>();
    }

    public void addEvent(Event event) {
        managedEvents.add(event);
    }

    public List<Event> getManagedEvents() {
        return new ArrayList<>(managedEvents); // Return a copy to maintain encapsulation
    }

    // More Organizer methods here...
}