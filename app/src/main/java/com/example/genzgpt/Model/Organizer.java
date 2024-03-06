package com.example.genzgpt.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Organizer role in the event management system.
 */
public class Organizer extends Role {
    private List<Event> managedEvents;
    public Organizer() {
        super("Organizer");
        this.managedEvents = new ArrayList<>();
    }

    /**
     * Adds an event to the list of managed events.
     *
     * @param event The event to be added.
     */
    public void addEvent(Event event) {
        managedEvents.add(event);
    }

    /**
     * Retrieves a copy of the list of events managed by the organizer.
     *
     * @return A new list containing the managed events.
     */
    public List<Event> getManagedEvents() {
        return new ArrayList<>(managedEvents); // Return copy to maintain encapsulation
    }

    /**
     * Retrieves the list of attendees who have checked in to a specific event.
     *
     * @param event The event to view checked-in attendees.
     * @return The list of checked-in attendees.
     */
    // US 01.02.01 - View the list of attendees who have checked in to an event
    public List<User> viewCheckedInAttendees(Event event) {
        return event.getCheckedInAttendees();
    }

    /**
     * Sends push notifications to all checked-in attendees of a specific event.
     *
     * @param event   The event for which notifications are sent.
     * @param message The message to be sent in the push notification.
     */
    // US 01.03.01 - Send notifications to all checked-in attendees
   /* public void sendPushNotification(Event event, String message) {
        List<User> checkedIn = event.getAttendees();
        NotificationService notificationService = new NotificationService();

        for (User u : checkedIn) {
            notificationService.sendPushNotificationToUser(u, message);
        }
    }*/

    /**
     * Retrieves the number of times an attendee has checked into a specific event.
     *
     * @param user  The attendee for whom the check-in count is retrieved.
     * @param event The event in question.
     * @return The number of times the attendee has checked in.
     */
    // US 01.09.01 View how many times an attendee has checked into an event.
    public int getCheckInCount(User user, Event event) {
        return event.getCheckInCount(user);
    }

    /**
     * Retrieves the list of people signed up to attend a specific event.
     *
     * @param event The event for which the list of registered attendees is retrieved.
     * @return The list of registered attendees.
     */
    // US 01.10.01 - View people signed up to attend
    public List<User> viewRegisteredInAttendees(Event event) {
        return event.getRegisteredAttendees();
    }

    // More Organizer methods here...
}