package com.example.genzgpt;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.genzgpt.Controller.QRCodeFragment;
import com.example.genzgpt.View.EventHostFragment;
import com.example.genzgpt.View.EventListFragment;
import com.example.genzgpt.View.MainPageFragment;
import com.example.genzgpt.View.UserListFragment;
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
    UserListFragment myEvents = new UserListFragment();
    EventListFragment eventHost = new EventListFragment();
    UserProfileFragment userProfile = new UserProfileFragment();
    QRCodeFragment QRCodeActivity = new QRCodeFragment();

    public static boolean hasSignedIn = false;


    // navListener was made using acknowledgement.
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
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseFragment, QRCodeActivity)
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

        // currently does nothing
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Test")) {
                hasSignedIn = savedInstanceState.getBoolean("Test");
            }
        }

        // Confirm whether or not a user has signed in on this application.
        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt", Context.MODE_PRIVATE);
        if (preferences.contains("Test")) {
            hasSignedIn = preferences.getBoolean("Test", false);
        }

        // Go to another Activity if the user needs to put in their information.
        sendToFirstTime();

        navBar = findViewById(R.id.bottomNavigationView);

        navBar.setOnItemSelectedListener(navListener);
        navBar.setSelectedItemId(R.id.home);
    }

    /**
     * Sends the user to a first time sign in if they have need to do this already.
     */
    public void sendToFirstTime() {
        if (!hasSignedIn) {
            hasSignedIn = true;
            Intent toFirst = new Intent(MainActivity.this, FirstSignInActivity.class);
            startActivity(toFirst);
        }
    }

    /**
     * Handles saving data into the app for future usages.
     * @param outState Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("Test", hasSignedIn).apply();
        outState.putBoolean("Test", hasSignedIn);
    }
}