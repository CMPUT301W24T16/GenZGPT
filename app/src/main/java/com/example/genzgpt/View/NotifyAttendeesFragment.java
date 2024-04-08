package com.example.genzgpt.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.FirebaseMessages;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyAttendeesFragment extends Fragment {
    private Event event;
    private EditText messageEditText;
    private Button sendButton;
    private Firebase firebase;
    private FirebaseMessages firebaseMessages;

    public NotifyAttendeesFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify_attendees, container, false);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        firebase = new Firebase();
        firebaseMessages = new FirebaseMessages(getContext());

        sendButton.setOnClickListener(v -> fetchAttendeesAndSendMessage());

        return view;
    }

    private void fetchAttendeesAndSendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a message to send.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebase.fetchCheckedInAttendees(event.getEventId(), new Firebase.OnCheckInAttendeesLoadedListener() {
            @Override
            public void onCheckInAttendeesLoaded(List<User> checkedInAttendees) {
                List<String> deviceTokens = new ArrayList<>();
                for (User attendee : checkedInAttendees) {
                    String token = firebaseMessages.getStoredDeviceToken();
                    if (token != null) {
                        deviceTokens.add(token);
                    }
                }
                // After collecting tokens, send message to all
                if (!deviceTokens.isEmpty()) {
                    sendMessageToAttendees(deviceTokens, "Event Notification", message, "message");
                } else {
                    Toast.makeText(getContext(), "No attendees to send message to.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCheckInAttendeesLoadFailed(Exception e) {
                Toast.makeText(getContext(), "Failed to fetch attendees.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessageToAttendees(List<String> deviceTokens, String title, String content, String type) {
        // Using the updated sendMessageToMultipleDevices method
        firebaseMessages.sendMessageToMultipleDevices(deviceTokens, title, content, type);
        Toast.makeText(getContext(), "Message sent to attendees.", Toast.LENGTH_SHORT).show();
    }
}