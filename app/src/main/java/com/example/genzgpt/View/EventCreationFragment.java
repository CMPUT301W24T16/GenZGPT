package com.example.genzgpt.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.genzgpt.Controller.GalleryHandler;
import com.example.genzgpt.Controller.ImageViewUpdater;
import com.example.genzgpt.MainActivity;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventCreationFragment extends Fragment {

    private EditText eventNameEditText, eventDateEditText, locationEditText;
    private ImageView eventImageView;
    private Button selectImageButton, createEventButton;
    private Calendar eventDateCalendar;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri selectedImageUri;


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
        selectImageButton = view.findViewById(R.id.selectImageButton);
        createEventButton = view.findViewById(R.id.createEventButton);

        eventDateEditText.setOnClickListener(v -> showDatePickerDialog());
        createEventButton.setOnClickListener(v -> createEvent());
        selectImageButton.setOnClickListener(v -> selectImage());

        // Register a FragmentResultListener to handle the result from the GalleryHandler
        getParentFragmentManager().setFragmentResultListener("galleryResult",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Uri selectedImageUri = result.getParcelable("selectedImageUri");
                        processGalleryResult(Activity.RESULT_OK, new Intent().setData(selectedImageUri));
                    }
                });

        // Initialize the ActivityResultLauncher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        processGalleryResult(resultCode, data);
                    }
                }
        );

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

    private void showDatePickerDialog() {
        int year = eventDateCalendar.get(Calendar.YEAR);
        int month = eventDateCalendar.get(Calendar.MONTH);
        int day = eventDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            eventDateCalendar.set(Calendar.YEAR, year1);
            eventDateCalendar.set(Calendar.MONTH, monthOfYear);
            eventDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        eventDateEditText.setText(sdf.format(eventDateCalendar.getTime()));
    }

    private void createEvent() {
        String eventName = eventNameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        if (TextUtils.isEmpty(eventName) || eventDateCalendar == null || TextUtils.isEmpty(location)) {
            return;
        }

        if (selectedImageUri != null) {
            // Upload image to Firebase Storage and get the download URL
            Firebase.uploadImageAndGetUrl("event_images/" + System.currentTimeMillis() + ".jpg", selectedImageUri, new Firebase.OnUploadCompleteListener() {
                @Override
                public void onUploadComplete(String imageURL) {
                    // Image upload is complete, now create the Event
                    Event newEvent = new Event(
                            "",
                            eventName,
                            eventDateCalendar.getTime(),
                            location,
                            100,
                            imageURL // Set the imageURL obtained from Firebase Storage
                    );

                    // Add the new event to Firebase
                    Firebase firebase = new Firebase();
                    AppUser appUserInstance = AppUser.getInstance();
                    firebase.createEvent(newEvent, appUserInstance);
                    getParentFragmentManager().popBackStack();
                }

                @Override
                public void onUploadFailed(String errorMessage) {
                    // Handle the image upload failure
                    Log.e("Firebase", "Failed to upload image: " + errorMessage);
                }
            });
        }
    }

    private void selectImage() {
        galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    private void processGalleryResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ImageViewUpdater.updateImageView(requireActivity(), selectedImageUri, eventImageView);
        }
    }
}
