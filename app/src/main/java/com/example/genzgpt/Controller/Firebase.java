package com.example.genzgpt.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is responsible for handling all interactions with Firebase.
 */
public class Firebase {
    private final FirebaseFirestore db;
    // Handle Firebase interactions

    /**
     * 
     * Uploads an image to Firebase Storage and associates it with the specified
     * event.
     *
     * @param eventID        ID of the event to associate with the image.
     * @param imageUri       Uri of the image to upload.
     * @param progressDialog Progress dialog for showing upload progress.
     * @param context        Context for displaying toasts.
     */
    public static void uploadImageForEvent(String eventID, Uri imageUri, ProgressDialog progressDialog,
            Context context) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("event_images/" + eventID);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image successfully uploaded
                    progressDialog.dismiss();

                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageURL = uri.toString();
                                updateEventImageURL(eventID, imageURL);
                                showToast(context, "Image uploaded and associated with the event");
                            })
                            .addOnFailureListener(e -> {
                                showToast(context, "Failed to get image download URL: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showToast(context, "Failed to upload image: " + e.getMessage());
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Percentage: " + (int) progressPercent + "%");
                });
    }

    /**
     * Update the Firestore document for the specified event with the image URL.
     *
     * @param eventID  ID of the event to update.
     * @param imageURL URL of the uploaded image.
     */
    private static void updateEventImageURL(String eventID, String imageURL) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventID);

        // Update the 'imageURL' field in the document
        eventRef.update("imageURL", imageURL)
                .addOnSuccessListener(aVoid -> {
                    // Image URL successfully updated in Firestore
                    Log.i("Firebase", "Image URL updated successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firebase", "Error updating image URL: " + e.getMessage());
                });
    }

    /**
     * Uploads an image to Firebase Storage and associates it with the specified
     * user.
     *
     * @param userID         ID of the event to associate with the image.
     * @param imageUri       Uri of the image to upload.
     * @param progressDialog Progress dialog for showing upload progress.
     * @param context        Context for displaying toasts.
     */
    public static void uploadImageForUser(String userID, Uri imageUri, ProgressDialog progressDialog, Context context) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("user_images/" + userID);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image successfully uploaded
                    progressDialog.dismiss();

                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageURL = uri.toString();
                                updateUserImageURL(userID, imageURL);
                                showToast(context, "Image uploaded and associated with the event");
                            })
                            .addOnFailureListener(e -> {
                                showToast(context, "Failed to get image download URL: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showToast(context, "Failed to upload image: " + e.getMessage());
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Percentage: " + (int) progressPercent + "%");
                });
    }

    /**
     * Update the Firestore document for the specified user with the image URL.
     *
     * @param userID   ID of the event to update.
     * @param imageURL URL of the uploaded image.
     */
    private static void updateUserImageURL(String userID, String imageURL) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("users").document(userID);

        // Update the 'imageURL' field in the document
        eventRef.update("imageURL", imageURL)
                .addOnSuccessListener(aVoid -> {
                    // Image URL successfully updated in Firestore
                    Log.i("Firebase", "Image URL updated successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firebase", "Error updating image URL: " + e.getMessage());
                });
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Delete an Event image from Firestore and Firebase Storage.
     *
     * @param eventName  Name of the event containing the image.
     */
    public void deleteEventImage(String eventName) {
        try {
            CollectionReference eventsRef = db.collection("events");
            Query query = eventsRef.whereEqualTo("eventName", eventName);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Get the first matching event document
                        DocumentSnapshot document = snapshot.getDocuments().get(0);
                        String eventId = document.getId();

                        // Update the imageURL field to null
                        eventsRef.document(eventId).update("imageURL", FieldValue.delete())
                                .addOnSuccessListener(aVoid -> {
                                    // Image URL deleted successfully
                                    Log.i("Firebase", "Image URL deleted successfully");
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while deleting image URL
                                    Log.e("Firebase", "Error deleting image URL: " + e.getMessage());
                                });
                    } else {
                        // No events found with the specified name
                        Log.i("Firebase", "No events found with the name: " + eventName);
                    }
                } else {
                    // Error occurred while querying events
                    Log.e("Firebase", "Error querying events: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error deleting image URL: " + e.getMessage());
        }
    }

    /**
     * Delete an Event image from Firebase Storage.
     *
     * @param userId  ID of the event containing the image.
     */
    public void deleteUserImage(String userId) {
        try {
            CollectionReference usersRef = db.collection("users");
            Query query = usersRef.whereEqualTo("email", userId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            // Update the imageURL field to null
                            document.getReference().update("imageURL", null)
                                    .addOnSuccessListener(aVoid -> {
                                        // Image URL deleted successfully
                                        Log.i("Firebase", "Image URL deleted successfully for user: " + userId);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error occurred while deleting the image URL
                                        Log.e("Firebase", "Error deleting image URL for user: " + userId + ", " + e.getMessage());
                                    });
                        }
                    } else {
                        // No users found with the specified ID
                        Log.i("Firebase", "No users found with the ID: " + userId);
                    }
                } else {
                    // Error occurred while querying users
                    Log.e("Firebase", "Error querying users: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error deleting image URL: " + e.getMessage());
        }
    }

    /**
     * Retrieves the user data from Firebase.
     * 
     * @param userId The ID of the user to retrieve.
     */
    public void getUserData(String userId, OnUserLoadedListener listener) {
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String email = document.getString("email");
                        Long phoneNumber = document.getLong("phoneNumber");
                        boolean geolocation = Boolean.TRUE.equals(document.getBoolean("geolocation"));
                        String imageURL = document.getString("imageURL");

                        User user = new User(userId, firstName, lastName, phoneNumber, email, geolocation, imageURL);
                        listener.onUserLoaded(user);
                    } else {
                        listener.onUserNotFound();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error fetching user data: " + e.getMessage());
                    listener.onUserLoadFailed(e);
                });
    }

    public interface OnUserLoadedListener {
        void onUserLoaded(User user);

        void onUserNotFound();

        void onUserLoadFailed(Exception e);
    }

    /**
     * Uploads an image to Firebase Storage and retrieves the download URL.
     *
     * @param imagePath                Path to store the image in Firebase Storage.
     * @param imageUri                 Uri of the image to upload.
     * @param onUploadCompleteListener Callback for handling upload completion.
     */
    public static void uploadImageAndGetUrl(String imagePath, Uri imageUri,
            OnUploadCompleteListener onUploadCompleteListener) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imagePath);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image successfully uploaded
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Get the download URL
                                String imageURL = uri.toString();
                                onUploadCompleteListener.onUploadComplete(imageURL);
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to get download URL
                                onUploadCompleteListener.onUploadFailed(e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle failure to upload image
                    onUploadCompleteListener.onUploadFailed(e.getMessage());
                });
    }

    /**
     * Callback interface for handling upload completion.
     */
    public interface OnUploadCompleteListener {
        void onUploadComplete(String imageURL);

        void onUploadFailed(String errorMessage);
    }

    // FIXME MIGHT STILL BE USING EMAIL TO GET A USER?
    /**
     * Retrieves the list of events from the database.
     * 
     * @param eventName The name of the event to retrieve.
     */
    public void getEventData(String eventName, OnEventLoadedListener listener) {
        db.collection("events")
                .whereEqualTo("eventName", eventName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String eventId = document.getId();
                            Date eventDate = document.getDate("eventDate");
                            String location = document.getString("location");
                            Integer maxAttendees = document.getLong("maxAttendees") != null
                                    ? document.getLong("maxAttendees").intValue()
                                    : null;
                            String imageURL = document.getString("imageURL");

                            Event event = new Event(eventId, eventName, eventDate, location, 0, imageURL);
                            event.setMaxAttendees(maxAttendees);

                            List<String> organizerEmails = (List<String>) document.get("organizers");
                            if (organizerEmails != null && !organizerEmails.isEmpty()) {
                                for (String email : organizerEmails) {
                                    getUserData(email, new OnUserLoadedListener() {
                                        @Override
                                        public void onUserLoaded(User user) {
                                            event.addOrganizer(user.getId());
                                            if (event.getOrganizers().size() == organizerEmails.size()) {
                                                fetchRegisteredAttendees(eventName,
                                                        new OnRegisteredAttendeesLoadedListener() {
                                                            @Override
                                                            public void onRegisteredAttendeesLoaded(
                                                                    List<User> registeredAttendees) {
                                                                event.setRegisteredAttendees(registeredAttendees);
                                                                fetchCheckedInAttendees(eventName,
                                                                        new OnCheckInAttendeesLoadedListener() {
                                                                            @Override
                                                                            public void onCheckInAttendeesLoaded(
                                                                                    List<User> checkedInAttendees) {
                                                                                event.setCheckedInAttendees(
                                                                                        checkedInAttendees);
                                                                                listener.onEventLoaded(event);
                                                                            }

                                                                            @Override
                                                                            public void onCheckInAttendeesLoadFailed(
                                                                                    Exception e) {
                                                                                listener.onEventLoadFailed(e);
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onRegisteredAttendeesLoadFailed(Exception e) {
                                                                listener.onEventLoadFailed(e);
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onUserNotFound() {
                                            Log.d("Firebase", "User not found");
                                        }

                                        @Override
                                        public void onUserLoadFailed(Exception e) {
                                            listener.onEventLoadFailed(e);
                                        }
                                    });
                                }
                            } else {
                                fetchRegisteredAttendees(eventName, new OnRegisteredAttendeesLoadedListener() {

                                    @Override
                                    public void onRegisteredAttendeesLoaded(List<User> registeredAttendees) {
                                        event.setRegisteredAttendees(registeredAttendees);
                                        fetchCheckedInAttendees(eventName, new OnCheckInAttendeesLoadedListener() {
                                            @Override
                                            public void onCheckInAttendeesLoaded(List<User> checkedInAttendees) {
                                                event.setCheckedInAttendees(checkedInAttendees);
                                                listener.onEventLoaded(event);
                                            }

                                            @Override
                                            public void onCheckInAttendeesLoadFailed(Exception e) {
                                                listener.onEventLoadFailed(e);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onRegisteredAttendeesLoadFailed(Exception e) {
                                        listener.onEventLoadFailed(e);
                                    }
                                });
                            }
                        } else {
                            listener.onEventNotFound();
                        }
                    } else {
                        listener.onEventLoadFailed(task.getException());
                    }
                });
    }

    public interface OnEventLoadedListener {
        void onEventLoaded(Event event);

        void onEventNotFound();

        void onEventLoadFailed(Exception e);
    }


    /**
     * Retrieves the user data from Firebase.
     * 
     * @return the user details for a particular email.
     * @param userMap The map containing the user data.
     */
    private User createUserFromMap(Map<String, Object> userMap) {
        String userId = (String) userMap.get("id");
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        Long phoneNumber = (Long) userMap.get("phoneNumber");
        boolean geolocation = (boolean) userMap.get("geolocation");
        String imageURL = (String) userMap.get("imageURL");

        return new User(userId, firstName, lastName, phoneNumber, email, geolocation, imageURL);
    }

    /**
     * Creates a new user in the database.
     * 
     * @param user The user to create.
     */
    public void createUser(User user, OnUserCreatedListener listener) {
        // Check if the email is already used
        db.collection("users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot emailSnapshot = task.getResult();
                        if (emailSnapshot != null && !emailSnapshot.isEmpty()) {
                            Log.e("Firebase", "Email " + user.getEmail() + " is already used");
                            listener.onEmailAlreadyExists();
                        } else {
                            // Create a new user document
                            DocumentReference userRef = db.collection("users").document();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("firstName", user.getFirstName());
                            userData.put("lastName", user.getLastName());
                            userData.put("email", user.getEmail());
                            userData.put("phoneNumber", user.getPhone());
                            userData.put("geolocation", user.isGeolocation());
                            userData.put("imageURL", user.getImageURL());

                            userRef.set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // User created successfully
                                        Log.i("Firebase", "User created successfully");

                                        // Retrieve the ID of the newly created user
                                        String userId = userRef.getId();

                                        // Update the user document with the ID field
                                        userRef.update("id", userId)
                                                .addOnSuccessListener(aVoid1 -> {
                                                    // ID field added successfully
                                                    Log.i("Firebase", "User ID added successfully");

                                                    // Return the user ID through the callback
                                                    listener.onUserCreated(userId);
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Error occurred while adding the ID field
                                                    Log.e("Firebase", "Error adding user ID: " + e.getMessage());
                                                    listener.onUserCreationFailed(e);
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error occurred while creating the user
                                        Log.e("Firebase", "Error creating user: " + e.getMessage());
                                        listener.onUserCreationFailed(e);
                                    });
                        }
                    } else {
                        // Error occurred while checking if the email is already used
                        Log.e("Firebase", "Error checking email existence: " + task.getException().getMessage());
                        listener.onUserCreationFailed(task.getException());
                    }
                });
    }

    public interface OnUserCreatedListener {
        void onUserCreated(String userId);

        void onEmailAlreadyExists();

        void onUserCreationFailed(Exception e);
    }

    /**
     * Creates a new event in the database.
     * 
     * @param organizer The user creating the event.
     * @param event The event to create.
     */
    public void createEvent(Event event, User organizer) {
        event.addOrganizer(organizer.getId());
        try {
            // Create a new event document
            DocumentReference eventRef = db.collection("events").document();
            String eventId = eventRef.getId();

            // Create a map to store the event data
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", eventId);
            eventData.put("eventName", event.getEventName());
            eventData.put("eventDate", event.getEventDate());
            eventData.put("location", event.getLocation());
            eventData.put("maxAttendees", event.getMaxAttendees());
            eventData.put("imageURL", event.getImageURL());

            // Add the organizer's email to the list of organizers
            List<String> organizerIds = new ArrayList<>();
            organizerIds.add(organizer.getId());
            eventData.put("organizers", organizerIds);

            // Initialize an empty list for registered attendees
            eventData.put("registeredAttendees", new ArrayList<>());
            eventData.put("checkedInAttendees", new ArrayList<>());

            // Create a new document in the "checkIn" collection for the event
            DocumentReference checkInRef = db.collection("checkIn").document(eventId);
            Map<String, Object> checkInData = new HashMap<>();
            checkInData.put("eventId", eventId);
            checkInData.put("checkInList", new ArrayList<>());

            // Use a WriteBatch to atomically create the event and check-in documents
            WriteBatch batch = db.batch();
            batch.set(eventRef, eventData);
            batch.set(checkInRef, checkInData);

            batch.commit()
                    .addOnSuccessListener(aVoid -> {
                        Log.i("Firebase", "Event and check-in data created successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while creating the event and check-in data
                        Log.e("Firebase", "Error creating event and check-in data: " + e.getMessage());
                    });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error creating event and check-in data: " + e.getMessage());
        }
    }

    /**
     * Adds a user to the list of registered attendees for a specific event.
     * @param eventId The ID of the event.
     * @param userId The ID of the user to add.
     */
    public void addUserToCheckedInAttendees(String eventId, String userId) {
        try {
            DocumentReference eventRef = db.collection("events").document(eventId);
            CollectionReference checkInRef = db.collection("checkIn");
            Query query = checkInRef.whereEqualTo("eventId", eventId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Get the first matching check-in document
                        DocumentSnapshot checkInDocument = snapshot.getDocuments().get(0);
                        String checkInId = checkInDocument.getId();

                        // Get the current "checkInList" array from the check-in document
                        List<Map<String, Object>> checkInList = (List<Map<String, Object>>) checkInDocument.get("checkInList");

                        boolean userExists = false;
                        if (checkInList != null) {
                            for (Map<String, Object> checkInEntry : checkInList) {
                                if (checkInEntry.get("userId").equals(userId)) {
                                    // User already exists in the "checkInList"
                                    userExists = true;
                                    long count = (long) checkInEntry.get("count");
                                    checkInEntry.put("count", count + 1);
                                    break;
                                }
                            }
                        } else {
                            checkInList = new ArrayList<>();
                        }

                        if (!userExists) {
                            // User doesn't exist in the "checkInList", add a new entry
                            Map<String, Object> newCheckInEntry = new HashMap<>();
                            newCheckInEntry.put("userId", userId);
                            newCheckInEntry.put("count", 1);
                            checkInList.add(newCheckInEntry);
                        }

                        // Update the "checkInList" array in the check-in document
                        checkInRef.document(checkInId).update("checkInList", checkInList)
                                .addOnSuccessListener(aVoid -> {
                                    // User added to checkInList successfully
                                    Log.i("Firebase", "User added to checkInList successfully");

                                    // Update the "checkedInAttendees" array in the event document
                                    eventRef.update("checkedInAttendees", FieldValue.arrayUnion(userId))
                                            .addOnSuccessListener(aVoid1 -> {
                                                // User added to checkedInAttendees successfully
                                                Log.i("Firebase", "User added to checkedInAttendees successfully");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error occurred while adding user to checkedInAttendees
                                                Log.e("Firebase", "Error adding user to checkedInAttendees: " + e.getMessage());
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while updating checkInList
                                    Log.e("Firebase", "Error updating checkInList: " + e.getMessage());
                                });
                    } else {
                        // No check-in document found for the specified event
                        Log.e("Firebase", "No check-in document found for the event");
                    }
                } else {
                    // Error occurred while querying check-in data
                    Log.e("Firebase", "Error querying check-in data: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error adding user to checkedInAttendees: " + e.getMessage());
        }
    }

    /**
     * Adds a user to the list of registered attendees for a specific event.
     * 
     * @param eventName The name of the event.
     * @param userId The ID of the user to add.
     */
    public void addUserToRegisteredAttendees(String eventName, String userId) {
        try {
            CollectionReference eventsRef = db.collection("events");
            Query query = eventsRef.whereEqualTo("eventName", eventName);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Assuming there is only one event with the given name
                        DocumentSnapshot eventDocument = snapshot.getDocuments().get(0);
                        String eventId = eventDocument.getId();

                        DocumentReference eventRef = db.collection("events").document(eventId);
                        eventRef.update("registeredAttendees", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid -> {
                                    // User added to registeredAttendees successfully
                                    Log.i("Firebase", "User added to registeredAttendees successfully");
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while adding user to registeredAttendees
                                    Log.e("Firebase", "Error adding user to registeredAttendees: " + e.getMessage());
                                });
                    } else {
                        // No events found with the specified name
                        Log.i("Firebase", "No events found with the name: " + eventName);
                    }
                } else {
                    // Error occurred while querying events
                    Log.e("Firebase", "Error querying events: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error adding user to registeredAttendees: " + e.getMessage());
        }
    }

    /**
     * firebase constructor
     */
    public Firebase() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Deletes an event from the database.
     * 
     * @param eventName The name of the event to delete.
     */
    public void deleteEvent(String eventName) {
        try {
            CollectionReference eventsRef = db.collection("events");
            Query query = eventsRef.whereEqualTo("eventName", eventName);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Get the first matching event document
                        DocumentSnapshot document = snapshot.getDocuments().get(0);
                        String eventId = document.getId();

                        // Delete the event document
                        eventsRef.document(eventId).delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Event deleted successfully
                                    Log.i("Firebase", "Event deleted successfully");

                                    // Delete the corresponding check-in document
                                    CollectionReference checkInRef = db.collection("checkIn");
                                    Query checkInQuery = checkInRef.whereEqualTo("eventId", eventId);

                                    checkInQuery.get().addOnCompleteListener(checkInTask -> {
                                        if (checkInTask.isSuccessful()) {
                                            QuerySnapshot checkInSnapshot = checkInTask.getResult();
                                            if (checkInSnapshot != null && !checkInSnapshot.isEmpty()) {
                                                // Get the first matching check-in document
                                                DocumentSnapshot checkInDocument = checkInSnapshot.getDocuments().get(0);
                                                String checkInId = checkInDocument.getId();

                                                // Delete the check-in document
                                                checkInRef.document(checkInId).delete()
                                                        .addOnSuccessListener(aVoid2 -> {
                                                            // Check-in data deleted successfully
                                                            Log.i("Firebase", "Check-in data deleted successfully");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Error occurred while deleting the check-in data
                                                            Log.e("Firebase", "Error deleting check-in data: " + e.getMessage());
                                                        });
                                            } else {
                                                // No check-in data found for the event
                                                Log.i("Firebase", "No check-in data found for the event");
                                            }
                                        } else {
                                            // Error occurred while querying check-in data
                                            Log.e("Firebase", "Error querying check-in data: " + checkInTask.getException().getMessage());
                                        }
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while deleting the event
                                    Log.e("Firebase", "Error deleting event: " + e.getMessage());
                                });
                    } else {
                        // No events found with the specified name
                        Log.i("Firebase", "No events found with the name: " + eventName);
                    }
                } else {
                    // Error occurred while querying events
                    Log.e("Firebase", "Error querying events: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error deleting event and check-in data: " + e.getMessage());
        }
    }

    /**
     * Deletes a user from the database.
     * 
     * @param userId The ID of the user to delete.
     */
    public void deleteUser(String userId) {
        try {
            CollectionReference usersRef = db.collection("users");
            Query query = usersRef.whereEqualTo("email", userId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // User deleted successfully
                                        Log.i("Firebase", "User deleted successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error occurred while deleting the user
                                        Log.e("Firebase", "Error deleting user: " + e.getMessage());
                                    });
                        }
                    } else {
                        // No users found with the specified ID
                        Log.i("Firebase", "No users found with the ID: " + userId);
                    }
                } else {
                    // Error occurred while querying users
                    Log.e("Firebase", "Error querying users: " + task.getException().getMessage());
                }
            });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Retrieves the list of all users from the database.
     *
     */
    public void fetchUsers(OnUsersLoadedListener listener) {
        db.collection("users").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<User> userList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Extract user data from the document
                        String userID = document.getString("id");
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String email = document.getString("email");
                        Long phoneNumber = document.getLong("phoneNumber");
                        boolean geolocation = document.getBoolean("geolocation") != null
                                && Boolean.TRUE.equals(document.getBoolean("geolocation"));
                        String imageURL = document.getString("imageURL");
                        User user = new User(userID, firstName, lastName, phoneNumber, email, geolocation, imageURL);
                        userList.add(user);
                    }
                    listener.onUsersLoaded(userList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error fetching users: " + e.getMessage());
                    listener.onUsersLoadFailed(e);
                });
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<User> userList);

        void onUsersLoadFailed(Exception e);
    }

    /**
     * Retrieves the list of events from the database.
     *
     * @param listener Listener for handling events retrieval.
     */
    public void fetchEvents(OnEventsLoadedListener listener) {
        System.out.println("Fetching events:" + db.collection("events").get() + OnAttendeeRegisteredListener.class);
        db.collection("events").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Event> eventList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String eventId = document.getString("eventId");
                        String eventName = document.getString("eventName");
                        Date eventDate = document.getDate("eventDate");
                        String location = document.getString("location");
                        Long maxAttendeesLong = document.getLong("maxAttendees");
                        Integer maxAttendees = maxAttendeesLong != null ? maxAttendeesLong.intValue() : null;
                        String imageURL = document.getString("imageURL");
                        List<String> organizers = (List<String>) document.get("organizers");
                        List<User> registeredAttendees = (List<User>) document.get("registeredAttendees");

                        // Check for nulls to avoid adding incomplete events
                        if (eventName != null && eventDate != null && location != null) {
                            Event event = new Event(eventId, eventName, eventDate, location, maxAttendees, imageURL);
                            event.setOrganizers(organizers);
                            event.setRegisteredAttendees(registeredAttendees);
                            eventList.add(event);
                        }
                    }
                    listener.onEventsLoaded(eventList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error fetching events: " + e.getMessage());
                    listener.onEventsLoadFailed(e);
                });
    }

    public interface OnEventsLoadedListener {
        void onEventsLoaded(List<Event> eventList);

        void onEventsLoadFailed(Exception e);
    }

    /**
     * Retrieves the list of events from the database.
     *
     * @param userId The ID of the user to retrieve events for.
     * @param listener Listener for handling events retrieval.
     */
    public void fetchUserEvents(String userId, OnUserEventsLoadedListener listener) {
        List<Event> eventList = new ArrayList<>();

        // First, query for events where the user is an organizer
        db.collection("events").whereArrayContains("organizers", userId).get()
                .addOnSuccessListener(organizerQuerySnapshot -> {
                    for (DocumentSnapshot document : organizerQuerySnapshot.getDocuments()) {
                        Event event = documentToObject(document);
                        eventList.add(event);
                    }

                    db.collection("events").whereArrayContains("registeredAttendees", userId).get()
                            .addOnSuccessListener(attendeeQuerySnapshot -> {

                                for (DocumentSnapshot document : attendeeQuerySnapshot.getDocuments()) {
                                    Event event = documentToObject(document); // Ensure no duplicate events are added
                                    if (!eventList.contains(event)) {
                                        eventList.add(event);
                                    }
                                }

                                listener.onEventsLoaded(userId, eventList);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Error fetching events as attendee: " + e.getMessage());
                                listener.onEventsLoadFailed(e);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error fetching events as organizer: " + e.getMessage());
                    listener.onEventsLoadFailed(e);
                });
    }

    /**
     * Converts a Firestore document to an Event object.
     *
     * @param document The Firestore document to convert.
     * @return The Event object.
     */
    private Event documentToObject(DocumentSnapshot document) {
        String eventId = document.getId(); // Use document ID as event ID
        String eventName = document.getString("eventName");
        Date eventDate = document.getDate("eventDate");
        String location = document.getString("location");
        Long maxAttendeesLong = document.getLong("maxAttendees");
        Integer maxAttendees = maxAttendeesLong != null ? maxAttendeesLong.intValue() : null;
        String imageURL = document.getString("imageURL");
        List<String> organizers = (List<String>) document.get("organizers");
        List<User> registeredAttendees = (List<User>) document.get("registeredAttendees");
        System.out.println("organizers: " + organizers + " registeredAttendees: " + registeredAttendees);
        Event event = new Event(eventId, eventName, eventDate, location, maxAttendees, imageURL);
        event.setOrganizers(organizers);
        event.setRegisteredAttendees(registeredAttendees);
        return event;
    }

    public interface OnUserEventsLoadedListener {
        void onEventsLoaded(String email, List<Event> eventList);
        void onEventsLoadFailed(Exception e);
    }

    /**
     * Retrieves the list of checked-in attendees for a specific event from the
     * database.
     * 
     * @param eventId The ID of the event to retrieve checked-in attendees for.
     */
    public void fetchCheckedInAttendees(String eventId, OnCheckInAttendeesLoadedListener listener) {
        CollectionReference checkInRef = db.collection("checkIn");
        Query query = checkInRef.whereEqualTo("eventId", eventId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    List<String> checkedInAttendeeIds = new ArrayList<>();

                    // Iterate through the documents in the "checkIn" collection
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        List<Map<String, Object>> checkInList = (List<Map<String, Object>>) document.get("checkInList");
                        if (checkInList != null) {
                            for (Map<String, Object> checkInEntry : checkInList) {
                                String userId = (String) checkInEntry.get("userId");
                                checkedInAttendeeIds.add(userId);
                            }
                        }
                    }

                    // Create a list to store the User objects
                    List<User> checkedInAttendees = new ArrayList<>();

                    // Create a counter to keep track of loaded users
                    AtomicInteger loadedUserCount = new AtomicInteger(0);

                    // Iterate through the list of attendee IDs and fetch their user data
                    for (String userId : checkedInAttendeeIds) {
                        getUserData(userId, new OnUserLoadedListener() {
                            @Override
                            public void onUserLoaded(User user) {
                                checkedInAttendees.add(user);
                                if (loadedUserCount.incrementAndGet() == checkedInAttendeeIds.size()) {
                                    // All users have been loaded
                                    listener.onCheckInAttendeesLoaded(checkedInAttendees);
                                }
                            }

                            @Override
                            public void onUserNotFound() {
                                if (loadedUserCount.incrementAndGet() == checkedInAttendeeIds.size()) {
                                    // All users have been loaded
                                    listener.onCheckInAttendeesLoaded(checkedInAttendees);
                                }
                            }

                            @Override
                            public void onUserLoadFailed(Exception e) {
                                listener.onCheckInAttendeesLoadFailed(e);
                            }
                        });
                    }
                } else {
                    // No check-in data found for the specified event
                    listener.onCheckInAttendeesLoaded(new ArrayList<>());
                }
            } else {
                // Error occurred while querying check-in data
                listener.onCheckInAttendeesLoadFailed(task.getException());
            }
        });
    }

    public interface OnCheckInAttendeesLoadedListener {
        void onCheckInAttendeesLoaded(List<User> checkedInAttendees);

        void onCheckInAttendeesLoadFailed(Exception e);
    }

    /**
     * Retrieves the list of registered attendees for a specific event from the
     * database.
     * 
     * @param eventName The name of the event to retrieve registered attendees for.
     */
    public void fetchRegisteredAttendees(String eventName, OnRegisteredAttendeesLoadedListener listener) {
        CollectionReference eventsRef = db.collection("events");
        Query query = eventsRef.whereEqualTo("eventName", eventName);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    // Assuming there is only one event with the given name
                    DocumentSnapshot eventDocument = snapshot.getDocuments().get(0);
                    List<String> registeredAttendeeIds = (List<String>) eventDocument.get("registeredAttendees");

                    // Create a list to store the User objects
                    List<User> registeredAttendees = new ArrayList<>();

                    // Create an AtomicInteger to keep track of the number of loaded users
                    AtomicInteger loadedUserCount = new AtomicInteger(0);

                    // Iterate through the list of attendee IDs and fetch their user data
                    for (String userId : registeredAttendeeIds) {
                        getUserData(userId, new OnUserLoadedListener() {
                            @Override
                            public void onUserLoaded(User user) {
                                registeredAttendees.add(user);
                                if (loadedUserCount.incrementAndGet() == registeredAttendeeIds.size()) {
                                    listener.onRegisteredAttendeesLoaded(registeredAttendees);
                                }
                            }

                            @Override
                            public void onUserNotFound() {
                                if (loadedUserCount.incrementAndGet() == registeredAttendeeIds.size()) {
                                    listener.onRegisteredAttendeesLoaded(registeredAttendees);
                                }
                            }

                            @Override
                            public void onUserLoadFailed(Exception e) {
                                listener.onRegisteredAttendeesLoadFailed(e);
                            }
                        });
                    }

                    // If there are no registered attendees, call the onRegisteredAttendeesLoaded
                    // callback immediately
                    if (registeredAttendeeIds.isEmpty()) {
                        listener.onRegisteredAttendeesLoaded(registeredAttendees);
                    }
                } else {
                    // No events found with the specified name
                    listener.onRegisteredAttendeesLoadFailed(
                            new Exception("No events found with the name: " + eventName));
                }
            } else {
                // Error occurred while querying events
                listener.onRegisteredAttendeesLoadFailed(task.getException());
            }
        });
    }

    public interface OnRegisteredAttendeesLoadedListener {
        void onRegisteredAttendeesLoaded(List<User> registeredAttendees);

        void onRegisteredAttendeesLoadFailed(Exception e);
    }

    /**
     * Updates the user data in the database.
     * 
     * @param user The user to update.
     */
    public void updateUser(User user, OnUserUpdatedListener listener) {
        String userId = user.getId();
        DocumentReference userRef = db.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", user.getFirstName());
        updates.put("lastName", user.getLastName());
        updates.put("email", user.getEmail());
        updates.put("phoneNumber", user.getPhone());
        updates.put("geolocation", user.isGeolocation());
        updates.put("imageURL", user.getImageURL());

        userRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User updated successfully");
                    listener.onUserUpdated();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating user: " + e.getMessage());
                    listener.onUserUpdateFailed(e);
                });
    }

    public interface OnUserUpdatedListener {
        void onUserUpdated();

        void onUserUpdateFailed(Exception e);
    }

    /**
     * Updates the event data in the database.
     * 
     * @param eventId, newEventDate, newLocation
     */
    public void updateEvent(String eventId, Date newEventDate, String newLocation, OnEventUpdatedListener listener) {
        DocumentReference eventRef = db.collection("events").document(eventId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("eventDate", newEventDate);
        updates.put("location", newLocation);

        eventRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Event updated successfully");
                    listener.onEventUpdated();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating event: " + e.getMessage());
                    listener.onEventUpdateFailed(e);
                });
    }

    /**
     * Registers a user for a specific event.
     * 
     * @param event The event to register for.
     * @param user The user to register.
     * @param listener Listener for handling registration events.
     */
    public void registerAttendee(Event event, User user, OnAttendeeRegisteredListener listener) {
        CollectionReference eventsRef = db.collection("events");
        Query query = eventsRef.whereEqualTo("eventName", event.getEventName());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    DocumentSnapshot eventDocument = snapshot.getDocuments().get(0);
                    String eventId = eventDocument.getId();
                    DocumentReference eventRef = db.collection("events").document(eventId);

                    // Fetching current registered attendees and maxAttendees
                    List<String> registeredAttendees = (List<String>) eventDocument.get("registeredAttendees");
                    int currentAttendeesCount = registeredAttendees != null ? registeredAttendees.size() : 0;
                    Long maxAttendees = eventDocument.getLong("maxAttendees");

                    if (maxAttendees == null || currentAttendeesCount < maxAttendees) {
                        // Register the user if maxAttendees not reached
                        eventRef.update("registeredAttendees", FieldValue.arrayUnion(user.getId()))
                                .addOnSuccessListener(aVoid -> listener.onAttendeeRegistered())
                                .addOnFailureListener(e -> {
                                    System.err.println("Error occurred while registering user for the event: " + e.getMessage());
                                    listener.onAttendeeRegistrationFailed(e);
                                });
                    } else {
                        // Max attendees reached
                        listener.onAttendeeRegistrationFailed(new Exception("Event is at full capacity."));
                    }
                } else {
                    listener.onEventNotFound();
                }
            } else {
                System.err.println("Error occurred while querying events: " + task.getException().getMessage());
                listener.onEventLoadFailed(task.getException());
            }
        });
    }

    public interface OnAttendeeRegisteredListener {
        void onAttendeeRegistered();

        void onAttendeeRegistrationFailed(Exception e);

        void onEventNotFound();

        void onEventLoadFailed(Exception e);
    }

    public interface OnEventUpdatedListener {
        void onEventUpdated();

        void onEventUpdateFailed(Exception e);
    }

    /**
     * Retrieves the list of events for a specific organizer from the database.
     * @param userId The ID of the user to retrieve events for.
     */
    public void fetchEventsForOrganizer(String userId, OnEventsLoadedListener listener) {
        db.collection("events")
                .whereArrayContains("organizers", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Event> eventList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String eventId = document.getString("eventId");
                        String eventName = document.getString("eventName");
                        Date eventDate = document.getDate("eventDate");
                        String location = document.getString("location");
                        Long maxAttendeesLong = document.getLong("maxAttendees");
                        Integer maxAttendees = maxAttendeesLong != null ? maxAttendeesLong.intValue() : null;
                        String imageURL = document.getString("imageURL");
                        List<User> registeredAttendees = (List<User>) document.get("registeredAttendees");


                        // Check for nulls to avoid adding incomplete events
                        if (eventName != null && eventDate != null && location != null) {
                            Event event = new Event(eventId, eventName, eventDate, location, maxAttendees, imageURL);
                            event.setRegisteredAttendees(registeredAttendees);
                            eventList.add(event);
                        }
                    }
                    listener.onEventsLoaded(eventList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error fetching events for organizer: " + e.getMessage());
                    listener.onEventsLoadFailed(e);
                });
    }

    /**
     * Retrieves the event data from firebase
     *
     * @param eventId The ID of the event to retrieve.
     */
    public void getEventDataById(String eventId, OnEventLoadedListener listener) {
        db.collection("events")
                .document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        if (document.exists()) {
                            String eventName = document.getString("eventName");
                            Date eventDate = document.getDate("eventDate");
                            String location = document.getString("location");
                            Integer maxAttendees = document.getLong("maxAttendees") != null ? document.getLong("maxAttendees").intValue() : null;
                            String imageURL = document.getString("imageURL");

                            Event event = new Event(eventId, eventName, eventDate, location, 0, imageURL);
                            event.setMaxAttendees(maxAttendees);

                            List<String> organizerEmails = (List<String>) document.get("organizers");
                            if (organizerEmails != null && !organizerEmails.isEmpty()) {
                                for (String email : organizerEmails) {
                                    getUserData(email, new OnUserLoadedListener() {
                                        @Override
                                        public void onUserLoaded(User user) {
                                            event.addOrganizer(user.getId());
                                            if (event.getOrganizers().size() == organizerEmails.size()) {
                                                fetchRegisteredAttendees(eventName, new OnRegisteredAttendeesLoadedListener() {
                                                    @Override
                                                    public void onRegisteredAttendeesLoaded(List<User> registeredAttendees) {
                                                        event.setRegisteredAttendees(registeredAttendees);
                                                        fetchCheckedInAttendees(eventName, new OnCheckInAttendeesLoadedListener() {
                                                            @Override
                                                            public void onCheckInAttendeesLoaded(List<User> checkedInAttendees) {
                                                                event.setCheckedInAttendees(checkedInAttendees);
                                                                listener.onEventLoaded(event);
                                                            }

                                                            @Override
                                                            public void onCheckInAttendeesLoadFailed(Exception e) {
                                                                listener.onEventLoadFailed(e);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onRegisteredAttendeesLoadFailed(Exception e) {
                                                        listener.onEventLoadFailed(e);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onUserNotFound() {
                                            Log.d("Firebase", "User not found");
                                        }

                                        @Override
                                        public void onUserLoadFailed(Exception e) {
                                            listener.onEventLoadFailed(e);
                                        }
                                    });
                                }
                            } else {
                                fetchRegisteredAttendees(eventName, new OnRegisteredAttendeesLoadedListener() {
                                    @Override
                                    public void onRegisteredAttendeesLoaded(List<User> registeredAttendees) {
                                        event.setRegisteredAttendees(registeredAttendees);
                                        fetchCheckedInAttendees(eventName, new OnCheckInAttendeesLoadedListener() {
                                            @Override
                                            public void onCheckInAttendeesLoaded(List<User> checkedInAttendees) {
                                                event.setCheckedInAttendees(checkedInAttendees);
                                                listener.onEventLoaded(event);
                                            }

                                            @Override
                                            public void onCheckInAttendeesLoadFailed(Exception e) {
                                                listener.onEventLoadFailed(e);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onRegisteredAttendeesLoadFailed(Exception e) {
                                        listener.onEventLoadFailed(e);
                                    }
                                });
                            }
                        } else {
                            listener.onEventNotFound();
                        }
                    } else {
                        listener.onEventLoadFailed(task.getException());
                    }
                });
    }

    /**
     * Retrieves the user data from Firebase.
     *
     * @return the user details for a particular email.
     * @param id The id of the user to retrieve.
     */
    public boolean confirmUserExists(String id) {
        getUserData(id, new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                // initialize the AppUser in case it hasn't already been done
                Log.d("User Found", "User was found");
                AppUser.setHasSignedIn(true);
            }

            @Override
            public void onUserNotFound() {
                // If the user is not found, it must have been deleted
                Log.d("Firebase", "User not found.");
                AppUser.setHasSignedIn(false);
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.e("Firebase", "User retrieval failed.");
                AppUser.setHasSignedIn(false);
            }
        });

        return AppUser.getHasSignedIn();
    }

    /**
     * removes the current user from the list of registered attendees for a specific event.
     * @param eventId the id for the event that registered attendees will be removed from
     * @param userId the id of the user that will be removed from the list of registered attendees
     * @param listener the listener for the removal of the user from the list of registered attendees
     */
    public void removeUserFromRegisteredAttendees(String eventId, String userId, OnAttendeeRemovedListener listener) {
        try {
            DocumentReference eventRef = db.collection("events").document(eventId);

            eventRef.update("registeredAttendees", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener(aVoid -> {
                        // User removed from registeredAttendees successfully
                        Log.i("Firebase", "User removed from registeredAttendees successfully");
                        listener.onAttendeeRemoved();
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while removing user from registeredAttendees
                        Log.e("Firebase", "Error removing user from registeredAttendees: " + e.getMessage());
                        listener.onAttendeeRemovalFailed(e);
                    });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error removing user from registeredAttendees: " + e.getMessage());
        }
    }

    public interface OnAttendeeRemovedListener {
        void onAttendeeRemoved();

        void onAttendeeRemovalFailed(Exception e);
    }

    /**
     * Change a past event into a new event, so the organizer can reuse the event ID.
     * This will allow them to reuse QR codes that are associated with the event ID.
     * It empties the list of registered attendees, checked-in attendees, and organizers.
     * It then adds the current user as the organizer.
     * It also clears the corresponding check-in data for the event.
     *
     * @param userId          the ID of the current user
     * @param eventId         the event that will be updated
     * @param newEventName    the new name for the event
     * @param newEventDate    the new date for the event
     * @param newLocation     the new location for the event
     * @param newMaxAttendees the new maximum number of attendees for the event
     * @param newImageURL     the new image URL for the event
     * @param listener        the listener for the event update
     */
    public void reusePastEvent(String userId, String eventId, String newEventName, Date newEventDate, String newLocation, int newMaxAttendees, String newImageURL, OnEventUpdatedListener listener) {
        DocumentReference eventRef = db.collection("events").document(eventId);
        CollectionReference checkInRef = db.collection("checkIn");
        Query query = checkInRef.whereEqualTo("eventId", eventId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    // Get the check-in document ID
                    String checkInId = snapshot.getDocuments().get(0).getId();

                    // Clear the "checkInList" array in the check-in document
                    checkInRef.document(checkInId).update("checkInList", new ArrayList<>())
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "Check-in data cleared successfully");

                                // Update the event document
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("eventName", newEventName);
                                updates.put("eventDate", newEventDate);
                                updates.put("location", newLocation);
                                updates.put("maxAttendees", newMaxAttendees);
                                updates.put("imageURL", newImageURL);
                                updates.put("registeredAttendees", new ArrayList<>());
                                updates.put("checkedInAttendees", new ArrayList<>());
                                updates.put("organizers", new ArrayList<>());

                                eventRef.update(updates)
                                        .addOnSuccessListener(aVoid1 -> {
                                            Log.d("Firebase", "Event transformed successfully");
                                            // Add the current user as the organizer
                                            eventRef.update("organizers", FieldValue.arrayUnion(userId))
                                                    .addOnSuccessListener(aVoid2 -> {
                                                        Log.d("Firebase", "Organizer added successfully");
                                                        listener.onEventUpdated();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("Firebase", "Error adding organizer: " + e.getMessage());
                                                        listener.onEventUpdateFailed(e);
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firebase", "Error transforming event: " + e.getMessage());
                                            listener.onEventUpdateFailed(e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Error clearing check-in data: " + e.getMessage());
                                listener.onEventUpdateFailed(e);
                            });
                } else {
                    // No check-in document found for the event
                    Log.d("Firebase", "No check-in data found for the event");
                    // Update the event document without clearing check-in data
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("eventName", newEventName);
                    updates.put("eventDate", newEventDate);
                    updates.put("location", newLocation);
                    updates.put("maxAttendees", newMaxAttendees);
                    updates.put("imageURL", newImageURL);
                    updates.put("registeredAttendees", new ArrayList<>());
                    updates.put("checkedInAttendees", new ArrayList<>());
                    updates.put("organizers", new ArrayList<>());

                    eventRef.update(updates)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "Event transformed successfully");
                                // Add the current user as the organizer
                                eventRef.update("organizers", FieldValue.arrayUnion(userId))
                                        .addOnSuccessListener(aVoid1 -> {
                                            Log.d("Firebase", "Organizer added successfully");
                                            listener.onEventUpdated();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firebase", "Error adding organizer: " + e.getMessage());
                                            listener.onEventUpdateFailed(e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Error transforming event: " + e.getMessage());
                                listener.onEventUpdateFailed(e);
                            });
                }
            } else {
                // Error occurred while querying check-in data
                Log.e("Firebase", "Error querying check-in data: " + task.getException().getMessage());
                listener.onEventUpdateFailed(task.getException());
            }
        });
    }

    /**
     * Retrieves Geopoints from the list of locations in the firebase
     * These locations will be used to check where attendees are checking in from
     * @param eventName The name of the event to retrieve locations for.
     * @param listener Listener for handling locations retrieval.
     */
    public void retrieveLocations(String eventName, OnLocationsRetrievedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference locationsRef = db.collection("events").document(eventName).collection("locations");

        locationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<GeoPoint> locations = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    GeoPoint location = document.getGeoPoint("location");
                    if (location != null) {
                        locations.add(location);
                    }
                }
                listener.onLocationsRetrieved(locations);
            } else {
                Log.e("firebase", "Error getting locations: ", task.getException());
                listener.onLocationsRetrievalFailed(task.getException());
            }
        });
    }

    public interface OnLocationsRetrievedListener {
        void onLocationsRetrieved(List<GeoPoint> locations);
        void onLocationsRetrievalFailed(Exception e);
    }

    /**
     * either creates the location list in the event, if it doesn't exist, or adds a new location to the list
     * These will be stored as geopoints in the firebase
     * @param eventName The name of the event to add the location to.
     * @param location The location to add.
     */
    public void addLocationToEvent(String eventName, GeoPoint location) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventName);

        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot eventSnapshot = task.getResult();
                if (eventSnapshot.exists()) {
                    // Event document exists, add the location to the existing list
                    eventRef.collection("locations").add(new HashMap<String, Object>() {{
                        put("location", location);
                    }}).addOnSuccessListener(documentReference -> {
                        Log.d("firebase", "Location added to event: " + eventName);
                    }).addOnFailureListener(e -> {
                        Log.e("firebase", "Error adding location to event: ", e);
                    });
                } else {
                    // Event document doesn't exist, create it with a new location list
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("name", eventName);
                    eventData.put("locations", Arrays.asList(new HashMap<String, Object>() {{
                        put("location", location);
                    }}));

                    eventRef.set(eventData).addOnSuccessListener(aVoid -> {
                        Log.d("firebase", "Event created with location: " + eventName);
                    }).addOnFailureListener(e -> {
                        Log.e("firebase", "Error creating event with location: ", e);
                    });
                }
            } else {
                Log.e("firebase", "Error checking event existence: ", task.getException());
            }
        });
    }
}
