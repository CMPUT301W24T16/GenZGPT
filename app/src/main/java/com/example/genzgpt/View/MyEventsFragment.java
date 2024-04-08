package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 */
public class MyEventsFragment extends EventsFragment {
    private Firebase firebase;

    /**
     * Handles the creation of the View for MyEventsFragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     *  any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     *  UI should be attached to.  The fragment should not add the view itself,
     *  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *  from a previous saved state as given here.
     *
     * @return
     * The View for MyEventsFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list_fragment, container, false);

        firebase = new Firebase();
        // Change the name of the page to My Events
        TextView pageName = view.findViewById(R.id.allEventsTitle);
        pageName.setText(R.string.my_events);

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EditText searchEditText = view.findViewById(R.id.searchEditText);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the events as the user types
                filterEvents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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

    /**
     * An empty constructor for MyEventsFragment.
     */
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
        return new MyEventsFragment();
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
            public void onEventsLoaded(List<Event> events) {
                eventList.clear();
                eventList.addAll(events);
                // Also update the originalEventList with the fetched events
                originalEventList.clear();
                originalEventList.addAll(events);
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
    @Override
    protected boolean shouldShowEditEventOption(){return true;}
    @Override
    protected boolean shouldShowCheckInQrCodeOption() {return true;}
    @Override
    protected boolean shouldShowRegisteredAttendeesOption() {
        return true;
    }
    @Override
    protected boolean shouldShowCheckedInAttendeesOption() {
        return true;
    }
    @Override
    protected boolean shouldShowMapViewOption(){return true;}
}