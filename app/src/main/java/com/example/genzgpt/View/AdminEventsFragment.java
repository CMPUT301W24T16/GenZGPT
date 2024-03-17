package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    protected EventAdapter eventAdapter;

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
     * @return A new instance of fragment AdminEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminEventsFragment newInstance() {
        AdminEventsFragment fragment = new AdminEventsFragment();
        return fragment;
    }

    /**
     *  Handles the visual creation of the AdminEventsFragment.
     *  @param inflater The LayoutInflater object that can be used to inflate
     *  any views in the fragment,
     *  @param container If non-null, this is the parent view that the fragment's
     *  UI should be attached to.  The fragment should not add the view itself,
     *  but this can be used to generate the LayoutParams of the view.
     *  @param savedInstanceState If non-null, this fragment is being re-constructed
     *  from a previous saved state as given here.
     *
     * @return
     * A View for an EventsFragment
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

        // Fetch the list of events from Firestore and update the RecyclerView
        fetchEvents();

        return view;
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

    //FIXME CAN WE JUST REMOVE EVENTADAPTER AND EVENTVIEWHOLDER FROM BEING NESTED CLASSES TO MAKE
    // THIS EASIER?
    /**
     * Allows for an Event to be handled by the RecyclerView in an AdminEventsFragment
     */
    protected class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;

        /**
         * Constructor for the EventAdapter
         */
        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        /**
         * Creates a new view holder and inflates the view
         */
        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(itemView);
        }

        /**
         * Binds the event data to the view holder
         */
        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event event = events.get(position);
            holder.bind(event);
        }

        /**
         * Returns the number of items in the list
         */
        @Override
        public int getItemCount() {
            return events.size();
        }

        /**
         * Updates the list of events in the EventAdapter
         */
        public void setEvents(List<Event> newEvents) {
            this.events.clear();
            this.events.addAll(newEvents);
            notifyDataSetChanged();
        }

        /**
         * Returns the list of events in the EventAdapter
         */
        public List<Event> getEvents() {
            return events;
        }
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