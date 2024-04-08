package com.example.genzgpt.Controller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * An adapter made so that users of the app can view events.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> eventList;
    private OnSettingButtonClickListener settingButtonClickListener;
    private EventClickListener eventClickListener;

    /**
     * A listener for what to do when someone clicks on an event.
     */
    public interface EventClickListener {
        void onEventClick(Event event);
    }

    /**
     * A listener for handling what to do when someone clicks on the settings button.
     */
    public interface OnSettingButtonClickListener {
        void onSettingButtonClick(Event event);
    }

    /**
     * The constructor for the eventAdapter.
     * @param eventList
     * The list of events to be shown in the app.
     * @param settingButtonClickListener
     * The click listener for the settings button.
     * @param eventClickListener
     * The click listener for when you click on an event.
     */
    public EventAdapter(List<Event> eventList, OnSettingButtonClickListener settingButtonClickListener, EventClickListener eventClickListener) {
        this.eventList = eventList;
        this.settingButtonClickListener = settingButtonClickListener;
        this.eventClickListener = eventClickListener;
    }

    /**
     * Handles the creation of the view holder for the event adapter.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     * The inflated version of the EventAdapter's view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Handles binding an event to a view holder.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    /**
     * A getter for the number of items within the EventAdapter.
     * @return
     * The number of items in the eventList being used by EventAdapter.
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    /**
     * The view holder to be used with this EventAdapter.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventLocation, eventDate, eventIndicator, hostName;
        ImageView eventImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventIndicator = itemView.findViewById(R.id.eventIndicator);
            hostName = itemView.findViewById(R.id.attendeeOrHostName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && eventClickListener != null) {
                    eventClickListener.onEventClick(eventList.get(position));
                }
            });
        }

        /**
         * Binds an event to an EventAdapter.
         * @param event
         * The event to bind.
         */
        void bind(Event event) {
            eventName.setText(event.getEventName());
            eventLocation.setText(event.getLocation());
            eventDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(event.getEventDate()));

            if (event.getImageURL() != null && !event.getImageURL().isEmpty()) {
                Picasso.get().load(event.getImageURL()).placeholder(R.drawable.ic_launcher_background).into(eventImage);
            }
            resetOrganizerDetails();
            updateOrganizerDetails(event);
        }

        private void resetOrganizerDetails() {
            // Reset the UI components to their default state
            eventIndicator.setVisibility(View.GONE); // Assume not the organizer by default
            hostName.setText(""); // Clear previous organizer name
        }

        private void updateOrganizerDetails(Event event) {
            String currentUserId = AppUser.getUserId();
            boolean isOrganizer = event.getOrganizers().contains(currentUserId);
            if (isOrganizer) {
                eventIndicator.setVisibility(View.VISIBLE);
                hostName.setText("You");
            } else {
                handleNonOrganizerScenario(event);
            }
        }

        private void handleNonOrganizerScenario(Event event) {
            if (!event.getOrganizers().isEmpty()) {
                String organizerId = event.getOrganizers().get(0);
                Firebase firebase = new Firebase();
                firebase.getUserData(organizerId, new Firebase.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User user) {
                        // Ensure the ViewHolder is still handling the same event
                        if (event.equals(eventList.get(getAdapterPosition()))) {
                            String organizerName = user.getFirstName() + " " + user.getLastName();
                            hostName.setText(organizerName);
                        }
                    }

                    @Override
                    public void onUserNotFound() {
                        hostName.setText("Unknown Organizer");
                    }

                    @Override
                    public void onUserLoadFailed(Exception e) {
                        hostName.setText("Error loading name");
                    }
                });
            } else {
                hostName.setText("No Organizers");
            }
        }
    }

}
