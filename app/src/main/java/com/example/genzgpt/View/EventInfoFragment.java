package com.example.genzgpt.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventInfoFragment extends Fragment {

    private Event event;
    private TextView eventNameTextView, eventDateTextView, eventLocationTextView, eventAttendeesTextView;
    private ImageView eventImageView;
    private Firebase firebase;

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
                // Load the image using Picasso and resize it to fit into a specific size
                Picasso.get()
                        .load(event.getImageURL())
                        .resize(800, 800) // Specify the desired dimensions
                        .centerCrop() // Crop the image from the center if necessary
                        .into(eventImageView);
            }
        }
    }
}