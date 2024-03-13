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
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.example.genzgpt.View.SpacingItemDecoration;
import com.example.genzgpt.Model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Fragment representing a list of attendees for a specific event.
 * It displays a list of attendees fetched from Firestore and allows navigation back to the previous screen.
 */
public class RegisteredListFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> registeredList;
    private Firebase firebase;
    private Event event;

    /**
     * Constructs a new instance of AttendeeListFragment with a specific event.
     * @param event The event for which attendees will be listed.
     */
    public RegisteredListFragment(Event event){
        this.event = event;
    };

    /**
     * Inflates the fragment's view and initializes its components, such as the RecyclerView for displaying attendees.
     * Also sets up a click listener for the back arrow ImageView to handle navigation.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registered_list_fragment, container, false);

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.registeredRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        registeredList = new ArrayList<>();
        userAdapter = new UserAdapter(registeredList);
        recyclerView.setAdapter(userAdapter);

        firebase = new Firebase();

        // Fetch the list of attendees from Firestore and update the RecyclerView
        fetchRegisteredAttendees(event.getEventName());

        ImageView backArrowImageView = view.findViewById(R.id.backArrowImageView);
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on back arrow click, typically go back
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    /**
     * Fetches the list of attendees who have checked in for the event from Firestore and updates the UI accordingly.
     * @param eventName The name of the event to fetch attendees for.
     */
    private void fetchRegisteredAttendees(String eventName) {
        registeredList.clear();
        firebase.fetchRegisteredAttendees(eventName, new Firebase.OnRegisteredAttendeesLoadedListener() {
            @Override
            public void onRegisteredAttendeesLoaded(List<User> loadedAttendees) {
                registeredList.clear();
                registeredList.addAll(loadedAttendees);
                userAdapter.notifyDataSetChanged();
                updateTotalCount();
            }

            @Override
            public void onRegisteredAttendeesLoadFailed(Exception e) {
                // Handle the error case
                Log.e("RegisteredListFragment", "Failed to load users: " + e.getMessage());
            }
        });
    }

    /**
     * Updates the displayed total count of attendees in the UI.
     */
    private void updateTotalCount() {
        if (getView() != null) {
            TextView tvTotalCount = getView().findViewById(R.id.number_of_attendees);
            // Correctly convert the size to a String
            tvTotalCount.setText(String.valueOf(registeredList.size()));
        }
    }
    /**
     * Adapter class for managing the display of attendees in a RecyclerView.
     */
    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private List<User> registered;

        public UserAdapter(List<User> attendees) {
            this.registered = attendees;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendee, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = registered.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return registered.size();
        }

        public void setUsers(List<User> newUsers) {
            this.registered.clear();
            this.registered.addAll(newUsers);
            notifyDataSetChanged();
        }

        public List<User> getUsers() {
            return registered;
        }
    }

    /**
     * ViewHolder class for displaying individual attendee items in the RecyclerView.
     */
    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView personName;
        //private TextView checkInCount;

        public UserViewHolder(View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.tvPersonName);
            //checkInCount = itemView.findViewById(R.id.tvCheckedInCount);
        }

        public void bind(User user) {
            personName.setText(user.getFirstName()+" "+user.getLastName());
            //checkInCount.setText("Checked In: " + user.getCheckInCount());
        }
    }
}