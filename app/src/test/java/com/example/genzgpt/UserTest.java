package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Role;
import com.example.genzgpt.Model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "John Doe", "john.doe@example.com");
    }

    @Test
    void testAddRole() {
        Role organizerRole = new TestRole("Organizer");
        user.addRole(organizerRole);
        assertTrue(user.hasRole("Organizer"));
    }

    @Test
    void testHasRole() {
        assertFalse(user.hasRole("Organizer")); // User starts with no roles

        Role attendeeRole = new TestRole("Attendee");
        user.addRole(attendeeRole);
        assertTrue(user.hasRole("Attendee"));
    }

    @Test
    void testGetters() {
        assertEquals("1", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testSetters() {
        user.setId("2");
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");

        assertEquals("2", user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
    }
    private static class TestRole extends Role {
        public TestRole(String name) {
            super(name);
        }
    }
}
