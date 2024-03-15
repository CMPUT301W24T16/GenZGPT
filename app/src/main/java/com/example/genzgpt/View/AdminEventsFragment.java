package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment where an administrator can access all events (unfinished)
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminEventsFragment extends EventsFragment {

    /**
     * Required empty constructor for AdminEventsFragment
     */
    public AdminEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminEventsFragment newInstance(String param1, String param2) {
        AdminEventsFragment fragment = new AdminEventsFragment();
        return fragment;
    }

    /**
     * Handles the programmatic creation of the AdminEventsFragment.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Deletes the image associated with a particular event
     * @param event
     * The event whose image is being deleted.
     */
    private void deleteEventImage(Event event) {
        // Call deleteImage to delete the associated image
        firebase.deleteEventImage(event.getEventId(), event.getImageURL());

        // Remove the event from the RecyclerView
        int position = eventAdapter.getEvents().indexOf(event);
        if (position != -1) {
            eventAdapter.getEvents().remove(position);
            eventAdapter.notifyItemRemoved(position);
        }
    }

    /**
     * Deletes the event from Firestore and updates the RecyclerView
     */
    private void deleteEvent(Event event) {
        firebase.deleteEvent(event.getEventName());
        int position = eventAdapter.getEvents().indexOf(event);
        if (position != -1) {
            eventAdapter.getEvents().remove(position);
            eventAdapter.notifyItemRemoved(position);
        }
    }

    /**
     * Shows a dialog to confirm the deletion of the event
     */
    private void showDeleteEventDialog(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteEvent(event);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    /**
     * Fetches all events from Firebase and updates the RecyclerView
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

    protected class EventViewHolder extends EventsFragment.EventViewHolder {
        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventstart = itemView.findViewById(R.id.event_start);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventImage = itemView.findViewById(R.id.imageView); // Add ImageView for the image
        }

        @Override
        public void bind(Event event) {
            eventName.setText(event.getEventName());
            eventstart.setText(event.getEventDate().toString());
            eventLocation.setText(event.getLocation());

            // Load the image using Picasso
            if (event.getImageURL() != null && !event.getImageURL().isEmpty()) {
                Picasso.get()
                        .load(event.getImageURL())
//                        .resize(200, 200) // Specify the desired width and height
                        .into(eventImage);
            }

            itemView.setOnClickListener(v -> {
                // Context for creating AlertDialog
                final Context context = v.getContext();

                // Options for the user to select
                final CharSequence[] options = {"Registered Attendees", "Attendees", "Event Info", "Cancel"};

                // Creating AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose an option");

                // Setting the options
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Registered Attendees")) {
                        // Navigate to RegisteredAttendeesFragment
                        RegisteredListFragment registeredAttendeesFragment = new RegisteredListFragment(event);
                        switchFragment(context, registeredAttendeesFragment, R.id.BaseAdminFragment);
                    } else if (options[item].equals("Attendees")) {
                        // Navigate to AttendeeListFragment
                        AttendeeListFragment attendeeListFragment = new AttendeeListFragment(event);
                        switchFragment(context, attendeeListFragment, R.id.BaseAdminFragment);
                    } else if (options[item].equals("Event Info")) {
                        // Navigate to AttendeeListFragment
                        EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
                        switchFragment(context, eventInfoFragment, R.id.BaseAdminFragment);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });

                itemView.setOnLongClickListener(vv -> {
                    showDeleteEventDialog(event);
                    return true;
                });

                // Showing the AlertDialog
                builder.show();
            });
        }
    }
}