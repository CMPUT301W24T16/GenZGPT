package com.example.genzgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
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
    AdminProfilesFragment adminProfiles = new AdminProfilesFragment();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminBar = findViewById(R.id.nav_admin);
        adminBar.setOnItemSelectedListener(adminListener);

    }
}