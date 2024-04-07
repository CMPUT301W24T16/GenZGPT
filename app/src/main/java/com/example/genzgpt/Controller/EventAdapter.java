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
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> eventList;
    private OnSettingButtonClickListener settingButtonClickListener;
    private EventClickListener eventClickListener;

    public interface EventClickListener {
        void onEventClick(Event event);
    }

    public interface OnSettingButtonClickListener {
        void onSettingButtonClick(Event event);
    }

    public EventAdapter(List<Event> eventList, OnSettingButtonClickListener settingButtonClickListener, EventClickListener eventClickListener) {
        this.eventList = eventList;
        this.settingButtonClickListener = settingButtonClickListener;
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

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
