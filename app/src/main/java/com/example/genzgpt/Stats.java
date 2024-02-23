package com.example.genzgpt;

/**
 * Class for providing statistics about an Event.
 */
public class Stats {

    /**
     * Gets the total number of registered attendees for a given event.
     *
     * @param event The event to get the stats for.
     * @return The number of registered attendees.
     */
    public static int getTotalRegisteredAttendees(Event event) {
        return event.getRegisteredAttendees().size();
    }

    /**
     * Gets the total number of attendees who have checked in for a given event.
     *
     * @param event The event to get the stats for.
     * @return The number of attendees who have checked in.
     */
    public static int getTotalCheckedInAttendees(Event event) {
        return event.getCheckedInAttendees().size();
    }


}
