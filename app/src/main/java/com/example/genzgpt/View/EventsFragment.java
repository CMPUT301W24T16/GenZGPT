package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * The basis for any fragment that handles showing a collection of events.
 */
abstract class EventsFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected EventAdapter eventAdapter;
    protected List<Event> eventList;
    protected Firebase firebase;

    // All EventsFragment subclasses will fetchEvents, but which they fetch will differ.
    abstract protected void fetchEvents();

    /**
     * Allows for an Event to be handled by the RecyclerView in an EventsFragment
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
         * Updates the list of events
         */
        public void setEvents(List<Event> newEvents) {
            this.events.clear();
            this.events.addAll(newEvents);
            notifyDataSetChanged();
        }

        /**
         * Returns the list of events
         */
        public List<Event> getEvents() {
            return events;
        }
    }

    /**
     * View holder for an Event
     */
    protected class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView eventstart;
        private TextView eventLocation;
        private ImageView eventImage;

        /**
         * Constructor for the EventViewHolder
         */
        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventstart = itemView.findViewById(R.id.event_start);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventImage = itemView.findViewById(R.id.imageView); // Add ImageView for the image
        }

        /**
         * Binds the event data to the view holder
         */
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
                final CharSequence[] options = {"Registered Attendees", "Attendees", "Event Info","Cancel"};

                // Creating AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose an option");

                // Setting the options
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Registered Attendees")) {
                        // Navigate to RegisteredAttendeesFragment
                        RegisteredListFragment registeredAttendeesFragment = new RegisteredListFragment(event);
                        switchFragment(context, registeredAttendeesFragment);
                    } else if (options[item].equals("Attendees")) {
                        // Navigate to AttendeeListFragment
                        AttendeeListFragment attendeeListFragment = new AttendeeListFragment(event);
                        switchFragment(context, attendeeListFragment);
                    } else if (options[item].equals("Event Info")) {
                        // Navigate to AttendeeListFragment
                        EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
                        switchFragment(context, eventInfoFragment);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });

                // Showing the AlertDialog
                builder.show();
            });

            itemView.setOnLongClickListener(v -> {
                showDeleteEventDialog(event);
                return true;
            });
        }


        // FIXME: BaseFragment bad
        // FIXME: Make this abstract?
        private void switchFragment(Context context, Fragment fragment) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current fragment with the new fragment
            fragmentTransaction.replace(R.id.BaseFragment, fragment);
            fragmentTransaction.addToBackStack(null);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

    // FIXME Not all EventsFragments should have this
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

    // FIXME Not all EventsFragments should have this
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

    // FIXME Not all EventsFragments Should have this

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
}
