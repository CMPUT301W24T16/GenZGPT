package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.Organizer;
import com.example.genzgpt.Model.User;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizerTest {

    private Organizer organizer;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        organizer = new Organizer();
        event = new Event("0","Sample Event", new Date(), "Edmonton", 100, "event_poster.jpeg");
        user = new User("123", "John", "Doe", 1234567890L, "john.doe@example.com", true, "profile.jpg");
    }

    @Test
    void addEvent_shouldAddEventToList() {
        organizer.addEvent(event);
        List<Event> managedEvents = organizer.getManagedEvents();
        assertTrue(managedEvents.contains(event));
    }

    @Test
    void viewCheckedInAttendee() {
        Event event = createEventWithOrganizers();
        User attendee = new User("4", "Alice", "Johnson", 1234567890, "alice@example.com", false, "img");
        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);
        List<User> checkedInAttendees = event.getCheckedInAttendees();
        assertTrue(checkedInAttendees.contains(attendee));
    }

    @Test
    void getCheckInCount() {
        Event event = createEventWithOrganizers();
        User attendee = new User("4", "Alice", "Johnson", 1234567890, "alice@example.com", false, "img");
        event.registerAttendee(attendee);
        int checkInCount = organizer.getCheckInCount(user, event);
        assertEquals(0, checkInCount);
    }

    @Test
    void viewRegisteredInAttendees() {
        event.registerAttendee(user);

        List<User> registeredAttendees = organizer.viewRegisteredInAttendees(event);
        assertTrue(registeredAttendees.contains(user));
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
