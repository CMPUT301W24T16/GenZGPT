package com.example.genzgpt;

import static java.lang.Long.parseLong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.View.AdminLoginFragment;

public class FirstSignInActivity extends AppCompatActivity {
    Button profileButton;
    Button adminButton;
    EditText profileFirstName;
    EditText profileLastName;
    EditText emailAddress;
    EditText phoneNumber;
    Spinner theme;
    Switch geolocation;
    AdminLoginFragment adminSignIn = new AdminLoginFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sign_in);

        adminButton = findViewById(R.id.admin_button);
        profileButton = findViewById(R.id.user_profile_button);

        profileFirstName = findViewById(R.id.first_name_fill);
        profileLastName = findViewById(R.id.last_name_fill);

        emailAddress = findViewById(R.id.email_fill);
        phoneNumber = findViewById(R.id.edit_phone);

        theme = findViewById(R.id.theme_spinner);
        geolocation = findViewById(R.id.geolocation_switch);

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

            String imageURL = null;

            User newUser = new User(firstName, lastName, phone, email, geo, imageURL);

            Firebase firebase = new Firebase();
            firebase.createUser(newUser);

            // Set the static value of UserEmail to the provided email address
            AppUser.setUserEmail(email);

            // notify the Main Activity that a successful sign in has occurred
            MainActivity.hasSignedIn = true;
            finish();
        });

        // Set the adminButton to send to the admin sign in page.
        adminButton.setOnClickListener( v -> {
            Intent toAdmin = new Intent(FirstSignInActivity.this, AdminActivity.class);
            startActivity(toAdmin);
        });
    }
}