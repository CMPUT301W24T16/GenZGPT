package com.example.genzgpt.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

/**
 * This class handles Firebase Cloud Messaging for sending and receiving messages.
 */
public class FirebaseMessages extends FirebaseMessagingService {

    private final Context context;

    public FirebaseMessages(Context context) {
        this.context = context;
    }

    /**
     * Retrieves the device token from Firebase Cloud Messaging.
     */
    public void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            storeDeviceToken(token);
                        } else {
                            Log.e("FirebaseMessages", "Failed to retrieve device token", task.getException());
                            // Display an error message to the user if necessary
                        }
                    }
                });
    }

    /**
     * Stores the device token in SharedPreferences.
     * @param token The device token to be stored.
     */
    private void storeDeviceToken(String token) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("deviceToken", token);
        editor.apply();
    }

    /**
     * Sends a message to a specific device.
     * @param deviceToken The device token of the recipient.
     * @param messageData The data to be sent in the message.
     */
    public void sendMessageToDevice(String deviceToken, Map<String, String> messageData) {
        RemoteMessage message = new RemoteMessage.Builder(deviceToken)
                .setData(messageData)
                .build();

        FirebaseMessaging.getInstance().send(message);
    }

    /**
     * Sends a message to multiple devices.
     * @param deviceTokens The device tokens of the recipients.
     * @param messageData The data to be sent in the message.
     */
    public void sendMessageToMultipleDevices(List<String> deviceTokens, Map<String, String> messageData) {
        for (String deviceToken : deviceTokens) {
            sendMessageToDevice(deviceToken, messageData);
        }
    }

    /**
     * Retrieves the stored device token from SharedPreferences.
     * @return The stored device token, or null if not found.
     */
    public String getStoredDeviceToken() {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return preferences.getString("deviceToken", null);
    }

    /**
     * Checks if a device token is available.
     * @return True if a device token is available, false otherwise.
     */
    public boolean isDeviceTokenAvailable() {
        String deviceToken = getStoredDeviceToken();
        return deviceToken != null && !deviceToken.isEmpty();
    }

    /**
     *displays a toast message when a message is sent successfully
     * @param messageId
     */
    @Override
    public void onMessageSent(@NonNull String messageId) {
        super.onMessageSent(messageId);
        // Message sent successfully
        Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show();
    }

    /**
     *displays a toast message when a message is not sent successfully
     * @param messageId
     * @param exception
     */
    @Override
    public void onSendError(@NonNull String messageId, @NonNull Exception exception) {
        super.onSendError(messageId, exception);
        // Message sending failed
        Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show();
        Log.e("FirebaseMessages", "Failed to send message", exception);
    }

    /**
     *displays a toast message when a new token is generated
     * @param token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Update the stored device token
        storeDeviceToken(token);
    }

    /**
     * Sends the device token to the server.
     * @param deviceToken The device token to be sent.
     */
    public void sendDeviceTokenToFirebase(String deviceToken) {
        // Send the device token to the server

    }
}
