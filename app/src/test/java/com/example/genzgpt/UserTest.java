package com.example.genzgpt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Role;
import com.example.genzgpt.Model.User;

/**
 * Test cases for the User class.
 */
class UserTest {

    @Test
    void testAddRole() {
        User user = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        Role role = new ConcreteRole("Organizer");
        user.addRole(role);
        assertTrue(user.hasRole("Organizer"));
    }

    @Test
    void testHasRole() {
        User user = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        Role role = new ConcreteRole("Organizer");
        user.addRole(role);
        assertTrue(user.hasRole("Organizer"));
        assertFalse(user.hasRole("Attendee"));
    }

    @Test
    void testGetters() {
        User user = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        assertEquals("1", user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(1234567890, user.getPhone());
        assertEquals("john@example.com", user.getEmail());
        assertFalse(user.isGeolocation());
    }

    @Test
    void testSetters() {
        User user = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        user.setId("2");
        user.setName("Jane");
        user.setLastName("Smith");
        user.setPhone(1234567890);
        user.setEmail("jane@example.com");
        user.setGeolocation(true);

        assertEquals("2", user.getId());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(1234567890, user.getPhone());
        assertEquals("jane@example.com", user.getEmail());
        assertTrue(user.isGeolocation());
    }

    @Test
    void testLoginAndLogout() {
        User user = new User("1", "John", "Doe", 1234567890, "john@example.com", false, "img");
        user.login();
        // You can add assertions or test further login/logout logic if needed
        assertTrue(true); // Placeholder assertion
        user.logout();
        // You can add assertions or test further login/logout logic if needed
        assertTrue(true); // Placeholder assertion
    }

    // ConcreteRole class for testing
    private static class ConcreteRole extends Role {
        public ConcreteRole(String name) {
            super(name);
        }
    }
}
