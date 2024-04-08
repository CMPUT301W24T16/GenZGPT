package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Controller.AttendeeManager;
import com.example.genzgpt.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the AttendeeManager Class
 */
class AttendeeManagerTest {

    private AttendeeManager attendeeManager;
    private User registeredUser;
    private User checkedInUser;

    @BeforeEach
    void setUp() {
        attendeeManager = new AttendeeManager(100);
        registeredUser = new User("1", "John", "Doe", 1234567890L, "john.doe@example.com", true, "john.jpg");
        checkedInUser = new User("2", "Jane", "Doe", 9876543210L, "jane.doe@example.com", true, "jane.jpg");
        attendeeManager.registerAttendee(registeredUser);
        attendeeManager.registerAttendee(checkedInUser);
        attendeeManager.checkInAttendee(checkedInUser);
    }

    @Test
    void registerAttendee_shouldAddUserToRegisteredAttendees() {
        User newUser = new User("3", "Alice", "Wonderland", 9999999999L, "alice@example.com", true, "alice.jpg");
        attendeeManager.registerAttendee(newUser);
        assertTrue(attendeeManager.getRegisteredAttendees().contains(newUser));
    }

    @Test
    void registerAttendee_shouldNotExceedMaxAttendees() {
        for (int i = 0; i < 200; i++) {
            User newUser = new User(Integer.toString(i), "User" + i, "user" + i + "@example.com", 1111111111L, "user" + i + "@example.com", true, "user.jpg");
            attendeeManager.registerAttendee(newUser);
        }
        assertEquals(100, attendeeManager.getNumberOfRegisteredAttendees());
    }

    @Test
    void checkInAttendee_shouldAddUserToCheckedInAttendees() {
        User newUser = new User("3", "Alice", "Wonderland", 9999999999L, "alice@example.com", true, "alice.jpg");
        attendeeManager.registerAttendee(newUser);
        attendeeManager.checkInAttendee(newUser);
        assertTrue(attendeeManager.getCheckedInAttendees().contains(newUser));
    }

    @Test
    void removeAttendee_shouldRemoveUser() {
        attendeeManager.removeAttendee(registeredUser);
        assertFalse(attendeeManager.getRegisteredAttendees().contains(registeredUser));
        assertFalse(attendeeManager.getCheckedInAttendees().contains(registeredUser));
    }

    @Test
    void isUserRegistered_shouldReturnTrueForRegisteredUser() {
        assertTrue(attendeeManager.isUserRegistered(registeredUser));
    }

    @Test
    void isUserRegistered_shouldReturnFalseForNonRegisteredUser() {
        User newUser = new User("3", "Alice", "Wonderland", 9999999999L, "alice@example.com", true, "alice.jpg");
        assertFalse(attendeeManager.isUserRegistered(newUser));
    }

    @Test
    void isAttendeeCheckedIn_shouldReturnTrueForCheckedInUser() {
        assertTrue(attendeeManager.isAttendeeCheckedIn(checkedInUser));
    }

    @Test
    void isAttendeeCheckedIn_shouldReturnFalseForNonCheckedInUser() {
        User newUser = new User("3", "Alice", "Wonderland", 9999999999L, "alice@example.com", true, "alice.jpg");
        assertFalse(attendeeManager.isAttendeeCheckedIn(newUser));
    }

    @Test
    void getCheckInCount_shouldReturnCorrectCount() {
        assertEquals(1, attendeeManager.getCheckInCount(checkedInUser));
        User newUser = new User("3", "Alice", "Wonderland", 9999999999L, "alice@example.com", true, "alice.jpg");
        assertEquals(0, attendeeManager.getCheckInCount(newUser));
    }
}
