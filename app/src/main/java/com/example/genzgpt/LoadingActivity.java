package com.example.genzgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;

/**
 * A place to send the user of the app between switches of activities.
 */
public class LoadingActivity extends AppCompatActivity {
    public boolean isAdmin = false;
    public boolean isSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    /**
     * Sends the user to a first time sign in if they have need to do this already.
     */
    public void sendToFirstTime() {
        if (!isSignedIn && !isAdmin) {
            Log.d("Loading", "Sent to First Time");
            Intent toFirst = new Intent(LoadingActivity.this, FirstSignInActivity.class);
            startActivity(toFirst);
        }
    }

    /**
     * Sends the user to the administrator activity (if they meet the right conditions).
     */
    public void sendToAdmin() {
        if (isAdmin) {
            Log.d("Loading", "Sent to Admin");
            Intent toAdmin = new Intent(LoadingActivity.this, AdminActivity.class);
            startActivity(toAdmin);
        }
    }

    /**
     * Checks whether or not the user of the app should be sent to the MainActivity.
     */
    public void sendToMain() {
        if (isSignedIn) {
            Log.d("Loading", "Sent to Main");
            Intent toMain = new Intent(LoadingActivity.this, MainActivity.class);
            startActivity(toMain);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        Log.e("Resume", "Resume Works");
        isSignedIn = preferences.getBoolean("signIn", false);
        String test = String.valueOf(isSignedIn);
        Log.d("isSignedIn", test);
        isAdmin = preferences.getBoolean("admin", false);

        AppUser.setUserId(preferences.getString("id", null));

        sendToFirstTime();
        sendToAdmin();
        sendToMain();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        Log.e("Restart", "Restart Works");
        isSignedIn = preferences.getBoolean("signIn", false);
        String test = String.valueOf(isSignedIn);
        Log.d("isSignedIn", test);
        isAdmin = preferences.getBoolean("admin", false);
        AppUser.setUserId(preferences.getString("id", null));
        sendToFirstTime();
        sendToAdmin();
        sendToMain();
    }
}