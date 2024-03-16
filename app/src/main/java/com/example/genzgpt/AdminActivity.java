package com.example.genzgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.genzgpt.View.AdminEventsFragment;
import com.example.genzgpt.View.AdminHomeFragment;
import com.example.genzgpt.View.AdminProfilesFragment;
import com.example.genzgpt.View.UserListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * The main Activity for administrator stories and usage (not finished yet).
 *
 *  Acknowledgements:
 *  1. NavBar Credit to agarwalkeshav8399, Last Updated April 5th: 2023,
 *  <a href="https://creativecommons.org/licenses/by-sa/4.0/">...</a>
 *  <a href="https://auth.geeksforgeeks.org/user/agarwalkeshav8399">...</a>
 *  <a href="https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/">...</a>
 */
public class AdminActivity extends AppCompatActivity {
    BottomNavigationView adminBar;

    AdminEventsFragment adminEvents = new AdminEventsFragment();
    AdminHomeFragment adminHome = new AdminHomeFragment();
    UserListFragment adminProfiles = new UserListFragment();

    // adminListener was made with Acknowledgements 1.
    NavigationBarView.OnItemSelectedListener adminListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseAdminFragment, adminProfiles)
                        .commit();
                return true;
            } else if (id == R.id.admin_home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseAdminFragment, adminHome)
                        .commit();
                return true;
            } else if (id == R.id.allevents) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.BaseAdminFragment, adminEvents)
                        .commit();
                return true;
            }
            return false;
        }
    };

    /**
     * Handles Creation of AdminActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminBar = findViewById(R.id.nav_admin);
        adminBar.setOnItemSelectedListener(adminListener);

    }
}