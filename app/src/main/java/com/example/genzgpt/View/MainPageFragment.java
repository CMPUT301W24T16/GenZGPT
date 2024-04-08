package com.example.genzgpt.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.genzgpt.Controller.EventAdapter;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.genzgpt.Model.AppUser;

/**
 * A fragment subclass serving as the main page display for the user.
 */
public class MainPageFragment extends Fragment implements EventAdapter.EventClickListener {
    @Override
    public void onEventClick(Event event) {
        EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
        switchFragment(eventInfoFragment, R.id.BaseFragment);
    }


    private Firebase firebase;
    private TextView userName;
    private List<Event> events = new ArrayList<>();
    private EventAdapter eventAdapter;
    private User currentUser;

    /**
     * Handles the creation of the MainPageFragment
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new Firebase();
        eventAdapter = new EventAdapter(events, new EventAdapter.OnSettingButtonClickListener() {
            @Override
            public void onSettingButtonClick(Event event) {
                Log.d("MainPageFragment", "Setting button clicked for event: " + event.getOrganizers());
            }
        }, this);
    }

    /**
     * Handles the creation of the View of the MainPageFragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * The View for the MainPageFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        userName = view.findViewById(R.id.greetingText);
        setUpRecyclerView(view);
        fetchEvents();
        fetchUserData();

        return view;
    }

    private void setUpRecyclerView(View view) {
        RecyclerView eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    /**
     * Switches the current fragment to the new fragment
     */
    protected void switchFragment(Fragment fragment, int idToReplace) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new fragment
        fragmentTransaction.replace(idToReplace, fragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    private void fetchEvents() {
        events.clear();
        String currentUserId = AppUser.getUserId();
        firebase.fetchUserEvents(currentUserId, new Firebase.OnUserEventsLoadedListener() {

            @Override
            public void onEventsLoaded(String userId, List<Event> eventList) {
                events.clear();
                events.addAll(eventList);
                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onEventsLoadFailed(Exception e) {
                Log.e("EventListFragment", "Failed to load events: " + e.getMessage());
            }
        });
    }

    private void fetchUserData() {

        firebase.getUserData(AppUser.getUserId(), new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                greetUserBasedOnTime(user);
                }

            @Override
            public void onUserNotFound() {
                Log.d("MainPageFragment", "User not found");
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.e("MainPageFragment", "Error loading user data: " + e.getMessage());
            }
        });
    }

    private void greetUserBasedOnTime(User user) {
        Date date = new Date();
        int hour = date.getHours();
        String greeting;
        if (hour < 12) {
            greeting = "Good Morning, ";
        } else if (hour < 18) {
            greeting = "Good Afternoon, ";
        } else {
            greeting = "Good Evening, ";
        }
        userName.setText(greeting + user.getFirstName() + "👋");
    }
}
