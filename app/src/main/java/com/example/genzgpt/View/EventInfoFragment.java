package com.example.genzgpt.View;

import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.QRCodeGenerator;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;
import com.example.genzgpt.Model.AppUser;


public class EventInfoFragment extends Fragment {

    private Event event;
    private TextView eventNameTextView, eventDateTextView, eventLocationTextView, eventAttendeesTextView;
    private ImageView eventImageView, qrCodeImageView;
    private Firebase firebase;
    private Button signUpButton;
    private boolean isUserSignedUp = false;
//    AppUser appUserInstance = AppUser.getInstance();

    // Constructor now takes an Event object
    public EventInfoFragment(Event event) {
        this.event = event;
    }

    // Default constructor
    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        view.findViewById(R.id.back_button).setOnClickListener(v -> getParentFragmentManager().popBackStack());
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

    private void initializeViews(View view) {
        eventNameTextView = view.findViewById(R.id.event_name);
        eventDateTextView = view.findViewById(R.id.event_date);
        eventLocationTextView = view.findViewById(R.id.event_location);
        eventImageView = view.findViewById(R.id.event_poster);
        qrCodeImageView = view.findViewById(R.id.qr_code_image_view); // Modify this line
    }

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
                        .resize(800, 800) // Specify the desired dimensions
                        .centerCrop() // Crop the image from the center if necessary
                        .into(eventImageView);
            }
            // Generate a QR code for Sign-Up and display it
            Bitmap signUpQrCode = QRCodeGenerator.generateSignUpQRCode(event.getEventId(), 200, 200);
            if (signUpQrCode != null) {
                qrCodeImageView.setImageBitmap(signUpQrCode);
            }
        }
    }

    public void signUpForEvent() {
        fetchUserData(AppUser.getUserId(), true);
    }

    public void withdrawFromEvent() {
        // Implement logic to withdraw the user from the event
//        fetchUserData(AppUser.getInstance().getEmail(), false);
    }


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
    private void registerUserForEvent(User user) {
        firebase.registerAttendee(event, user, new Firebase.OnAttendeeRegisteredListener() {
            @Override
            public void onAttendeeRegistered() {
                Toast.makeText(getContext(), "You have signed up for the event!", Toast.LENGTH_SHORT).show();
                isUserSignedUp = true;
                signUpButton.setText("Withdraw");
            }

            @Override
            public void onAttendeeRegistrationFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to sign up for the event!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventNotFound() {

            }

            @Override
            public void onEventLoadFailed(Exception e) {

            }
        });
    }

    private void unregisterUserFromEvent(User user) {
        // Implement the logic to unregister the user from the event
        // After successfully unregistering, update the button and flag
        Toast.makeText(getContext(), "You have withdrawn from the event!", Toast.LENGTH_SHORT).show();
        isUserSignedUp = false;
        signUpButton.setText("Sign Up");
    }
}