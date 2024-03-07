package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;

import org.junit.jupiter.api.Test;

import java.util.Date;

class EventTest {

    @Test
    void addOrganizer() {
        Event event = new Event(1, "Conference", new Date(), "Conference Hall");
        User organizer = new User("organizerId", "Organizer Name", "organizer@example.com");

        event.addOrganizer(organizer);

        assertTrue(event.getOrganizers().contains(organizer));
    }

    @Test
    void registerAttendeeWithinLimit() {
        Event event = new Event(2, "Workshop", new Date(), "Workshop Venue");
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");

        event.setMaxAttendees(50);
        event.registerAttendee(attendee);

        assertTrue(event.getRegisteredAttendees().contains(attendee));
    }

    @Test
    void checkInAttendee() {
        Event event = new Event(4, "Networking Event", new Date(), "Networking Venue");
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");

        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);

        assertTrue(event.isAttendeeCheckedIn(attendee));
        assertEquals(1, event.getCheckInCount(attendee));
    }

    @Test
    void removeAttendee() {
        Event event = new Event(5, "Panel Discussion", new Date(), "Discussion Venue");
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");

        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);

        event.removeAttendee(attendee);

        assertFalse(event.getRegisteredAttendees().contains(attendee));
        assertFalse(event.isAttendeeCheckedIn(attendee));
        assertEquals(0, event.getCheckInCount(attendee));
    }

    @Test
    void updateEventDetails() {
        Event event = new Event(6, "Old Event", new Date(), "Old Venue");

        event.updateEventDetails("New Event", new Date(), "New Venue");

        assertEquals("New Event", event.getEventName());
        assertEquals("New Venue", event.getLocation());
    }
}