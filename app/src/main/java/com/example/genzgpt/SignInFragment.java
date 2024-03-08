package com.example.genzgpt;

import static java.lang.Long.parseLong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;

public class SignInFragment extends Fragment {

    Button profileButton;
    Button adminButton;
    EditText profileFirstName;
    EditText profileLastName;
    EditText emailAddress;
    EditText phoneNumber;
    Spinner theme;
    Switch geolocation;
    AdminLoginFragment adminLogin = new AdminLoginFragment();
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminButton = view.findViewById(R.id.admin_button);
        profileButton = view.findViewById(R.id.user_profile_button);

        profileFirstName = view.findViewById(R.id.first_name_fill);
        profileLastName = view.findViewById(R.id.last_name_fill);

        emailAddress = view.findViewById(R.id.email_fill);
        phoneNumber = view.findViewById(R.id.edit_phone);

        theme = view.findViewById(R.id.theme_spinner);
        geolocation = view.findViewById(R.id.geolocation_switch);

        profileButton.setOnClickListener( v -> {
            // Get the information for a user profile.
            String firstName = profileFirstName.getText().toString().trim();
            if (firstName.isEmpty()) {
                firstName = "Firstname";
            }

            String lastName = profileLastName.getText().toString().trim();
            if (lastName.isEmpty()) {
                lastName = "lastname";
            }

            String email = emailAddress.getText().toString().trim();
            if (email.isEmpty()) {
                email = "userperson@mailsite.com";
            }

            long phone = parseLong(phoneNumber.getText().toString());
            String currentTheme = theme.toString();
            boolean geo = geolocation.isActivated();

            User newUser = new User(firstName, lastName, phone, email, geo);

            Firebase firebase = new Firebase();
            firebase.createUser(newUser);

            AppUser user = new AppUser(firstName, lastName, phone, email, geo);
        });

        // Set the adminButton to send to the admin sign in page.
        adminButton.setOnClickListener( v -> {
            //FIXME need to send to AdminLoginFragment
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        return view;
    }
}
