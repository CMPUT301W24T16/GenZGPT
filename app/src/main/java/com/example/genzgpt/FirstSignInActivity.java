package com.example.genzgpt;

import static com.example.genzgpt.Controller.GeolocationTracking.RequestCode.FINE_LOCATION_PERMISSION;
import static java.lang.Character.isLetter;
import static java.lang.Long.parseLong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.GeolocationTracking;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.View.AdminLoginFragment;
import com.example.genzgpt.Controller.GeolocationTracking;

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
    private boolean isValidSignIn = false;

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
            String lastName = profileLastName.getText().toString().trim();
            String email = emailAddress.getText().toString().trim();
            String phoneStr = phoneNumber.getText().toString();

            String currentTheme = theme.toString();
            geolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && (!checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) && (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))){
                        requestPermission();
                    }
                }
            });

            boolean geo = geolocation.isActivated();

            // FIXME Put procedural generation here
            String imageURL = null;

            boolean isValidFirst = isValidName(firstName);
            boolean isValidLast = isValidName(lastName);
            boolean isValidPhone = isValidPhone(phoneStr);
            boolean isValidEmail = isValidEmail(email);

            // Ensure all the necessary sign in parameters have been met
            if (!isValidFirst) {
                Toast.makeText(this,
                        "Please ensure first name is filled out and only has letters.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!isValidLast) {
                Toast.makeText(this,
                        "Please ensure last name is filled out and only has letters.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!isValidEmail) {
                Toast.makeText(this,
                        "Please ensure email is filled out and is a valid email address.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!isValidPhone) {
                Toast.makeText(this,
                        "Please ensure phone number is correct number of digits.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                User newUser = new User(firstName, lastName, parseLong(phoneStr), email, geo,
                        imageURL);

                Firebase firebase = new Firebase();
                firebase.createUser(newUser, new Firebase.OnUserCreatedListener() {
                    @Override
                    public void onUserCreated(String userId) {
                        AppUser.setUserId(userId);
                        AppUser.setHasSignedIn(true);
                        Log.e("User Creation", "Successful User Creation");

                        // Save user details to SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("com.example.genzgpt", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("signIn", true);
                        editor.putString("id", userId);
                        editor.putString("firstName", newUser.getFirstName());
                        editor.putString("lastName", newUser.getLastName());
                        editor.putString("email", newUser.getEmail());
                        editor.putLong("phoneNumber", newUser.getPhone());
                        editor.putBoolean("geolocation", newUser.isGeolocation());
                        // If imageURL can be null, then we need to handle that case
                        editor.putString("imageURL", newUser.getImageURL() != null ? newUser.getImageURL() : "default_image_url");

                        editor.apply();
                        Intent toMain = new Intent(FirstSignInActivity.this, MainActivity.class);
                        startActivity(toMain);

                        finish();
                        System.out.println("ID: " + preferences.getString("id", null)  + " firstName testing 232: " + preferences.getString("email", null));

                    }

                    @Override
                    public void onEmailAlreadyExists() {
                        Toast.makeText(getApplicationContext(),
                                "The provided email already exists for another user." +
                                "Please use a different email.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserCreationFailed(Exception e) {
                        Log.e("User Creation", "The User was not Created", e);
                    }
                });
            }
        });


        // Set the adminButton to send to the admin sign in page.
        adminButton.setOnClickListener( v -> {
            Intent toAdmin = new Intent(FirstSignInActivity.this, AdminActivity.class);
            startActivity(toAdmin);
        });
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char letter = name.charAt(i);
            if (!isLetter(letter)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidPhone(String phone) {
        // FIXME May want to change number from 10
        return (phone.length() == 4 || phone.length() >= 10);
    }

    private boolean isValidEmail(String email) {
        // FIXME NEED TO GET JAVA EMAIL package
        return (!email.isEmpty());
    }

    /**
     *
     */
    public void requestPermission(){
        ActivityCompat.requestPermissions(FirstSignInActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GeolocationTracking.RequestCode.FINE_LOCATION_PERMISSION);
    }

    /**
     * This method will check if permissions are granted or denied
     * @param permission
     * @return a boolean value
     */
    public boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
}