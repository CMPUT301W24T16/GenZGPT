package com.example.genzgpt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    // Attributes of an event
    private String eventId;
    private String eventName;
    private Date eventDate;
    private String location;
    private List<Organizer> organizers;
    private List<Attendee> attendees;

    // Constructor to create a new event
    public Event(String eventId, String eventName, Date eventDate, String location) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizers = new ArrayList<>();
        this.attendees = new ArrayList<>();
    }

    // Getters and Setters for the event attributes
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

    public List<Organizer> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<Organizer> organizers) {
        this.organizers = organizers;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    // Method to add an organizer
    public void addOrganizer(Organizer organizer) {
        this.organizers.add(organizer);
    }

    public void updateEventDetails(String eventName, Date eventDate, String location) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
    }

}
