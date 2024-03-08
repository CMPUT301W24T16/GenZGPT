package com.example.genzgpt;

import static java.lang.Long.parseLong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;

/**
 * The activity a user will be taken to if they do not have a profile created.
 */
public class FirstActivity extends AppCompatActivity {
    Button profileButton;
    Button adminButton;
    EditText profileFirstName;
    EditText profileLastName;
    EditText emailAddress;
    EditText phoneNumber;
    Spinner theme;
    Switch geolocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

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
            // FIXME: User has no id, yet I need an id for the user.
            User newUser = new User("0", firstName, lastName, phone, email, geo, imageURL);

            Firebase firebase = new Firebase();
            firebase.createUser(newUser);

            AppUser user = new AppUser("0",firstName, lastName, phone, email, geo, imageURL);


            finish();
        });

        adminButton.setOnClickListener( v -> {
            // Send to the admin confirm page
        });

    }
}