package com.example.genzgpt;

import static com.example.genzgpt.Controller.GeolocationTracking.FINE_LOCATION_PERMISSION;
import static java.lang.Character.isLetter;
import static java.lang.Long.parseLong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import com.example.genzgpt.Controller.CameraHandler;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.FirebaseMessages;
import com.example.genzgpt.Controller.GeolocationTracking;
import com.example.genzgpt.Controller.ProfileGenerator;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.View.AdminLoginFragment;

/**
 * The first activity a user will see when they need to Sign up for the app.
 */
public class FirstSignInActivity extends AppCompatActivity {
    Button profileButton;
    Button adminButton;
    EditText profileFirstName;
    EditText profileLastName;
    EditText emailAddress;
    EditText phoneNumber;
    Switch geolocation;
    boolean geo;
    ProfileGenerator profileMaker = new ProfileGenerator();
    AdminLoginFragment adminLogin = new AdminLoginFragment();

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

        geolocation = findViewById(R.id.geolocation_switch);

        requestNotificationPermissions();

        profileButton.setOnClickListener( v -> {
            // Get the information for a user profile.
            String firstName = profileFirstName.getText().toString().trim();
            String lastName = profileLastName.getText().toString().trim();
            String email = emailAddress.getText().toString().trim();
            String phoneStr = phoneNumber.getText().toString();

            if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                geo = false;
            }else{
                geo = geolocation.isActivated();
            }

            // Check if input parameters are valid
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
                String imageURL = null;

                User newUser = new User(firstName, lastName, parseLong(phoneStr), email, geo,
                        imageURL);

                Firebase firebase = new Firebase();
                Log.e("FSFB", "We got to this point");
                firebase.createUser(newUser, new Firebase.OnUserCreatedListener() {
                    @Override
                    public void onUserCreated(String userId) {
                        // Assign the id for the new user into the app and confirm sign in
                        AppUser.setUserId(userId);
                        AppUser.setHasSignedIn(true);

                        SharedPreferences preferences =
                                FirstSignInActivity.this.getSharedPreferences(
                                        "com.example.genzgpt", Context.MODE_PRIVATE);

                        // Store info that the user has signed in for future app usage
                        preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
                        Log.d("FSFB", String.valueOf(AppUser.getHasSignedIn()));
                        preferences.edit().putString("id", AppUser.getUserId()).apply();

                        Log.e("FSI UserId", userId);
                        Log.e("FSFB", "Successful User Creation");

                        // Set up Firebase Messaging for this user.
                        FirebaseMessages fms = new FirebaseMessages(getApplicationContext());
                        fms.FMSFlow(userId);

                        // Generate the profile picture for this user
                        Bitmap bitmap = profileMaker.generateProfile(firstName, lastName);
                        CameraHandler uriGetter = new CameraHandler();
                        Context context = getApplicationContext();
                        Uri uri = uriGetter.getImageUri(bitmap, context);
                        Firebase.uploadImageForUser(AppUser.getUserId(), uri,
                                new ProgressDialog(context), context);

                        finish();
                    }

                    @Override
                    public void onEmailAlreadyExists() {
                        Toast.makeText(getApplicationContext(),
                                "The provided email already exists for another user." +
                                "Please use a different email.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserCreationFailed(Exception e) {
                        Log.e("FSFB", "The User was not Created", e);
                    }
                });
            }
        });
        geolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))){
                    requestPermission();
                }
            }
        });


        // Set the adminButton to send to the admin sign in page.
        adminButton.setOnClickListener( v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.sign_in_container, adminLogin)
                    .addToBackStack("FirstSign")
                    .commit();
        });
    }

    /**
     * Checks if a name contains only letters.
     * @param name
     * The name that will be checked
     *
     * @return
     * true if the name contains only letters.
     */
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

    /**
     * Checks if a phone number is valid based on certain parameters.
     * @param phone
     * The phone number to check.
     * @return
     * True if the phone number is valid. False Otherwise.
     */
    private boolean isValidPhone(String phone) {
        return (phone.length() >= 4 && phone.length() <= 10);
    }

    /**
     * Checks if the email provided is valid based on certain parameters.
     * @param email
     * The email to verify is valid.
     * @return
     * True if the email is valid. False otherwise.
     */
    private boolean isValidEmail(String email) {
        return (email.contains("@") && email.contains("."));
    }

    /**
     *
     */
    public void requestPermission(){
        ActivityCompat.requestPermissions(FirstSignInActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GeolocationTracking.FINE_LOCATION_PERMISSION);
    }

    /**
     * This method will check if permissions are granted or denied
     * @param permission
     * A string representing the permissions that a user has or has not enabled.
     * @return a boolean value
     */
    public boolean checkPermission(String permission){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Requests permission to receive push notifications from the app.
     */
   private void requestNotificationPermissions() {
        // if we are on a build that requires this request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            // if the permission has not been given already somehow
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                   android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 7);
            }
        }
    }

    @Override
    protected void onResume() {
       super.onResume();
       SharedPreferences preferences = getSharedPreferences("com.example.genzgpt",
               Context.MODE_PRIVATE);
       boolean isAdmin = preferences.getBoolean("admin", false);

       if (isAdmin) {
           finish();
       }
    }
}