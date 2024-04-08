package com.example.genzgpt;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;

/**
 * Test Cases for the Event Class.
 */
class EventTest {

    @Test
    void testAddOrganizer() {
        Event event = createEventWithOrganizers();
        User newOrganizer = new User("3", "Jane", "Doe", 123456789, "jane@example.com", false, "img");
        event.addOrganizer(newOrganizer.getId());
        List<String> organizers = event.getOrganizers();
        assertTrue(organizers.contains(newOrganizer.getId()));
    }

    @Test
    void testUpdateEventDetails() {
        Event event = createEventWithOrganizers();
        event.updateEventDetails("Updated Event", new Date(), "New Location");
        assertEquals("Updated Event", event.getEventName());
        assertEquals("New Location", event.getLocation());
    }

    @Test
    void testRegisterAttendee() {
        Event event = createEventWithOrganizers();
        User attendee = new User("4", "Alice", "Johnson", 1234567890, "alice@example.com", false, "img");
        event.registerAttendee(attendee);
        List<User> registeredAttendees = event.getRegisteredAttendees();
        assertTrue(registeredAttendees.contains(attendee));
    }

    @Test
    void testCheckInAttendee() {
        Event event = createEventWithOrganizers();
        User attendee = new User("4", "Alice", "Johnson", 1234567890, "alice@example.com", false, "img");
        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);
        List<User> checkedInAttendees = event.getCheckedInAttendees();
        assertTrue(checkedInAttendees.contains(attendee));
    }

    @Test
    void testRemoveAttendee() {
        Event event = createEventWithOrganizers();
        User attendee = new User("4", "Alice", "Johnson", 1234567890, "alice@example.com", false, "img");
        event.registerAttendee(attendee);
        event.removeAttendee(attendee);
        List<User> registeredAttendees = event.getRegisteredAttendees();
        assertFalse(registeredAttendees.contains(attendee));
    }

    private Event createEventWithOrganizers() {
        Event event = new Event("1", "Sample Event", new Date(), "Sample Location", 50, "img");

        User organizer1 = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        User organizer2 = new User("2", "Bob", "Smith", 168402749, "bob@example.com", false, null);

        event.addOrganizer(organizer1.getId());
        event.addOrganizer(organizer2.getId());

        return event;
    }
}
