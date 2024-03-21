package com.example.genzgpt.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * A simple {@link Fragment} subclass.
 * Serves as a display for the User's Main Page.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment implements EventAdapter.EventClickListener {
    @Override
    public void onEventClick(Event event) {
        EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
        switchFragment(eventInfoFragment, R.id.BaseFragment);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firebase = new Firebase();
        eventAdapter = new EventAdapter(events, new EventAdapter.OnSettingButtonClickListener() {
            @Override
            public void onSettingButtonClick(Event event) {
                Log.d("MainPageFragment", "Setting button clicked for event: " + event.getEventName());
            }
        }, this); // 'this' refers to MainPageFragment which now implements EventClickListener
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
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
        firebase.fetchEvents(new Firebase.OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(List<Event> loadedEvents) {
                events.clear();
                events.addAll(loadedEvents);
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEventsLoadFailed(Exception e) {
                Log.e("EventListFragment", "Failed to load events: " + e.getMessage());
            }
        });
    }

    private void fetchUserData() {

        firebase.getUserData(AppUser.getAppUserEmail(), new Firebase.OnUserLoadedListener() {
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
