package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.GeolocationTracking;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The basis for any fragment that handles showing a collection of events.
 */
abstract class EventsFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected EventAdapter eventAdapter;
    protected List<Event> eventList;
    protected List<Event> originalEventList = new ArrayList<>();
    protected Firebase firebase;

    // All EventsFragment subclasses will fetchEvents, but which they fetch will differ.
    abstract protected void fetchEvents();

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
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);
        firebase = new Firebase();

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

        // Fetch the list of events from Firestore and update the RecyclerView
        fetchEvents();

        return view;
    }

    /**
     * Filters the event list based on a query and updates the RecyclerView.
     *
     * @param query The text to filter the event list by.
     */
    protected void filterEvents(String query) {
        List<Event> filteredList = new ArrayList<>();
        for (Event event : originalEventList) {
            if (event.getEventName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(event);
            }
        }

        // Update the RecyclerView with the filtered list
        eventAdapter.setEvents(filteredList);
    }

    /**
     * Switches the current fragment to the new fragment
     */
    protected void switchFragment(Fragment fragment, int idToReplace) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new fragment
        fragmentTransaction.replace(idToReplace, fragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    /**
     * Allows for an Event to be handled by the RecyclerView in an EventsFragment
     */
    protected class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private final List<Event> events;

        /**
         * Constructor for the EventAdapter
         */
        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        /**
         * Creates a new view holder and inflates the view
         */
        @NonNull
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

    /**
     * View holder for an Event
     */
    protected class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView eventName;
        protected TextView eventstart;
        protected TextView eventLocation;
        protected ImageView eventImage;

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
         * This Version is designed for MainActivity EventsFragments
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
                ArrayList<String> optionsList = new ArrayList<>(Arrays.asList("Event Info", "Cancel"));

                // Conditional option addition for MyEventsFragment
                if (shouldShowEditEventOption()) {
                    optionsList.add(1, "Edit Event");
                }
                // Conditional option addition for MyEventsFragment
                if (shouldShowCheckInQrCodeOption()) {
                    optionsList.add(2, "View Check-In QR Code");
                }
                // Conditional option addition for MyEventsFragment
                if (shouldShowRegisteredAttendeesOption()) {
                    optionsList.add(3, "Registered Attendees");
                }
                // Conditional option addition for MyEventsFragment
                if (shouldShowCheckedInAttendeesOption()) {
                    optionsList.add(4, "Attendees");
                }
                // Conditional option addition for MyEventsFragment
                if (shouldShowMapViewOption()) {
                    optionsList.add(5, "View Map");
                }
                // Conditional option addition for MyEventsFragment
                if (shouldShowSendMessageOption()) {
                    optionsList.add(5, "Send Message");
                }


                CharSequence[] options = optionsList.toArray(new CharSequence[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose an option");

                // Setting the options
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Registered Attendees")) {
                        // Navigate to RegisteredAttendeesFragment
                        RegisteredListFragment registeredAttendeesFragment = new RegisteredListFragment(event);
                        switchFragment(context, registeredAttendeesFragment, R.id.BaseFragment);
                    } else if (options[item].equals("Attendees")) {
                        // Navigate to AttendeeListFragment
                        AttendeeListFragment attendeeListFragment = new AttendeeListFragment(event);
                        switchFragment(context, attendeeListFragment, R.id.BaseFragment);
                    } else if (options[item].equals("Event Info")) {
                        // Navigate to AttendeeListFragment
                        EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
                        switchFragment(context, eventInfoFragment, R.id.BaseFragment);
                    } else if (options[item].equals("Edit Event")){
                        EditEventFragment editEventFragment = new EditEventFragment(event);
                        switchFragment(context, editEventFragment, R.id.BaseFragment);
                    } else if (options[item].equals("View Check-In QR Code")) {
                        CheckInQRCodeFragment checkInQRCodeFragment = new CheckInQRCodeFragment(event);
                        switchFragment(context, checkInQRCodeFragment, R.id.BaseFragment);
                    }else if (options[item].equals("View Map")){
                        GeolocationTracking geolocationTracking = new GeolocationTracking(event);
                        switchFragment(context, geolocationTracking, R.id.BaseFragment);
                    }
                    else if (options[item].equals("Send Message")){
                        NotifyAttendeesFragment notifyAttendeesFragment = new NotifyAttendeesFragment(event);
                        switchFragment(context, notifyAttendeesFragment, R.id.BaseFragment);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                // Showing the AlertDialog
                builder.show();
            });
        }

        /**
         * A re-usable switch between two different fragments.
         * @param context
         * The information needed to switch fragments from the app.
         * @param fragment
         * The fragment that will be switched to.
         * @param idToReplace
         * The id of the fragment being replaced in the switch.
         */
        protected void switchFragment(Context context, Fragment fragment, int idToReplace) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current fragment with the new fragment
            fragmentTransaction.replace(idToReplace, fragment);
            fragmentTransaction.addToBackStack(null);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }
    protected boolean shouldShowEditEventOption(){return false;}
    protected boolean shouldShowCheckInQrCodeOption() {
        return false;
    }
    protected boolean shouldShowRegisteredAttendeesOption() {
        return false;
    }
    protected boolean shouldShowCheckedInAttendeesOption() {
        return false;
    }
    protected boolean shouldShowMapViewOption(){return false;}
    protected boolean shouldShowSendMessageOption(){return false;}

}
