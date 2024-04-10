package com.example.genzgpt;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class FirebaseTest {

    private FirebaseFirestore db;
    private String testEventId;
    private Firebase firebase;
    private String testUserId;


    @Before
    public void setup() {
        db = FirebaseFirestore.getInstance();
        firebase = new Firebase();  // Instantiate your Firebase class
        testUserId = "testUser_" + System.currentTimeMillis();
        testEventId = "testEvent_" + System.currentTimeMillis();
    }

    private Map<String, Object> createTestUserData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("firstName", "John");
        userData.put("lastName", "Doe");
        userData.put("email", "johndoe@example.com");
        userData.put("phoneNumber", "1234567890");
        userData.put("geolocation", true);
        userData.put("imageURL", "https://example.com/image.jpg");
        return userData;
    }

    private Map<String, Object> createTestEventData() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventName", "TestEvent");
        // Add other event data as needed for your test
        return eventData;
    }


    @After
    public void cleanup() {
        // Clean up test data after each test
        if (testUserId != null) {
            db.collection("users").document(testUserId).delete()
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Test user deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error deleting test user from Firestore: " + e.getMessage());
                    });
        }

        if (testEventId != null) {
            db.collection("events").document(testEventId).delete()
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Test event deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error deleting test event from Firestore: " + e.getMessage());
                    });
        }
    }

    @Test
    public void testCreateEvent() throws InterruptedException {
        Event event = new Event(testEventId, "TestEvent", new Date(), "Test Location", 50, "https://example.com/image.jpg");
        User organizer = new User("1", "Organizer Name", "org last name", 1234567890, "organizer@example.com", true, "imageeee");

        // Call the method with test data
        firebase.createEvent(event, organizer);

        // Use a latch to wait for the asynchronous operation to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Wait for a maximum of 10 seconds for the operation to complete
        latch.await(10, TimeUnit.SECONDS);

        // Verify the event and check-in data in Firestore
        DocumentReference eventRef = db.collection("events").document(testEventId);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assertTrue(task.getResult().exists());
                // Verify organizer is added to organizers list
                assertTrue(task.getResult().contains("organizers"));
                assertEquals(1, task.getResult().get("organizers", List.class).size());
                assertEquals(organizer.getId(), task.getResult().get("organizers", List.class).get(0));
            } else {
                fail("Error retrieving event data from Firestore");
            }
        });

        DocumentReference checkInRef = db.collection("checkIn").document(testEventId);
        checkInRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assertTrue(task.getResult().exists());
            } else {
                fail("Error retrieving check-in data from Firestore");
            }
            latch.countDown(); // Release the latch to end the test
        });

        // delete event
        firebase.deleteEvent("TestEvent");
    }

    @Test
    public void testDeleteEvent() throws InterruptedException {
        // Call the deleteEvent method
        Event event = new Event(testEventId, "TestEvent", new Date(), "Test Location", 50, "https://example.com/image.jpg");
        User organizer = new User("1", "Organizer Name", "org last name", 1234567890, "organizer@example.com", true, "imageeee");
        firebase.createEvent(event, organizer);
        firebase.deleteEvent("TestEvent");

        // Use a latch to wait for asynchronous operations to complete
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS); // Wait for 10 seconds

        // Verify that the event document is deleted
        DocumentReference eventRef = db.collection("events").document(testEventId);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assertFalse(task.getResult().exists()); // Ensure the event document does not exist
                latch.countDown(); // Release the latch to end the test
            } else {
                fail("Error querying event document: " + task.getException().getMessage());
            }
        });
    }

    // Fails
    @Test
    public void testUpdateUser() throws InterruptedException {
        // Prepare updated user data
        User updatedUser = new User(testUserId, "Jane", "Smith", 1234567889, "janesmith@example.com", false, "https://example.com/newimage.jpg");

        // Call the updateUser method
        CountDownLatch latch = new CountDownLatch(1);
        firebase.updateUser(updatedUser, new Firebase.OnUserUpdatedListener() {
            @Override
            public void onUserUpdated() {
                // User updated successfully
                System.out.println("User updated successfully");
                latch.countDown(); // Release the latch to end the test
            }

            @Override
            public void onUserUpdateFailed(Exception e) {
                // User update failed
                fail("User update failed: " + e.getMessage());
            }
        });

        // Wait for Firestore operations to complete
        latch.await(10, TimeUnit.SECONDS);

        // Verify that the user document is updated with the new data
        DocumentReference userRef = db.collection("users").document(testUserId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assertTrue(task.getResult().exists());
                assertEquals("Jane", task.getResult().getString("firstName"));
                assertEquals("Smith", task.getResult().getString("lastName"));
                assertEquals("janesmith@example.com", task.getResult().getString("email"));
                assertEquals("1234567889", task.getResult().getString("phoneNumber"));
//                assertFalse(task.getResult().getBoolean("geolocation"));
                assertEquals("https://example.com/newimage.jpg", task.getResult().getString("imageURL"));
            } else {
                fail("Error querying user document: " + task.getException().getMessage());
            }
        });
    }

    @Test
    public void testUpdateEventImageURL() {
        String imageURL = "https://example.com/test_image.jpg";
        // Call the method with test data
        updateEventImageURL(testEventId, imageURL);

        // Verify the update in Firestore
        DocumentReference eventRef = db.collection("events").document(testEventId);
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String updatedImageURL = task.getResult().getString("imageURL");
                assertEquals(imageURL, updatedImageURL);
            } else {
                // Handle test failure
                assertEquals("Firestore update failed", task.getException().getMessage());
            }
        });
    }

    private void updateEventImageURL(String eventID, String imageURL) {
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Update the 'imageURL' field in the document
        eventRef.update("imageURL", imageURL)
                .addOnSuccessListener(aVoid -> {
                    // Image URL successfully updated in Firestore
                    // You can log success if needed
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    throw new RuntimeException("Error updating image URL: " + e.getMessage());
                });
    }
}
