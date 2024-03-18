package com.example.genzgpt.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;
import com.example.genzgpt.Model.AppUser;

public class EventInfoFragment extends Fragment {

    private Event event;
    private TextView eventNameTextView, eventDateTextView, eventLocationTextView, eventAttendeesTextView;
    private ImageView eventImageView;
    private Firebase firebase;
    private Button signUpButton;
    private boolean isUserSignedUp = false;

    // Constructor now takes an Event object
    public EventInfoFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.event_info_fragment, container, false);
        initializeViews(view);
        displayEventData();
        firebase = new Firebase();
        // if a user clicks on sign_up_button, call signUpForEvent
        view.findViewById(R.id.sign_up_button).setOnClickListener(this::signUpForEvent);
        return view;
    }

    private void initializeViews(View view) {
        eventNameTextView = view.findViewById(R.id.event_name);
        eventDateTextView = view.findViewById(R.id.event_date);
        eventLocationTextView = view.findViewById(R.id.event_location);
        eventImageView = view.findViewById(R.id.event_poster);
    }

    private void displayEventData() {
        // Directly display event data using the provided Event object
        if (event != null) {
            eventNameTextView.setText(event.getEventName());
            // You may want to format the date
            eventDateTextView.setText(event.getEventDate().toString());
            eventLocationTextView.setText(event.getLocation());

            if (event.getImageURL() != null && !event.getImageURL().isEmpty()) {
                // Load the image using Picasso or another library
                Picasso.get().load(event.getImageURL()).into(eventImageView);
            }
        }
    }

    public void signUpForEvent(View view) {
        fetchUserData("zachtest@gmail.com");
    }

    private void fetchUserData(String email) {
        firebase.getUserData(email, new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                registerUserForEvent(user);
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
            }

            @Override
            public void onAttendeeRegistrationFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to sign up for the event!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventNotFound() {
                Toast.makeText(getContext(), "Event not found!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventLoadFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to load event data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}