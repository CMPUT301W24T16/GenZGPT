package com.example.genzgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
        // Go to another Activity if the user needs to put in their information.
        sendToFirstTime();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Intent firstTime = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(firstTime);
        }
    }
}