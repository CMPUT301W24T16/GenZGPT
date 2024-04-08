package com.example.genzgpt;

import com.example.genzgpt.View.EditEventFragment;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import android.text.TextUtils;

/**
 * Test cases for EditEventFragment Class.
 */
public class EditEventFragmentTest {

    @Test
    public void testValidateEventData_ValidData() {
        assertTrue(validateEventData("Event Name", getFutureDate(), "Event Location", 100));
    }

    @Test
    public void testValidateEventData_InvalidName() {
        assertFalse(validateEventData("", getFutureDate(), "Event Location", 100));
    }

    public boolean validateEventData(String eventName, Date eventDate, String eventLocation, int maxAttendees) {
        return eventName != null && !eventName.trim().isEmpty() && eventDate != null && eventLocation != null && !eventLocation.trim().isEmpty() && maxAttendees > 0;
    }

    private Date getFutureDate() {
        return new Calendar.Builder().setDate(2024, Calendar.DECEMBER, 25).build().getTime();
    }
}

