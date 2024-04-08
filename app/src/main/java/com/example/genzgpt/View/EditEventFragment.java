package com.example.genzgpt.View;

import android.app.DatePickerDialog;
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

import com.bumptech.glide.Glide;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.ImageViewUpdater;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEventFragment extends Fragment {

    private EditText eventNameEditText, eventDateEditText, locationEditText, maxAttendeesEditText;
    private ImageView eventImageView;
    private Calendar eventDateCalendar;
    private ActivityResultLauncher<String> galleryLauncher;
    private Uri selectedImageUri;
    public Event eventCurrent;

    public EditEventFragment(Event event){this.eventCurrent = event;}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        eventDateCalendar = Calendar.getInstance();

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        eventNameEditText = view.findViewById(R.id.editEventNameEditText);
        eventDateEditText = view.findViewById(R.id.editEventDateEditText);
        locationEditText = view.findViewById(R.id.editLocationEditText);
        eventImageView = view.findViewById(R.id.editEventImageView);
        Button selectImageButton = view.findViewById(R.id.editSelectImageButton);
        Button createEventButton = view.findViewById(R.id.confirmEditEventButton);
        maxAttendeesEditText = view.findViewById(R.id.editMaxAttendeesEditText);

        eventDateEditText.setOnClickListener(v -> showDatePickerDialog());
        createEventButton.setOnClickListener(v -> createEvent());
        selectImageButton.setOnClickListener(v -> selectImage());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        ImageViewUpdater.updateImageView(getActivity(), result, eventImageView);
                        selectedImageUri = result;
                    }
                });

        if (eventCurrent != null) {
            Bind(eventCurrent);
        }

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
            Log.d("EditEventFragment", "Updating event with name: " + eventName + " and location: " + location);

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
            firebase.updateEvent(eventCurrent.getEventId(), eventName, eventDateCalendar.getTime(), location, imageURL, maxAttendees, new Firebase.OnEventUpdatedListener() {
                    @Override
                    public void onEventUpdated() {
                        // Update profile picture if available
                        if (eventCurrent!= null && eventCurrent.getImageURL() != null) {
                            Glide.with(requireContext())
                                    .load(eventCurrent.getImageURL()) // Load image URL
                                    .into(eventImageView); // ImageView to load the image into
                        }
                    }
                    @Override
                    public void onEventUpdateFailed(Exception e) {
                            Log.e("EventEdit", "Event update failed.");
                    }
                });

        getParentFragmentManager().popBackStack();
    }



    /**
     * Initiates the process of selecting an image from the device's gallery.
     */
    private void selectImage() {
        galleryLauncher.launch("image/*");
    }
    public void Bind(Event event){
        eventNameEditText.setText(event.getEventName());
        updateLabel();
        locationEditText.setText(event.getLocation());
        maxAttendeesEditText.setText(String.valueOf(event.getMaxAttendees()));
    }
}
