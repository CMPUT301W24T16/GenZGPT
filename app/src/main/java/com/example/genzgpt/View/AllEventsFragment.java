package com.example.genzgpt.View;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Serves as a Display for the User's Events List
 */
public class AllEventsFragment extends EventsFragment {
    public AllEventsFragment() {
        // required empty constructor
    }

    /**
     * Fetches all events from Firestore and updates the RecyclerView
     */
    protected void fetchEvents() {
        firebase.fetchEvents(new Firebase.OnEventsLoadedListener() {
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
                // Handle the error case
                Log.e("EventListFragment", "Failed to load events: " + e.getMessage());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
