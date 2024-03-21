package com.example.genzgpt.Controller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private static List<Event> eventList;
    private OnSettingButtonClickListener settingButtonClickListener;

    public EventAdapter(List<Event> eventList, OnSettingButtonClickListener settingButtonClickListener) {
        this.eventList = eventList;
        this.settingButtonClickListener = settingButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new ViewHolder(view, settingButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventLocation.setText(event.getLocation());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = dateFormat.format(event.getEventDate());
        holder.eventDate.setText(dateString);
        if (event.getImageURL() != null && !event.getImageURL().isEmpty()) {
            Picasso.get()
                    .load(event.getImageURL())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.eventImage);
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public interface OnSettingButtonClickListener {
        void onSettingButtonClick(Event event);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventLocation, eventDate;
        ImageView eventImage;
        ImageButton settingButton;

        public ViewHolder(@NonNull View itemView, final OnSettingButtonClickListener listener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventImage = itemView.findViewById(R.id.eventImage);
            settingButton = itemView.findViewById(R.id.settingBotton);

            settingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onSettingButtonClick(eventList.get(position));
                        System.out.println("Setting button clicked for event: " + eventList.get(position).getEventName());
                    }
                }
            });
        }
    }
}
