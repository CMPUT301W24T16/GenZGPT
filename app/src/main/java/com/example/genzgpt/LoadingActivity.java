package com.example.genzgpt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends AppCompatActivity {
    public boolean isAdmin = false;
    public boolean isSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        isSignedIn = preferences.getBoolean("signIn", false);
        isAdmin = preferences.getBoolean("admin", false);

        sendToFirstTime();
        sendToAdmin();
    }

    /**
     * Sends the user to a first time sign in if they have need to do this already.
     */
    public void sendToFirstTime() {
        if (!isSignedIn) {
            Intent toFirst = new Intent(LoadingActivity.this, FirstSignInActivity.class);
            startActivity(toFirst);
        }
    }

    /**
     * Sends the user to the administrator activity
     */
    public void sendToAdmin() {
        if (isAdmin) {
            Intent toAdmin = new Intent(LoadingActivity.this, AdminActivity.class);
            startActivity(toAdmin);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = this.getSharedPreferences("com.example.genzgpt",
                Context.MODE_PRIVATE);

        Log.e("Resume", "Resume Works");
        isSignedIn = preferences.getBoolean("signIn", false);
        isAdmin = preferences.getBoolean("admin", false);
        sendToAdmin();
        sendToFirstTime();
    }
}