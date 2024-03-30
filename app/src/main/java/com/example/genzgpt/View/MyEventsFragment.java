package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Serves as a Display for the User's Events List
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends EventsFragment {
    private TextView pageName;
    private Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list_fragment, container, false);

        firebase = new Firebase();
        // Change the name of the page to My Events
        pageName = view.findViewById(R.id.allEventsTitle);
        pageName.setText("My Events");

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        firebase = new Firebase();

        // The MyEventsFragment should have a createEventButton that is now made visible
        Button createEventButton = view.findViewById(R.id.addEventButton);
        createEventButton.setVisibility(View.VISIBLE);

        createEventButton.setOnClickListener(v -> {
            // Navigate to EventCreationFragment
            EventCreationFragment eventCreationFragment = new EventCreationFragment();
            switchFragment(eventCreationFragment, R.id.BaseFragment);
        });

        // Fetch the list of events from Firestore and update the RecyclerView
        fetchEvents();

        return view;
    }

    public MyEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyEventsFragment.
     */
    public static MyEventsFragment newInstance() {
        MyEventsFragment fragment = new MyEventsFragment();
        return fragment;
    }

    /**
     * Handles creation of an instance of MyEvents
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void fetchEvents() {
        // Assuming AppUser.getInstance().getId() returns the current user's ID
        String userId = AppUser.getUserId();

        firebase.fetchEventsForOrganizer(userId, new Firebase.OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(List<Event> eventList) {
                // Update the RecyclerView with the fetched events
                MyEventsFragment.this.eventList.clear();
                MyEventsFragment.this.eventList.addAll(eventList);
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEventsLoadFailed(Exception e) {
                // Handle the error, possibly by showing an error message to the user
                Log.e("MyEventsFragment", "Error loading events: " + e.getMessage());
                Toast.makeText(getContext(), "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}