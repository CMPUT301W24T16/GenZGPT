package com.example.genzgpt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing an Event
 */
public class Event {
    private String eventId;
    private String eventName;
    private Date eventDate;
    private String location;
    private List<User> registeredAttendees; // Users who have registered for the event
    private List<User> checkedInAttendees;  // Users who have actually checked in

    /**
     * Constructor to create a new Event
     */
    public Event(String eventId, String eventName, Date eventDate, String location) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.registeredAttendees = new ArrayList<>();
        this.checkedInAttendees = new ArrayList<>();
    }

    /**
     * Registers a user for the event
     */
    public void registerAttendee(User user) {
        if (!registeredAttendees.contains(user)) {
            registeredAttendees.add(user);
        }
    }

    /**
     * Checks in a user for the event
     */
    public void checkInAttendee(User user) {
        if (registeredAttendees.contains(user) && !checkedInAttendees.contains(user)) {
            checkedInAttendees.add(user);
        }
    }

    // Getters and Setters

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<User> getRegisteredAttendees() {
        return new ArrayList<>(registeredAttendees);
    }

    public List<User> getCheckedInAttendees() {
        return new ArrayList<>(checkedInAttendees);
    }


}
