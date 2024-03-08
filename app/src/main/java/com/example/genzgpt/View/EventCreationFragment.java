package com.example.genzgpt.View;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.genzgpt.Controller.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class EventCreationFragment extends Fragment {

    private EditText eventNameEditText, eventDateEditText, locationEditText;
    private ImageView eventImageView;
    private Button selectImageButton, createEventButton;
    private FirebaseFirestore db;
    private Calendar eventDateCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_creation_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        eventDateCalendar = Calendar.getInstance();

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        eventNameEditText = view.findViewById(R.id.eventNameEditText);
        eventDateEditText = view.findViewById(R.id.eventDateEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        eventImageView = view.findViewById(R.id.eventImageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        createEventButton = view.findViewById(R.id.createEventButton);

        eventDateEditText.setOnClickListener(v -> showDatePickerDialog());
        createEventButton.setOnClickListener(v -> createEvent());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        });
    }
    private void showDatePickerDialog() {
        int year = eventDateCalendar.get(Calendar.YEAR);
        int month = eventDateCalendar.get(Calendar.MONTH);
        int day = eventDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            eventDateCalendar.set(Calendar.YEAR, year1);
            eventDateCalendar.set(Calendar.MONTH, monthOfYear);
            eventDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        eventDateEditText.setText(sdf.format(eventDateCalendar.getTime()));
    }

    private void createEvent() {
        // Example user, replace with actual user data
        User organizer = new User("123123", "Johb", "Doe", 123123123, "bob@gmail.com", false);

        String eventName = eventNameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        if (TextUtils.isEmpty(eventName) || eventDateCalendar == null || TextUtils.isEmpty(location)) {
            return;
        }

        Event newEvent = new Event(
                "",
                eventName,
                eventDateCalendar.getTime(),
                location,
                100,
                "https://example.com/image.png"
        );

        Firebase firebase = new Firebase();
        firebase.createEvent(newEvent, organizer);

        getParentFragmentManager().popBackStack();

    }



}


