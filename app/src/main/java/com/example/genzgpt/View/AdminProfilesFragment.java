package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.genzgpt.R;

/**
 * The profile that will manage all profiles for the Admin to view.
 * A simple {@link Fragment} subclass.
 */
public class AdminProfilesFragment extends Fragment {

    /**
     * The required empty constructor for AdminProfilesFragment.
     */
    public AdminProfilesFragment() {
        // Required empty public constructor
    }

    /**
     * Handles creation of an instance of AdminProfilesFragment.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Handles visual side of creating AdminProfilesFragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * The View for the AdminProfilesFragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profiles, container, false);
    }
}