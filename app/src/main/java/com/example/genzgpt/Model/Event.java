package com.example.genzgpt.Model;

import com.example.genzgpt.Controller.AttendeeManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing an Event
 */
public class Event {
    private String eventId;
    private String eventName;
    private Date eventDate;
    private String location;
    private List<String> organizers;
    private final AttendeeManager attendeeManager;
    private final String imageURL;


    /**
     * Constructor to create a new Event.
     * @param eventId The ID of the event.
     * @param eventName The name of the event.
     * @param eventDate The date of the event.
     * @param location The location of the event.
     * @param maxAttendees The maximum number of attendees for the event, or null if no limit.
     */
    public Event(String eventId, String eventName, Date eventDate, String location, Integer maxAttendees, String imageURL) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.organizers = new ArrayList<>();
        this.attendeeManager = new AttendeeManager(maxAttendees); // Initialize AttendeeManager
        this.imageURL = imageURL;
    }

    /**
     * Adds an organizer to the event.
     *
     * @param userId The user to be added as an organizer.
     */
    public void addOrganizer(String userId) {
        organizers.add(userId);
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

    /**
     * A getter for the id of an event.
     * @return
     * The id of an event.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * A setter for the id of an event.
     * @param eventId
     * The id to set for an event.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * A getter for the name of the Event.
     * @return
     * The name of the Event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * A setter for the name of an Event.
     *
     * @param eventName
     * The name to set for the Event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * A getter for the Date of the Event.
     *
     * @return
     * The Date of the Event.
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * A setter for the Date of the Event.
     *
     * @param eventDate
     * The Date to set for the Event.
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * A getter for the Location of the Event (as a String).
     *
     * @return
     * The location of the event as a String.
     */
    public String getLocation() {
        return location;
    }

    /**
     * A setter for the String representing the location of the Event.
     *
     * @param location
     * The new location String to set for the Event.
     */
    public void setLocation(String location) { this.location = location; }


    // Getters for the lists (returns a copy for encapsulation)

    /**
     * A getter for the User data of the Organizers of an Event.
     *
     * @return
     * The list of Users considered Oragnizers of an Event.
     */
    public List<String> getOrganizers() {
        return organizers;
    }

    /**
     * A getter for User data of Attendees who have registered for an Event (as in promised to go).
     *
     * @return
     * A list of all User data relating to attendees registered for the Event.
     */
    public List<User> getRegisteredAttendees() {
        return attendeeManager.getRegisteredAttendees();
    }

    /**
     * A getter for the User data of checked in Attendees for an Event.
     *
     * @return
     * A list of User objects representing checked in Attendees.
     */
    public List<User> getCheckedInAttendees() {
        return attendeeManager.getCheckedInAttendees();
    }

    /**
     * A getter for the number of times a particular User has checked into an Event.
     *
     * @param user
     * The User to get the number of check-ins for.
     *
     * @return
     * The number of check-ins for a specific Event Attendee.
     */
    public int getCheckInCount(User user) {
        return attendeeManager.getCheckInCount(user);
    }

    /**
     * A getter for the number of registered attendees for an Event.
     *
     * @return
     * The number of registered Attendees for an Event.
     */
    public int getNumberOfRegisteredAttendees() {
        return attendeeManager.getNumberOfRegisteredAttendees();
    }

    /**
     * A getter for the number of attendees checked into an Event.
     *
     * @return
     * The number of checked-in Attendees for an Event.
     */
    public int getNumberOfCheckedInAttendees() {
        return attendeeManager.getNumberOfCheckedInAttendees();
    }

    /**
     * A getter for the assigned maximum number of Attendees for this Event.
     *
     * @return
     * The maximum number of Attendees for an Event.
     */
    public Integer getMaxAttendees() {
        return attendeeManager.getMaxAttendees();
    }

    /**
     * A setter for the maximum number of Attendees for an Event.
     *
     * @param maxAttendees
     * The limit for the number of Attendees that should be able to join an Event.
     */
    public void setMaxAttendees(Integer maxAttendees) {
        attendeeManager.setMaxAttendees(maxAttendees);
    }


    /**
     * Registers an Attendee with the Attendee Manager for this Event.
     *
     * @param user
     * The user to register for the Event.
     */
    public void registerAttendee(User user) {
        attendeeManager.registerAttendee(user);
    }

    /**
     * Checks in an Attendee using the Attendee Manager for this Event.
     *
     * @param user
     * The user to check in.
     */
    public void checkInAttendee(User user) {
        attendeeManager.checkInAttendee(user);
    }

    /**
     * Removes an Attendee from an Event.
     *
     * @param user
     * The User to remove.
     */
    public void removeAttendee(User user) {
        attendeeManager.removeAttendee(user);
    }

    /**
     * A getter for the ImageURL of an event.
     * @return
     * The imageUrl for an event.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * A setter for the checked in attendees of an event.
     * @param checkedInAttendees
     * The list of checked in attendees to set.
     */
    public void setCheckedInAttendees(List<User> checkedInAttendees) {
        attendeeManager.setCheckedInAttendees(checkedInAttendees);
    }

    /**
     * A setter for the registered attendees of an event.
     * @param registeredAttendees
     * The list of registered attendees to set.
     */
    public void setRegisteredAttendees(List<User> registeredAttendees) {
        attendeeManager.setRegisteredAttendees(registeredAttendees);
    }

    /**
     * A setter for the organizers of an event.
     * @param organizersId
     * The ids for all of the organizers of an event.
     */
    public void setOrganizers(List<String> organizersId) {
        this.organizers = organizersId;
    }

    /**
     * Converts the current event to a map.
     * @return
     * The current event data as a map.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventName", eventName);
        eventMap.put("eventDate", eventDate);
        eventMap.put("location", location);
        eventMap.put("maxAttendees", getMaxAttendees());
        eventMap.put("imageURL", imageURL);

        return eventMap;
    }

}