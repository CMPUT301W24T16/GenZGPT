package com.example.genzgpt;

import com.example.genzgpt.Controller.EventManager;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EventManagerTest {
    private EventManager eventManager;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() {
        eventManager = new EventManager();
        event1 = new Event("1", "Event 1", new Date(), "Location 1", 100, "image1.jpg");
        event2 = new Event("2", "Event 2", new Date(), "Location 2", 200, "image2.jpg");
    }

    @Test
    public void testAddEvent() {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);

        List<Event> events = eventManager.getAllEvents();
        Assertions.assertEquals(2, events.size());
        Assertions.assertTrue(events.contains(event1));
        Assertions.assertTrue(events.contains(event2));
    }

    @Test
    public void testRemoveEvent() {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);

        eventManager.removeEvent(Integer.parseInt(event1.getEventId()));

        List<Event> events = eventManager.getAllEvents();
        Assertions.assertEquals(1, events.size());
        Assertions.assertFalse(events.contains(event1));
        Assertions.assertTrue(events.contains(event2));
    }

    @Test
    public void testGetEventDetails() {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);

        Optional<Event> foundEvent = eventManager.getEventDetails(Integer.parseInt(event1.getEventId()));
        Assertions.assertTrue(foundEvent.isPresent());
        Assertions.assertEquals(event1, foundEvent.get());

        Optional<Event> notFoundEvent = eventManager.getEventDetails(3);
        Assertions.assertFalse(notFoundEvent.isPresent());
    }

    @Test
    public void testGetAllEvents() {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);

        List<Event> events = eventManager.getAllEvents();
        Assertions.assertEquals(2, events.size());
        Assertions.assertTrue(events.contains(event1));
        Assertions.assertTrue(events.contains(event2));
    }
}

