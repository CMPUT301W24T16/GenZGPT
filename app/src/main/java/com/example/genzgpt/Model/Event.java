package com.example.genzgpt.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing an Event
 */
public class Event {
    private Integer eventId;
    private String eventName;
    private Date eventDate;
    private String location;
    private List<User> organizers;
    private List<User> registeredAttendees; // Users who have registered for the event
    private List<User> checkedInAttendees; // Users who have actually checked in

    private Map<String, Integer> checkedInAttendeesCount;  // Users who have actually checked inMap<String, Integer> attendeeCheckInCounts;


    /**
     * Constructor to create a new Event
     */
    public Event(Integer eventId, String eventName, Date eventDate, String location) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizers = new ArrayList<>();
        this.registeredAttendees = new ArrayList<>();
        this.checkedInAttendees = new ArrayList<>();
        this.checkedInAttendeesCount = new HashMap<>();
    }

    // Add an organizer to the event
    public void addOrganizer(User user) {
        if (!organizers.contains(user)) {
            organizers.add(user);
        }
    }

    // Registers a user for the event
    public void registerAttendee(User user) {
        if (!registeredAttendees.contains(user)) {
            registeredAttendees.add(user);
        }
    }

    // Checks in a user for the event
    public void checkInAttendee(User user) {
        String userId = user.getId();
        if (!checkedInAttendeesCount.containsKey(userId)) {
            checkedInAttendees.add(user); // Add to list only if not already checked in
        }
        checkedInAttendeesCount.put(userId, checkedInAttendeesCount.getOrDefault(userId, 0) + 1);
    }

    public void removeAttendee(User user) {
        registeredAttendees.remove(user);
        if (checkedInAttendeesCount.containsKey(user.getId())) {
            checkedInAttendees.remove(user);
            checkedInAttendeesCount.remove(user.getId());
        }
    }

    // Update event details
    public void updateEventDetails(String newName, Date newDate, String newLocation) {
        this.eventName = newName;
        this.eventDate = newDate;
        this.location = newLocation;
    }

    // Getters and Setters for event properties
    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
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

    public void setLocation(String location) { this.location = location; }

    // Getters for the lists (returns a copy for encapsulation)
    public List<User> getOrganizers() {
        return new ArrayList<>(organizers);
    }

    public List<User> getRegisteredAttendees() {
        return new ArrayList<>(registeredAttendees);
    }

    public List<User> getCheckedInAttendees() {
        return new ArrayList<>(checkedInAttendees);
    }

    // Additional useful methods
    public boolean isAttendeeCheckedIn(User user) {
        return checkedInAttendees.contains(user);
    }

    public boolean isUserRegistered(User user) {
        return registeredAttendees.contains(user);
    }

    public int getNumberOfRegisteredAttendees() {
        return registeredAttendees.size();
    }

    public int getNumberOfCheckedInAttendees() {
        return checkedInAttendees.size();
    }
    //method to get check-in count for a specific attendee
    public int getCheckInCount(User user) {
        return checkedInAttendeesCount.getOrDefault(user.getId(), 0);
    }
}