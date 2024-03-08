package com.example.genzgpt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.Organizer;
import com.example.genzgpt.Model.User;

class OrganizerTest {

    @Test
    void testAddEvent() {
        Organizer organizer = new Organizer();
        Event event = new Event(1, "Sample Event", null, "Sample Location", 50);
        organizer.addEvent(event);
        assertTrue(organizer.getManagedEvents().contains(event));
    }

    @Test
    void testViewCheckedInAttendees() {
        Organizer organizer = new Organizer();
        Event event = new Event(1, "Sample Event", null, "Sample Location", 50);
        organizer.addEvent(event);
        User attendee = new User("1", "John", "Doe", 1234567890, "john@example.com", false);
        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);
        assertEquals(organizer.viewCheckedInAttendees(event).size(), 1);
    }

    // Uncomment the test below if the sendPushNotification method is uncommented
    /*
    @Test
    void testSendPushNotification() {
        Organizer organizer = new Organizer();
        Event event = new Event(1, "Sample Event", null, "Sample Location", 50);
        organizer.addEvent(event);
        User attendee = new User("1", "John Doe", "john@example.com");
        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);
        String message = "Important Announcement!";
        organizer.sendPushNotification(event, message);
        // Add assertions related to the notification service or other behavior
    }
    */

    @Test
    void testGetCheckInCount() {
        Organizer organizer = new Organizer();
        Event event = new Event(1, "Sample Event", null, "Sample Location", 50);
        organizer.addEvent(event);
        User attendee = new User("1", "John", "Doe", 1234567890, "john@example.com", false);
        event.registerAttendee(attendee);
        event.checkInAttendee(attendee);
        assertEquals(organizer.getCheckInCount(attendee, event), 1);
    }

    @Test
    void testViewRegisteredInAttendees() {
        Organizer organizer = new Organizer();
        Event event = new Event(1, "Sample Event", null, "Sample Location", 50);
        organizer.addEvent(event);
        User attendee = new User("1", "John", "Doe", 1234567890, "john@example.com", false);
        event.registerAttendee(attendee);
        assertEquals(organizer.viewRegisteredInAttendees(event).size(), 1);
    }

    // Add more tests for additional Organizer methods if needed...
}
