package com.example.genzgpt.View;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Model.Event;

public class EventInfoFragment extends Fragment {
    private Event event;
    public void setEvent(Event event) {
        // Set the event details in the fragment
        this.event = event;
    }
}
