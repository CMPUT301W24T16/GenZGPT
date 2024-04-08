package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.AppUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the AppUser class.
 */
class AppUserTest {

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser("123", "John", "Doe", 1234567890L, "john.doe@example.com", true, "profile.jpg");
    }

    @Test
    void constructor_withId_shouldSetFieldsCorrectly() {
        assertEquals("123", appUser.getId());
        assertEquals("John", appUser.getFirstName());
        assertEquals("Doe", appUser.getLastName());
        assertEquals(1234567890L, appUser.getPhone());
        assertEquals("john.doe@example.com", appUser.getEmail());
        assertEquals("profile.jpg", appUser.getImageURL());
    }



    @Test
    void setUserEmail_shouldSetCorrectEmail() {
        AppUser.setUserEmail("new.email@example.com");
        assertEquals("new.email@example.com", AppUser.getAppUserEmail());
    }
}

