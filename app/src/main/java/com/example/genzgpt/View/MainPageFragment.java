package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genzgpt.Controller.EventAdapter;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.EditProfileFragment;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.example.genzgpt.Model.Event;

/**
 * A simple {@link Fragment} subclass.
 * Serves as a display for the User's Main Page.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Firebase firebase;
    private TextView userName;
    private List<Event> events;

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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        firebase = new Firebase();

        userName = view.findViewById(R.id.greetingText);


        // Fetch user data as before
        firebase.getUserData("zachtest@gmail.com", new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                Date date = new Date();
                int hour = date.getHours();
                if (hour < 12) {
                    userName.setText("Good Morning, " + user.getFirstName() + "👋");
                } else if (hour < 18) {
                    userName.setText("Good Afternoon, " + user.getFirstName() + "👋");
                } else {
                    userName.setText("Good Evening, " + user.getFirstName() + "👋");
                }
//                firebase.fetchEvents(new Firebase.OnEventsLoadedListener() {
//                    @Override
//                    public void onEventsLoaded(List<Event> eventList) {
////                        events = eventList;
//                    }
//
//                    @Override
//                    public void onEventsLoadFailed(Exception e) {
//                        Log.e("MainPageFragment", "Error loading events: " + e.getMessage());
//                    }
//                });
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

        RecyclerView eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Event> events = Arrays.asList(
                new Event("1", "Event 1", new Date(), "Location 1", 10, "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.istockphoto.com%2Fphotos%2Fedmonton&psig=AOvVaw2OOB5CWrrOTeE46vwl3Nvp&ust=1710665932969000&source=images&cd=vfe&opi=89978449&ved=0CBYQjRxqFwoTCIilvKu1-IQDFQAAAAAdAAAAABAE"),
                new Event("2", "Event 2", new Date(), "Location 2", 20, "https://example.com/image.jpg"),
                new Event("3", "Event 3", new Date(), "Location 3", 30, "https://example.com/image.jpg")
        );
//        for (Event event : events) {
//            Log.d("MainPageFragment", "Event: hahs" + event.getEventName());
//        }
        EventAdapter adapter = new EventAdapter(events);
        eventsRecyclerView.setAdapter(adapter);


        return view;
    }
}