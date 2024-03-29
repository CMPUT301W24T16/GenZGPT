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
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllEventsFragment extends EventsFragment {
    public AllEventsFragment() {
        // required empty constructor
    }

    /**
     * Fetches all events from Firestore and updates the RecyclerView
     */
    protected void fetchEvents() {
        eventList.clear();
        firebase.fetchEvents(new Firebase.OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(List<Event> loadedEvents) {
                eventList.clear();
                eventList.addAll(loadedEvents);
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
