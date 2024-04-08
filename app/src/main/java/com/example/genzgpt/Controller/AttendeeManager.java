package com.example.genzgpt.Controller;

import com.example.genzgpt.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages attendees for a given event.
 */
public class AttendeeManager {
    private List<User> registeredAttendees = new ArrayList<>();
    private List<User> checkedInAttendees = new ArrayList<>();
    private Map<String, Integer> checkedInAttendeesCount = new HashMap<>();
    private Integer maxAttendees; // Nullable, if null, no limit is imposed

    /**
     * Constructs an AttendeeManager with a specified maximum number of attendees.
     * @param maxAttendees The maximum number of attendees for the event, or null if no limit.
     */
    public AttendeeManager(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    /**
     * Registers a user as an attendee of the event, if the maximum attendee limit has not been reached.
     * @param user The user to be registered.
     */
    public void registerAttendee(User user){
        if (maxAttendees == null || registeredAttendees.size() < maxAttendees) {
            if (!registeredAttendees.contains(user)) {
                registeredAttendees.add(user);
            }
        }
    }


    /**
     * Checks in an attendee to the event.
     * @param user The user to be checked in.
     */
    public void checkInAttendee(User user) {
        if (!registeredAttendees.contains(user)) {
            return; // Can only check in registered attendees
        }
        if (!checkedInAttendeesCount.containsKey(user.getId())) {
            checkedInAttendees.add(user);
        }
        checkedInAttendeesCount.put(user.getId(), checkedInAttendeesCount.getOrDefault(user.getId(), 0) + 1);
    }

    /**
     * Removes a user from the list of registered attendees. If the user has checked in, also updates the check-in details.
     * @param user The user to be removed.
     */
    public void removeAttendee(User user) {
        registeredAttendees.remove(user);
        if (checkedInAttendeesCount.containsKey(user.getId())) {
            checkedInAttendees.remove(user);
            checkedInAttendeesCount.remove(user.getId());
        }
    }

    /**
     * Checks if a user is registered for the event.
     * @param user The user to check.
     */
    public boolean isUserRegistered(User user) {
        return registeredAttendees.contains(user);
    }

    /**
     * Checks if a user has checked in to the event.
     * @param user The user to check.
     */
    public boolean isAttendeeCheckedIn(User user) {
        return checkedInAttendees.contains(user);
    }

/**
     * Retrieves the number of times a user has checked in to the event.
     * @param user The user to check.
     */
    public int getCheckInCount(User user) {
        return checkedInAttendeesCount.getOrDefault(user.getId(), 0);
    }

    /**
     * Retrieves the list of registered attendees.
     * @return The list of registered attendees.
     */
    public List<User> getRegisteredAttendees() {
        return registeredAttendees;
    }

    /**
     * Retrieves the list of checked-in attendees.
     * @return The list of checked-in attendees.
     */
    public List<User> getCheckedInAttendees() {
        return new ArrayList<>(checkedInAttendees);
    }

/**
     * Retrieves the number of registered attendees.
     * @return The number of registered attendees.
     */
    public int getNumberOfRegisteredAttendees() {
        return registeredAttendees.size();
    }

    /**
     * Retrieves the number of checked-in attendees.
     * @return The number of checked-in attendees.
     */
    public int getNumberOfCheckedInAttendees() {
        return checkedInAttendees.size();
    }

    /**
     * Retrieves the maximum number of attendees for the event.
     * @return The maximum number of attendees, or null if no limit.
     */
    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    /**
     * Sets the maximum number of attendees for the event.
     * @param maxAttendees The maximum number of attendees, or null if no limit.
     */
    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    /**
     * A setter for the checked in Attendees in the app.
     * @param checkedInAttendees
     * The list of checked in Attendees.
     */
    public void setCheckedInAttendees(List<User> checkedInAttendees) {
        this.checkedInAttendees = checkedInAttendees;
    }

    /**
     * A setter for the list of registered Attendees in the app.
     * @param registeredAttendees
     * The list of registered Attendees to set.
     */
    public void setRegisteredAttendees(List<User> registeredAttendees){
        this.registeredAttendees = registeredAttendees;}

}

