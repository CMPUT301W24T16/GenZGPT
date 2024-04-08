package com.example.genzgpt;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.View.MyEventsFragment;
import com.example.genzgpt.View.QRCodeFragment;
import com.example.genzgpt.View.AllEventsFragment;
import com.example.genzgpt.View.MainPageFragment;
import com.example.genzgpt.View.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * The Main Activity this app uses to run.
 *
 * Acknowledgements:
 * 1. NavBar Credit to agarwalkeshav8399, Last Updated April 5th: 2023,
 * <a href="https://creativecommons.org/licenses/by-sa/4.0/">...</a>
 * <a href="https://auth.geeksforgeeks.org/user/agarwalkeshav8399">...</a>
 * <a href="https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/">...</a>
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView navBar;
    MainPageFragment homePage = new MainPageFragment();
    MyEventsFragment myEvents = new MyEventsFragment();
    // FIXME SHOULD TAKE TO THE EVENTHOSTFRAGMENT
    AllEventsFragment eventHost = new AllEventsFragment();
    UserProfileFragment userProfile = new UserProfileFragment();
    QRCodeFragment QRCodeActivity = new QRCodeFragment();
    public static boolean hasSignedIn;


    // navListener was made using acknowledgement 1.
    // Configure the buttons on the Navbar to work as intended.
    NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, homePage)
                        .commit();
                return true;
            } else if (id == R.id.events) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, myEvents)
                        .commit();
                return true;
            } else if (id == R.id.qr) {
                // Create a new instance of QRCodeFragment
                QRCodeFragment qrCodeFragment = new QRCodeFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, qrCodeFragment)
                        .commit();
                return true;
            } else if (id == R.id.event_host) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, eventHost)
                        .commit();
                return true;
            } else if (id == R.id.profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, userProfile)
                        .commit();
                return true;
            }
            return false;
        }

    };

    /**
     * Handles the initial creation of MainActivity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get information that should be stored between runs of the app
        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        // Confirm whether or not a user has signed in on this application.
        hasSignedIn = preferences.getBoolean("signIn", false);
        if (preferences.contains("id")) {
            AppUser.setUserId(preferences.getString("id", null));
        }

        navBar = findViewById(R.id.bottomNavigationView);
        navBar.setOnItemSelectedListener(navListener);
        navBar.setSelectedItemId(R.id.home);
    }

    /**
     * Handles saving data into the app for future usages.
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        AppUser.setUserId(preferences.getString("id", null));

        Firebase firebase = new Firebase();
    }
}
