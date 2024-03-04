package com.example.genzgpt.Model;

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
        return new ArrayList<>(managedEvents); // Return copy to maintain encapsulation
    }

    // US 01.02.01 - View the list of attendees who have checked in to an event
    public List<User> viewCheckedInAttendees(Event event) {
        return event.getCheckedInAttendees();
    }

    // US 01.03.01 - Send notifications to all checked-in attendees
   /* public void sendPushNotification(Event event, String message) {
        List<User> checkedIn = event.getAttendees();
        NotificationService notificationService = new NotificationService();

        for (User u : checkedIn) {
            notificationService.sendPushNotificationToUser(u, message);
        }
    }*/

    // US 01.09.01 View how many times an attendee has checked into an event.
    public int getCheckInCount(User user, Event event) {
        return event.getCheckInCount(user);
    }

    // US 01.10.01 - View people signed up to attend
    public List<User> viewRegisteredInAttendees(Event event) {
        return event.getRegisteredAttendees();
    }

    // More Organizer methods here...
}