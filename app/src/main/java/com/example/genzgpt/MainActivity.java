package com.example.genzgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.genzgpt.Controller.CameraFragment;
import com.example.genzgpt.View.EventHostFragment;
import com.example.genzgpt.View.MainPageFragment;
import com.example.genzgpt.View.MyEventsFragment;
import com.example.genzgpt.View.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Credit to https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/ (needs proper format)
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView navBar;
    MainPageFragment homePage = new MainPageFragment();
    MyEventsFragment myEvents = new MyEventsFragment();
    EventHostFragment eventHost = new EventHostFragment();
    UserProfileFragment userProfile = new UserProfileFragment();
    CameraFragment cameraActivity = new CameraFragment();



    NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, homePage)
                        .commit();
                return true;
            } else if (id == R.id.events) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, myEvents)
                        .commit();
                return true;
            } else if (id == R.id.qr) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, cameraActivity)
                        .commit();
                return true;
            } else if (id == R.id.event_host) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventHost)
                        .commit();
                return true;
            } else if (id == R.id.profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, userProfile)
                        .commit();
                return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomNavigationView);

        navBar.setOnItemSelectedListener(navListener);
        navBar.setSelectedItemId(R.id.home);
    }

}