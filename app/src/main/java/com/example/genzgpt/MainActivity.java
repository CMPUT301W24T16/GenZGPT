package com.example.genzgpt;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.genzgpt.Model.AppUser;
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

        // Get information that should be stored between runs of the app
        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        // Confirm whether or not a user has signed in on this application.
        hasSignedIn = preferences.getBoolean("signIn", false);
        if (preferences.contains("id")) {
            AppUser.setUserId(preferences.getString("id", null));
        }

        // Go to another Activity if the user needs to put in their information.
        // Putting this before setContentView will stop Main Activity from showing initially
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
        if (AppUser.getHasSignedIn() && AppUser.getUserId() != null) {
            SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                    Context.MODE_PRIVATE);
            preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
            preferences.edit().putString("id", AppUser.getUserId()).apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (AppUser.getHasSignedIn() && AppUser.getUserId() != null) {
            SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                    Context.MODE_PRIVATE);
            preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
            preferences.edit().putString("id", AppUser.getUserId()).apply();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (AppUser.getHasSignedIn() && AppUser.getUserId() != null) {
            SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                    Context.MODE_PRIVATE);
            preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
            preferences.edit().putString("id", AppUser.getUserId()).apply();
        }
    }


    /**
     * Handles resuming Main Activity (this is used for when we travel from FirstSignIn Currently)
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (AppUser.getHasSignedIn() && AppUser.getUserId() != null) {
            SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                    Context.MODE_PRIVATE);

            preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
            preferences.edit().putString("id", AppUser.getUserId()).apply();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (AppUser.getHasSignedIn() && AppUser.getUserId() != null) {
            SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                    Context.MODE_PRIVATE);

            preferences.edit().putBoolean("signIn", AppUser.getHasSignedIn()).apply();
            preferences.edit().putString("id", AppUser.getUserId()).apply();
        }
    }
}