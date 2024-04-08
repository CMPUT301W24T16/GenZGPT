package com.example.genzgpt;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.FirebaseMessages;
import com.example.genzgpt.Model.User;

import org.junit.After;
import org.junit.Test;

/**
 * Test Cases for the FirebaseMessages class.
 */
public class FirebaseMessagesTest {
    FirebaseMessages messages = new FirebaseMessages(getApplicationContext());
    Firebase firebase = new Firebase();
    User messageTestUser = new User("Messages", "Test", 1234567890,
            "Ihopethisworks@mail.cosmos", false, null);
    String token;

    @Test
    public void testFMS() {
        // create user.
        // call getDeviceToken(), pass in the user id, call getStoredDeviceToken() send message to returned device token
        firebase.createUser(messageTestUser, new Firebase.OnUserCreatedListener() {
            @Override
            public void onUserCreated(String userId) {
                Log.d("FMSTEST", "ID Created");
                messageTestUser.setId(userId);
            }

            @Override
            public void onEmailAlreadyExists() {
                Log.d("FMSTest", "User to Create Already Exists");
            }

            @Override
            public void onUserCreationFailed(Exception e) {
                Log.d("FMSTest", "Failed testFMSFlow with exception");
            }
        });

        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.e("FMSTEST", "TIMER ERROR IN FIRST TEST", ie);
        }

        messages.getDeviceToken(messageTestUser.getId());
        String token = messages.getStoredDeviceToken();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        if (token == null) {
            token = preferences.getString("devicetoken", null);
        }

        try{
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.d("FMSTEST", "WE DIDN'T GO TO THE END");
        }

        assertNotNull(token);
        messages.sendMessageToDevice(token, "FMSTEST", "DID YOU GET THE FMS TEST?",
                "message");

        NotificationManager manager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        try{
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.d("FMSTEST", "WE DIDN'T GO TO THE END");
        }

        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.e("FMSTEST", "TIMER ERROR IN FIRST TEST", ie);
        }

        StatusBarNotification[] notificationsList = manager.getActiveNotifications();

        boolean testSent = false;

        testSent = (notificationsList.length > 0);

        assertTrue(testSent);
    }
}
