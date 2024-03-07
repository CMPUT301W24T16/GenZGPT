package com.example.genzgpt.Model;

import com.example.genzgpt.Controller.AttendeeManager;

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
    private AttendeeManager attendeeManager;

    /**
     * Constructor to create a new Event.
     * @param eventId The ID of the event.
     * @param eventName The name of the event.
     * @param eventDate The date of the event.
     * @param location The location of the event.
     * @param maxAttendees The maximum number of attendees for the event, or null if no limit.
     */
    public Event(Integer eventId, String eventName, Date eventDate, String location, Integer maxAttendees) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizers = new ArrayList<>();
        this.attendeeManager = new AttendeeManager(maxAttendees); // Initialize AttendeeManager
    }

    /**
     * Adds an organizer to the event.
     *
     * @param user The user to be added as an organizer.
     */
    public void addOrganizer(User user) {
        if (!organizers.contains(user)) {
            organizers.add(user);
        }
    }


    /**
     * Updates the details of the event, including its name, date, and location.
     *
     * @param newName     The new name of the event.
     * @param newDate     The new date and time of the event.
     * @param newLocation The new location of the event.
     */
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
        return attendeeManager.getRegisteredAttendees();
    }

    public List<User> getCheckedInAttendees() {
        return attendeeManager.getCheckedInAttendees();
    }

    public int getCheckInCount(User user) {
        return attendeeManager.getCheckInCount(user);
    }

    public int getNumberOfRegisteredAttendees() {
        return attendeeManager.getNumberOfRegisteredAttendees();
    }

    public int getNumberOfCheckedInAttendees() {
        return attendeeManager.getNumberOfCheckedInAttendees();
    }

    public Integer getMaxAttendees() {
        return attendeeManager.getMaxAttendees();
    }

    public void setMaxAttendees(Integer maxAttendees) {
        attendeeManager.setMaxAttendees(maxAttendees);
    }

    public void registerAttendee(User user) {
        attendeeManager.registerAttendee(user);
    }

    public void checkInAttendee(User user) {
        attendeeManager.checkInAttendee(user);
    }

    public void removeAttendee(User user) {
        attendeeManager.removeAttendee(user);
    }



}