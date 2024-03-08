package com.example.genzgpt.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is responsible for handling all interactions with Firebase.
 */
public class Firebase {

    private String email;
    private final FirebaseFirestore db;
    //Handle Firebase interactions

    /**

     * Uploads an image to Firebase Storage and associates it with the specified event.
     *
     * @param eventID   ID of the event to associate with the image.
     * @param imageUri  Uri of the image to upload.
     * @param progressDialog Progress dialog for showing upload progress.
     * @param context   Context for displaying toasts.
     */
    public static void uploadImageForEvent(String eventID, Uri imageUri, ProgressDialog progressDialog, Context context) {
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
     * @param eventID   ID of the event to update.
     * @param imageURL  URL of the uploaded image.
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
     * Uploads an image to Firebase Storage and associates it with the specified user.
     *
     * @param userID   ID of the event to associate with the image.
     * @param imageUri  Uri of the image to upload.
     * @param progressDialog Progress dialog for showing upload progress.
     * @param context   Context for displaying toasts.
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
     * @param imageURL  URL of the uploaded image.
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
     * @param eventId  ID of the event containing the image.
     * @param imageURL URL of the image to be deleted.
     */
    public void deleteEventImage(String eventId, String imageURL) {
        // Delete the image data in Firestore
        db.collection("events").document(eventId)
                .update("imageURL", FieldValue.delete())
                .addOnSuccessListener(aVoid -> {
                    Log.i("Firebase", "Image URL deleted from Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error deleting image URL from Firestore: " + e.getMessage());
                });

        // Delete the image file from Firebase Storage
        if (imageURL != null && !imageURL.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL);
            storageReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.i("Firebase", "Image deleted from Firebase Storage");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error deleting image from Firebase Storage: " + e.getMessage());
                    });
        }
    }

    /**
     * Delete an Event image from Firestore and Firebase Storage.
     *
     * @param userID  ID of the event containing the image.
     * @param imageURL URL of the image to be deleted.
     */
    public void deletUserImage(String userID, String imageURL) {
        // Delete the image data in Firestore
        db.collection("users").document(userID)
                .update("imageURL", FieldValue.delete())
                .addOnSuccessListener(aVoid -> {
                    Log.i("Firebase", "Image URL deleted from Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error deleting image URL from Firestore: " + e.getMessage());
                });

        // Delete the image file from Firebase Storage
        if (imageURL != null && !imageURL.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL);
            storageReference.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.i("Firebase", "Image deleted from Firebase Storage");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error deleting image from Firebase Storage: " + e.getMessage());
                    });
        }
    }
    /**
     * Retrieves the user data from Firebase.
     * @return the user details for a particular email.
     * @param email
     */
    public void getUserData(String email, OnUserLoadedListener listener) {
        db.collection("users")
                .document(email)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        Long phoneNumber = document.getLong("phoneNumber");
                        boolean geolocation = Boolean.TRUE.equals(document.getBoolean("geolocation"));
                        String userID = document.getId();
                        String imageURL = document.getString("imageURL");

                        User user = new User(userID, firstName, lastName, phoneNumber, email, geolocation, imageURL);
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
     * Retrieves the list of events from the database.
     * @return the event details for a particular event name.
     * @param eventName
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
                                            event.addOrganizer(user);
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

    public interface OnEventLoadedListener {
        void onEventLoaded(Event event);
        void onEventNotFound();
        void onEventLoadFailed(Exception e);
    }

    /**
     * Retrieves the user data from Firebase.
     * @return the user details for a particular email.
     * @param userMap
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
     * @param user
     */
    public void createUser(User user) {
        try {
            // Check if the user already exists
            DocumentReference userRef = db.collection("users").document(user.getEmail());
            DocumentSnapshot userSnapshot = userRef.get().getResult();

            if (userSnapshot.exists()) {
                // User already exists, handle accordingly (e.g., throw an exception or return an error)
                throw new IllegalArgumentException("User with ID " + user.getEmail() + " already exists");
            } else {
                // Create a new user document
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("firstName", user.getFirstName());
                userData.put("lastName", user.getLastName());
                userData.put("email", user.getEmail());
                userData.put("phoneNumber", user.getPhone());
                userData.put("geolocation", user.isGeolocation());

                userRef.set(userData)
                        .addOnSuccessListener(aVoid -> {
                            // User created successfully
                            Log.i("Firebase", "User created successfully");
                        })
                        .addOnFailureListener(e -> {
                            // Error occurred while creating the user
                            Log.e("Firebase", "Error creating user: " + e.getMessage());
                        });
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            System.err.println("Error creating user: " + e.getMessage());
        }
    }
    /**
     * Creates a new event in the database.
     * @param organizer
     * @param event
     */
    public void createEvent(Event event, User organizer) {
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

            // Add the organizer's email to the list of organizers
            List<String> organizerEmails = new ArrayList<>();
            organizerEmails.add(organizer.getEmail());
            eventData.put("organizers", organizerEmails);

            // Initialize empty lists for registered attendees and checked-in attendees
            eventData.put("registeredAttendees", new ArrayList<>());
            eventData.put("checkedInAttendees", new ArrayList<>());

            eventRef.set(eventData)
                    .addOnSuccessListener(aVoid -> {
                        // Event created successfully
                        Log.i("Firebase", "Event created successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while creating the event
                        Log.e("Firebase", "Error creating event: " + e.getMessage());
                    });
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            Log.e("Firebase", "Error creating event: " + e.getMessage());
        }
    }

    /**
     * Adds a user to the list of registered attendees for a specific event.
     * @param eventName
     * @param userEmail
     */
    public void addUserToCheckedInAttendees(String eventName, String userEmail) {
        //fixme get rid of context
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
                        eventRef.update("checkedInAttendees", FieldValue.arrayUnion(userEmail))
                                .addOnSuccessListener(aVoid -> {
                                    // User added to checkedInAttendees successfully
                                    Log.i("Firebase", "User added to checkedInAttendees successfully");
                                })
                                .addOnFailureListener(e -> {
                                    // Error occurred while adding user to checkedInAttendees
                                    Log.e("Firebase", "Error adding user to checkedInAttendees: " + e.getMessage());
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
            Log.e("Firebase", "Error adding user to checkedInAttendees: " + e.getMessage());
        }
    }


    /**
     * set the email of the user
     * @param email
     */
    private void setEmail(String email){
        this.email = email;
    }

    /**
     * firebase constructor
     */
    public Firebase() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Deletes an event from the database.
     * @param eventName
     */
    public void deleteEvent(String eventName) {
        try {
            CollectionReference eventsRef = db.collection("events");
            Query query = eventsRef.whereEqualTo("event_name", eventName);

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
            Log.e("Firebase", "Error deleting event: " + e.getMessage());
        }
    }

    /**
     * Deletes a user from the database.
     * @param userEmail
     */
    public void deleteUser(String userEmail) {
        try {
            CollectionReference usersRef = db.collection("users");
            Query query = usersRef.whereEqualTo("email", userEmail);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        // Delete each matching user document
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
                        // No users found with the specified email
                        Log.i("Firebase", "No users found with the email: " + userEmail);
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
     * @return list of events
     * Synchronous method
     */
    public void fetchUsers(OnUsersLoadedListener listener) {
        db.collection("users").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<User> userList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Extract user data from the document
                        String userID = document.getId(); //comment this out if we get rid of userID
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String email = document.getString("email");
                        Long phoneNumber = document.getLong("phoneNumber");
                        boolean geolocation = document.getBoolean("geolocation") != null && Boolean.TRUE.equals(document.getBoolean("geolocation"));
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
        db.collection("events").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Event> eventList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Extract event data from the document
                        String eventId = document.getId();
                        String eventName = document.getString("event_name");
                        Date eventDate = document.getDate("start_time");
                        String location = document.getString("event_location");
                        Integer maxAttendees = document.getLong("maxAttendees") != null ? document.getLong("maxAttendees").intValue() : null;
                        String imageURL = document.getString("imageURL"); // Retrieve image URL

                        Event event = new Event(eventId, eventName, eventDate, location, maxAttendees, imageURL);
                        eventList.add(event);
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
     * Retrieves the list of checked-in attendees for a specific event from the database.
     * @param eventName
     * @return list of checked-in attendees
     * asynchronous method
     */
    public void fetchCheckedInAttendees(String eventName, OnCheckInAttendeesLoadedListener listener) {
        CollectionReference eventsRef = db.collection("events");
        Query query = eventsRef.whereEqualTo("eventName", eventName);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    // Assuming there is only one event with the given name
                    DocumentSnapshot eventDocument = snapshot.getDocuments().get(0);
                    List<String> checkedInAttendeesEmails = (List<String>) eventDocument.get("checkedInAttendees");

                    // Create a list to store the User objects
                    List<User> checkedInAttendees = new ArrayList<>();

                    // Create a counter to keep track of loaded users
                    AtomicInteger loadedUserCount = new AtomicInteger(0);

                    // Iterate through the list of attendee emails and fetch their user data
                    for (String email : checkedInAttendeesEmails) {
                        getUserData(email, new OnUserLoadedListener() {
                            @Override
                            public void onUserLoaded(User user) {
                                checkedInAttendees.add(user);
                                if (loadedUserCount.incrementAndGet() == checkedInAttendeesEmails.size()) {
                                    // All users have been loaded
                                    listener.onCheckInAttendeesLoaded(checkedInAttendees);
                                }
                            }

                            @Override
                            public void onUserNotFound() {
                                if (loadedUserCount.incrementAndGet() == checkedInAttendeesEmails.size()) {
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
                    // No events found with the specified name
                    listener.onCheckInAttendeesLoadFailed(new Exception("No events found with the name: " + eventName));
                }
            } else {
                // Error occurred while querying events
                listener.onCheckInAttendeesLoadFailed(task.getException());
            }
        });
    }


    public interface OnCheckInAttendeesLoadedListener {
        void onCheckInAttendeesLoaded(List<User> checkedInAttendees);
        void onCheckInAttendeesLoadFailed(Exception e);
    }

    /**
     * Retrieves the list of registered attendees for a specific event from the database.
     * @param eventName
     * @return list of registered attendees
     * asynchronous method
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
                    List<String> registeredAttendeesEmails = (List<String>) eventDocument.get("registeredAttendees");

                    // Create a list to store the User objects
                    List<User> registeredAttendees = new ArrayList<>();

                    // Create an AtomicInteger to keep track of the number of loaded users
                    AtomicInteger loadedUserCount = new AtomicInteger(0);

                    // Iterate through the list of attendee emails and fetch their user data
                    for (String email : registeredAttendeesEmails) {
                        getUserData(email, new OnUserLoadedListener() {
                            @Override
                            public void onUserLoaded(User user) {
                                registeredAttendees.add(user);
                                if (loadedUserCount.incrementAndGet() == registeredAttendeesEmails.size()) {
                                    listener.onRegisteredAttendeesLoaded(registeredAttendees);
                                }
                            }

                            @Override
                            public void onUserNotFound() {
                                if (loadedUserCount.incrementAndGet() == registeredAttendeesEmails.size()) {
                                    listener.onRegisteredAttendeesLoaded(registeredAttendees);
                                }
                            }

                            @Override
                            public void onUserLoadFailed(Exception e) {
                                listener.onRegisteredAttendeesLoadFailed(e);
                            }
                        });
                    }

                    // If there are no registered attendees, call the onRegisteredAttendeesLoaded callback immediately
                    if (registeredAttendeesEmails.isEmpty()) {
                        listener.onRegisteredAttendeesLoaded(registeredAttendees);
                    }
                } else {
                    // No events found with the specified name
                    listener.onRegisteredAttendeesLoadFailed(new Exception("No events found with the name: " + eventName));
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


}
