package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.ImageViewUpdater;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventCreationFragment extends Fragment {

    private EditText eventNameEditText, eventDateEditText, locationEditText, maxAttendeesEditText;
    private ImageView eventImageView;
    private Calendar eventDateCalendar;
    private ActivityResultLauncher<String> galleryLauncher;

    private Uri selectedImageUri;
    private String updatedImageURL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_creation_fragment, container, false);

        eventDateCalendar = Calendar.getInstance();

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        eventNameEditText = view.findViewById(R.id.eventNameEditText);
        eventDateEditText = view.findViewById(R.id.eventDateEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        eventImageView = view.findViewById(R.id.eventImageView);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button createEventButton = view.findViewById(R.id.createEventButton);
        Button reuseEventIdButton = view.findViewById(R.id.reuseQRCodeButton);
        maxAttendeesEditText = view.findViewById(R.id.maxAttendeesEditText);

        eventDateEditText.setOnClickListener(v -> showDatePickerDialog());
        createEventButton.setOnClickListener(v -> createEvent());
        selectImageButton.setOnClickListener(v -> selectImage());
        reuseEventIdButton.setOnClickListener(v -> reuseEventId());


        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        ImageViewUpdater.updateImageView(getActivity(), result, eventImageView);
                        selectedImageUri = result;
                    }
                });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    /**
     * Shows a DatePickerDialog to allow the user to select the event date.
     */
    private void showDatePickerDialog() {
        int year = eventDateCalendar.get(Calendar.YEAR);
        int month = eventDateCalendar.get(Calendar.MONTH);
        int day = eventDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            eventDateCalendar.set(Calendar.YEAR, year1);
            eventDateCalendar.set(Calendar.MONTH, monthOfYear);
            eventDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Updates the eventDateEditText field with the selected date.
     */
    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        eventDateEditText.setText(sdf.format(eventDateCalendar.getTime()));
    }

    /**
     * Gathers input from the user and attempts to create a new event. Performs validation
     * to ensure required fields are filled and that maxAttendees, if provided, is an integer.
     * Shows a toast message if validation fails or if the event creation is successful.
     */
    private void createEvent() {
        String eventName = eventNameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String maxAttendeesStr = maxAttendeesEditText.getText().toString();
        int maxAttendees = Integer.MAX_VALUE; // Default if no input
        if (TextUtils.isEmpty(eventName) || eventDateCalendar == null || TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if maxAttendeesStr is not empty and is an integer
        if (!maxAttendeesStr.isEmpty()) {
            try {
                maxAttendees = Integer.parseInt(maxAttendeesStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Max attendees field must to be an integer or empty.", Toast.LENGTH_SHORT).show();
                return; // Stop the method execution if input is not valid
            }
        }

        if (selectedImageUri != null) {
            // Upload image to Firebase Storage and get the download URL
            int finalMaxAttendees = maxAttendees;
            Firebase.uploadImageAndGetUrl("event_images/" + System.currentTimeMillis() + ".jpg", selectedImageUri, new Firebase.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(String imageURL) {
                    // Image upload is complete, now create the Event
                    createAndSaveEvent(eventName, location, imageURL, finalMaxAttendees);
                }

                @Override
                public void onUploadFailed(String errorMessage) {
                    // Handle the image upload failure
                    Log.e("Firebase", "Failed to upload image: " + errorMessage);
                }
            });
        } else {
            // If selectedImageUri is null, proceed to create event without imageURL
            createAndSaveEvent(eventName, location, null, maxAttendees);
        }
    }

    /**
     * Helper method to finalize event creation, including uploading the event image
     * (if selected) to Firebase Storage and saving the event details to Firestore.
     *
     * @param eventName The name of the event.
     * @param location The location of the event.
     * @param imageURL The URL of the uploaded event image; null if no image was selected.
     * @param maxAttendees The maximum number of attendees; defaults to Integer.MAX_VALUE if not specified.
     */
    private void createAndSaveEvent(String eventName, String location, @Nullable String imageURL, Integer maxAttendees) {
        Event newEvent = new Event(
                "",
                eventName,
                eventDateCalendar.getTime(),
                location,
                maxAttendees,
                imageURL
        );
        // Add the new event to Firebase
        Firebase firebase = new Firebase();
        firebase.getUserData(AppUser.getUserId(), new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                firebase.createEvent(newEvent, user);
            }

            @Override
            public void onUserNotFound() {
                Log.d("spaghetti code", "aaahahaahah");
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.d("spaghetti code", "aaahahaahah");
            }
        });

        getParentFragmentManager().popBackStack();
    }

    private void reuseEventId() {
        //shows an alert dialog where the current user can choose from a list of events that they're an organizer of.
        //This will allow for the reusing of the QR codes in existence to map to a different event
        //The user will be able to select an event from the list and map all the updated info into an existing event, overwriting the data and resetting the attendees
        Firebase firebase = new Firebase();
        // Retrieve the current user's ID
        String currentUserId = AppUser.getUserId();

        // Fetch the events for the current user as an organizer
        firebase.fetchEventsForOrganizer(currentUserId, new Firebase.OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(List<Event> organizerEvents) {
                // Create an array of event names
                String[] eventNames = new String[organizerEvents.size()];
                for (int i = 0; i < organizerEvents.size(); i++) {
                    eventNames[i] = organizerEvents.get(i).getEventName();
                }

                // Create an alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(EventCreationFragment.this.getContext());
                builder.setTitle("Select Event to Reuse")
                        .setSingleChoiceItems(eventNames, -1, null)
                        .setPositiveButton("Create Event", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition >= 0) {
                                    // Get the selected event
                                    Event selectedEvent = organizerEvents.get(selectedPosition);
                                    String updatedEventName = eventNameEditText.getText().toString();
                                    Date updatedEventDate = eventDateCalendar.getTime();
                                    String updatedLocation = locationEditText.getText().toString();
                                    String maxAttendeesText = maxAttendeesEditText.getText().toString();
                                    int updatedMaxAttendees = maxAttendeesText.isEmpty() ? 0 : Integer.parseInt(maxAttendeesText);

                                    if (selectedImageUri != null) {
                                        Firebase.uploadImageAndGetUrl("event_images/" + System.currentTimeMillis() + ".jpg", selectedImageUri, new Firebase.OnUploadCompleteListener() {
                                            @Override
                                            public void onUploadComplete(String imageURL) {
                                                // Image upload is complete, now create the Event
                                                updatedImageURL = imageURL;
                                                firebase.reusePastEvent(currentUserId, selectedEvent.getEventId(),
                                                        updatedEventName, updatedEventDate, updatedLocation,
                                                        updatedMaxAttendees, updatedImageURL, new Firebase.OnEventUpdatedListener() {
                                                            @Override
                                                            public void onEventUpdated() {
                                                                // Event updated successfully
                                                                Log.d("CreateEventActivity", "Event data updated successfully");
                                                                // Perform any additional actions as needed
                                                            }

                                                            @Override
                                                            public void onEventUpdateFailed(Exception e) {
                                                                // Event update failed
                                                                Log.e("CreateEventActivity", "Failed to update event data: " + e.getMessage());
                                                                // Handle the failure case, e.g., display an error message
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onUploadFailed(String errorMessage) {
                                                // Handle the image upload failure
                                                Log.e("Firebase", "Failed to upload image: " + errorMessage);
                                            }
                                        });
                                    } else {
                                        // If selectedImageUri is null, proceed to create event without imageURL
                                        Log.d("EventCreationFragment", "no image URI ");
                                    }


                                } else {
                                    // No event selected, log an error message or perform any other necessary actions
                                    Log.e("CreateEventActivity", "No event selected to reuse");
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);

                // Show the alert dialog
                builder.create().show();
            }

            @Override
            public void onEventsLoadFailed(Exception e) {
                // Handle the failure case, e.g., log an error message
                Log.e("CreateEventActivity", "Failed to load events: " + e.getMessage());
            }
        });
    }

    /**
     * Initiates the process of selecting an image from the device's gallery.
     */
    private void selectImage() {
        galleryLauncher.launch("image/*");
    }
}