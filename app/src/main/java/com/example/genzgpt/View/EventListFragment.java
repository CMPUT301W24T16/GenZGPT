package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Serves as a Display for the User's Events List
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends EventsFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventListFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list_fragment, container, false);

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.eventsRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        firebase = new Firebase();

        Button createEventButton = view.findViewById(R.id.addEventButton);

        createEventButton.setOnClickListener(v -> {
            // Navigate to EventCreationFragment
            EventCreationFragment eventCreationFragment = new EventCreationFragment();
            switchFragment(eventCreationFragment);
        });

        // Fetch the list of events from Firestore and update the RecyclerView
        fetchEvents();

        return view;
    }

    // FIXME Seems like an interface for many different fragments and can be Fragment fragment
    /**
     * Switches the current fragment to the new fragment
     */
    private void switchFragment(EventCreationFragment eventCreationFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new fragment
        fragmentTransaction.replace(R.id.BaseFragment, eventCreationFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    /**
     * Fetches the list of events from Firestore and updates the RecyclerView
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

}
