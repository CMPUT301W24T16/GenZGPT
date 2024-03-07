package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.Organizer;
import com.example.genzgpt.Model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

class OrganizerTest {

    private Organizer organizer;
    private Event event;

    @BeforeEach
    void setUp() {
        organizer = new Organizer();
        event = new Event(1, "Conference", new Date(), "Conference Hall");
    }

    @Test
    void testAddEvent() {
        organizer.addEvent(event);
        List<Event> managedEvents = organizer.getManagedEvents();
        assertTrue(managedEvents.contains(event));
    }

    @Test
    void testGetManagedEvents() {
        organizer.addEvent(event);
        List<Event> managedEvents = organizer.getManagedEvents();
        assertEquals(1, managedEvents.size());
        assertEquals(event, managedEvents.get(0));
    }

    @Test
    void testViewCheckedInAttendees() {
        User attendee1 = new User("attendeeId", "Attendee Name", "attendee@example.com");
        User attendee2 = new User("attendeeId2", "John Doe", "doe@john.com");
        event.checkInAttendee(attendee1);
        event.checkInAttendee(attendee2);

        List<User> checkedInAttendees = organizer.viewCheckedInAttendees(event);
        assertEquals(2, checkedInAttendees.size());
        assertTrue(checkedInAttendees.contains(attendee1));
        assertTrue(checkedInAttendees.contains(attendee2));
    }

    @Test
    void testGetCheckInCount() {
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");
        event.checkInAttendee(attendee);

        int checkInCount = organizer.getCheckInCount(attendee, event);
        assertEquals(1, checkInCount);
    }

    @Test
    void testViewRegisteredInAttendees() {
        User attendee1 = new User("attendeeId", "Attendee Name", "attendee@example.com");
        User attendee2 = new User("attendeeId2", "John Doe", "doe@john.com");
        event.registerAttendee(attendee1);
        event.registerAttendee(attendee2);

        List<User> registeredAttendees = organizer.viewRegisteredInAttendees(event);
        assertEquals(2, registeredAttendees.size());
        assertTrue(registeredAttendees.contains(attendee1));
        assertTrue(registeredAttendees.contains(attendee2));
    }

    @Test
    void testViewCheckedInAttendeesEmptyEvent() {
        // Test when there are no checked-in attendees
        List<User> checkedInAttendees = organizer.viewCheckedInAttendees(event);
        assertTrue(checkedInAttendees.isEmpty());
    }

    @Test
    void testGetCheckInCountNoCheckIn() {
        // Test when the user hasn't checked in
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");
        int checkInCount = organizer.getCheckInCount(attendee, event);
        assertEquals(0, checkInCount);
    }

    @Test
    void testGetCheckInCountForMultipleCheckIns() {
        User attendee = new User("attendeeId", "Attendee Name", "attendee@example.com");
        event.checkInAttendee(attendee);
        event.checkInAttendee(attendee);

        int checkInCount = organizer.getCheckInCount(attendee, event);
        assertEquals(2, checkInCount);
    }


    // Additional tests for other methods can be added here...

}
