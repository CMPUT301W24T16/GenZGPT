package com.example.genzgpt.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genzgpt.FirstSignInActivity;
import com.example.genzgpt.R;

/**
 * The login page for an Admin.
 * A simple {@link Fragment} subclass.
 */
public class AdminLoginFragment extends Fragment {
    EditText adminPassword;
    Button loginButton;
    Button returnButton;
    View view;
    private final String correctPassword = "pomegranate";

    /**
     * The empty constructor for the AdminLoginFragment
     */
    public AdminLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Handles creation of AdminLoginFragment programmatically.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Handles visual creation of the AdminLoginFragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * The finished version of the view for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        adminPassword = view.findViewById(R.id.admin_password);
        loginButton = view.findViewById(R.id.admin_signin_button);
        returnButton = view.findViewById(R.id.return_button);

        loginButton.setOnClickListener(v -> {
            String password = adminPassword.getText().toString().trim();
            if (password.equals(correctPassword)) {
                SharedPreferences preferences = requireActivity().getSharedPreferences(
                        "com.example.genzgpt", Context.MODE_PRIVATE);

                preferences.edit().putBoolean("admin", true).apply();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
            else {
                Toast.makeText(getContext(), "Invalid Admin Password", Toast.LENGTH_SHORT);
            }
        });

        returnButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    public static AdminLoginFragment newInstance() {return new AdminLoginFragment();}
}