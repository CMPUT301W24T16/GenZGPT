package com.example.genzgpt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Attendee;
import com.example.genzgpt.Model.Event;

import java.util.Date;

class AttendeeTest {

    @Test
    void registerForEvent() {
        Attendee attendee = new Attendee("1", "John Doe", "john@example.com");
        Event event = new Event(1, "Conference", new Date(), "Edmonton");

        attendee.registerEvent(event);

        assertTrue(attendee.getRegisteredEvents().contains(event));
        assertTrue(event.getRegisteredAttendees().contains(attendee));
    }

    @Test
    void checkInForEvent() {
        Attendee attendee = new Attendee("1", "Jane Smith", "jane@example.com");
        Event event = new Event(1, "Conference", new Date(), "Edmonton");

        attendee.registerEvent(event);
        attendee.checkIn(event);

        assertTrue(attendee.getCheckedInEvents().contains(event));
        assertTrue(event.getCheckedInAttendees().contains(attendee));
    }

    @Test
    void checkInWithoutRegistration() {
        Attendee attendee = new Attendee("1", "Bob Johnson", "bob@example.com");
        Event event = new Event(5, "Panel Discussion", new Date(), "Vancouver");

        assertThrows(IllegalStateException.class, () -> attendee.checkIn(event));
        assertFalse(attendee.getCheckedInEvents().contains(event));
    }

    @Test
    void multipleEventRegistrations() {
        Attendee attendee = new Attendee("1", "Alice Brown", "alice@example.com");
        Event event1 = new Event(4, "Networking Event", new Date(), "Toronto");
        Event event2 = new Event(5, "Panel Discussion", new Date(), "Vancouver");

        attendee.registerEvent(event1);
        attendee.registerEvent(event2);

        assertTrue(attendee.getRegisteredEvents().contains(event1));
        assertTrue(attendee.getRegisteredEvents().contains(event2));
        assertTrue(event1.getRegisteredAttendees().contains(attendee));
        assertTrue(event2.getRegisteredAttendees().contains(attendee));
    }

    @Test
    void registerForSameEventMultipleTimes() {
        Attendee attendee = new Attendee("1", "Charlie Green", "charlie@example.com");
        Event event = new Event(6, "Seminar", new Date(), "Calgary");

        attendee.registerEvent(event);

        assertThrows(IllegalStateException.class, () -> {
            attendee.registerEvent(event);
        });
    }

    @Test
    void checkInForSameEventMultipleTimes() {
        Attendee attendee = new Attendee("1", "David White", "david@example.com");
        Event event = new Event(7, "Workshop", new Date(), "Montreal");

        attendee.registerEvent(event);
        attendee.checkIn(event);

        assertThrows(IllegalStateException.class, () -> {
            attendee.checkIn(event);
        });
    }
}