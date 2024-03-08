package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private Firebase firebase;

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

    private void fetchEvents() {
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

    private class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event event = events.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        public void setEvents(List<Event> newEvents) {
            this.events.clear();
            this.events.addAll(newEvents);
            notifyDataSetChanged();
        }

        public List<Event> getEvents() {
            return events;
        }
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView eventstart;
        private TextView eventLocation;
        private ImageView eventImage;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventstart = itemView.findViewById(R.id.event_start);
            eventLocation = itemView.findViewById(R.id.event_location);
            eventImage = itemView.findViewById(R.id.imageView); // Add ImageView for the image
        }

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
                showDeleteEventDialog(event);
            });
        }
    }

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

    private void deleteEvent(Event event) {
        firebase.deleteEvent(event.getEventName());
        int position = eventAdapter.getEvents().indexOf(event);
        if (position != -1) {
            eventAdapter.getEvents().remove(position);
            eventAdapter.notifyItemRemoved(position);
        }
    }
}
