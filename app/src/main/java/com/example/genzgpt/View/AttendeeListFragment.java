package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.example.genzgpt.View.SpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendeeListFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> attendeeList;
    private Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendee_list_fragment, container, false);

        int spacingInPixels = 16; // Adjust the spacing as needed
        recyclerView = view.findViewById(R.id.attendeesRecyclerView);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        attendeeList = new ArrayList<>();
        userAdapter = new UserAdapter(attendeeList);
        recyclerView.setAdapter(userAdapter);

        firebase = new Firebase();

        // Fetch the list of users from Firebase and update the RecyclerView
        fetchAttendees();

        return view;
    }

    private void fetchAttendees() {
        attendeeList.clear();
        firebase.fetchAttendees(new Firebase.OnCheckInAttendeesLoadedListener() {
            @Override
            public void onUsersLoaded(List<User> loadedAttendees) {
                attendeeList.clear();
                attendeeList.addAll(loadedAttendees);
                userAdapter.notifyDataSetChanged();
                updateTotalCount();
            }

            @Override
            public void onUsersLoadFailed(Exception e) {
                // Handle the error case
                Log.e("AttendeeListFragment", "Failed to load users: " + e.getMessage());
            }
        });
    }

    private void updateTotalCount() {
        if (getView() != null) {
            TextView tvTotalCount = getView().findViewById(R.id.number_of_attendees);
            tvTotalCount.setText(attendeeList.size());
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private List<User> attendees;

        public UserAdapter(List<User> attendees) {
            this.attendees = attendees;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendee, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = attendees.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return attendees.size();
        }

        public void setUsers(List<User> newUsers) {
            this.attendees.clear();
            this.attendees.addAll(newUsers);
            notifyDataSetChanged();
        }

        public List<User> getUsers() {
            return attendees;
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView personName;
        private TextView checkInCount;

        public UserViewHolder(View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.tvPersonName);
            checkInCount = itemView.findViewById(R.id.tvCheckedInCount);
        }

        public void bind(User user) {
            personName.setText(user.getFirstName()+user.getLastName());
            checkInCount.setText("Checked In: " + user.getCheckInCount());
        }
    }
}