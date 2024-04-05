package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.QRCodeGenerator;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * A Fragment class to display detailed information about an event.
 * It allows users to sign up for or withdraw from the event, and displays event details
 * such as name, date, location, and an image. Additionally, it generates and displays
 * a QR code for event sign-up and offers functionality to save the QR code to the device.
 */
public class EventInfoFragment extends Fragment {

    private Event event;
    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationTextView;
    private ImageView eventImageView, qrCodeImageView;
    private Firebase firebase;
    private Button signUpButton;
    private boolean isUserSignedUp = false;
//    AppUser appUserInstance = AppUser.getInstance();


    /**
     * Constructor that takes an Event object. This should be used when an instance of EventInfoFragment
     * is needed to display information about a specific event.
     *
     * @param event The event to display information about.
     */
    public EventInfoFragment(Event event) {
        this.event = event;
    }

    /**
     * Default constructor. Required for instantiating the fragment without passing an Event object.
     */
    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflates the layout and initializes UI components
        View view = inflater.inflate(R.layout.event_info_fragment, container, false);
        initializeViews(view);
        displayEventData();
        firebase = new Firebase();

        signUpButton = view.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(v -> {
            if (!isUserSignedUp) {
                signUpForEvent();
            } else {
                withdrawFromEvent();
            }
        });

        view.findViewById(R.id.backArrowImageView).setOnClickListener(v -> getParentFragmentManager().popBackStack());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Sets up back navigation
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void initializeViews(View view) {
        // Initialization logic
        eventNameTextView = view.findViewById(R.id.event_name);
        eventDateTextView = view.findViewById(R.id.event_date);
        eventLocationTextView = view.findViewById(R.id.event_location);
        eventImageView = view.findViewById(R.id.event_poster);
        qrCodeImageView = view.findViewById(R.id.qr_code_image_view); // Modify this line
    }

    /**
     * Displays the event data on the initialized views. This includes setting text on TextViews
     * and loading the event image and QR code into ImageView.
     */
    private void displayEventData() {
        // Directly display event data using the provided Event object
        if (event != null) {
            eventNameTextView.setText(event.getEventName());
            eventDateTextView.setText(event.getEventDate().toString());
            eventLocationTextView.setText(event.getLocation());

            if (event.getImageURL() != null && !event.getImageURL().isEmpty()) {
                // Load the image using Picasso and resize it to fit into a specific size
                Picasso.get()
                        .load(event.getImageURL())
                        .into(eventImageView);
            }
            // Generate a QR code for Sign-Up and display it
            Bitmap signUpQrCode = QRCodeGenerator.generateSignUpQRCode(event.getEventId(), 200, 200);
            if (signUpQrCode != null) {
                qrCodeImageView.setImageBitmap(signUpQrCode);
                // Set an OnClickListener for the ImageView
                qrCodeImageView.setOnClickListener(v -> showSaveQrCodeDialog());
            }
        }
    }

    /**
     * Saves the QR code to the device's gallery. Displays a toast message indicating
     * the outcome of the operation.
     */
    private void showSaveQrCodeDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Save QR Code")
                .setMessage("Do you want to save this QR code to your device?")
                .setPositiveButton("Yes", (dialog, which) -> saveQrCodeToDevice())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private void saveQrCodeToDevice() {
        if (qrCodeImageView.getDrawable() == null) {
            Toast.makeText(getContext(), "QR Code image is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert ImageView content to Bitmap
        Bitmap bitmap = ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap();

        // Use MediaStore to save the bitmap
        String savedImageURL = MediaStore.Images.Media.insertImage(
                requireContext().getContentResolver(),
                bitmap,
                event.getEventName() +" Registration", // Use event name as part of the image title
                "Registration QR Code for " + event.getEventName()); // Use event name as part of the image description

        if (savedImageURL == null) {
            Toast.makeText(getContext(), "Failed to save QR code", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "QR Code saved to Gallery", Toast.LENGTH_SHORT).show();
            Log.d("EventInfoFragment", "Image saved to: " + savedImageURL);
        }
    }

    public void signUpForEvent() {
        fetchUserData(AppUser.getUserId(), true);
    }

    public void withdrawFromEvent() {
        // Implement logic to withdraw the user from the event
//        fetchUserData(AppUser.getInstance().getEmail(), false);
    }

    /**
     * Fetches user data from Firebase based on the provided user ID. Depending on the isSignUp flag,
     * it either signs up the user for the event or withdraws them.
     *
     * @param userId   The user ID for fetching user data.
     * @param isSignUp A flag indicating whether the operation is to sign up (true) or withdraw (false).
     */
    private void fetchUserData(String userId, boolean isSignUp) {
        firebase.getUserData(userId, new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {

                if (isSignUp) {
                    registerUserForEvent(user);
                } else {
                    unregisterUserFromEvent(user);
                }
            }

            @Override
            public void onUserNotFound() {
                Log.d("EventInfoFragment", "User not found");
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.e("EventInfoFragment", "Error loading user data: " + e.getMessage());
            }
        });
    }

    /**
     * Registers the user as an attendee for the event. On successful registration,
     * updates UI to reflect the sign-up status.
     *
     * @param user The user to register as an attendee.
     */
    private void registerUserForEvent(User user) {
        firebase.registerAttendee(event, user, new Firebase.OnAttendeeRegisteredListener() {
            @Override
            public void onAttendeeRegistered() {
                Toast.makeText(getContext(), "You have signed up for the event!", Toast.LENGTH_SHORT).show();
                isUserSignedUp = true;
                signUpButton.setText(R.string.withdraw);
            }

            @Override
            public void onAttendeeRegistrationFailed(Exception e) {
                // display a specific error message
                if (Objects.requireNonNull(e.getMessage()).contains("Event is at full capacity")) {
                    Toast.makeText(getContext(), "Event is at full capacity. Cannot sign up.", Toast.LENGTH_LONG).show();
                } else {
                    // For other errors
                    Toast.makeText(getContext(), "Failed to sign up for the event. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onEventNotFound() {
                Toast.makeText(getContext(), "Event not found.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onEventLoadFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to load event data. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Unregisters the user from the event. Updates the UI to reflect the new status.
     *
     * @param user The user to unregister from the event.
     */
    private void unregisterUserFromEvent(User user) {
        firebase.removeUserFromRegisteredAttendees(event.getEventId(), user.getId(), new Firebase.OnAttendeeRemovedListener() {
            @Override
            public void onAttendeeRemoved() {
                Toast.makeText(getContext(), "You have withdrawn from the event.", Toast.LENGTH_SHORT).show();
                isUserSignedUp = false;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        getParentFragmentManager().popBackStack();
                    });
                }
            }

            @Override
            public void onAttendeeRemovalFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to withdraw from the event.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}