package com.example.genzgpt.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.genzgpt.Model.AppNotification;
import com.example.genzgpt.Model.MessageNotification;
import com.example.genzgpt.Model.MilestoneNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles Firebase Cloud Messaging for sending and receiving messages.
 */
public class FirebaseMessages extends FirebaseMessagingService {

    private final Context context;
    private final FirebaseFirestore db;
    private String userId;
    private final int milestone1 = 1;
    private final int milestone2 = 5;
    private final int milestone3 = 10;
    private final int milestone4 = 25;


    /**
     * A constructor for FirebaseMessages service.
     * @param context
     * The context needed for FirebaseMessages and all its methods to work correctly.
     */
    public FirebaseMessages(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Retrieves the device token from Firebase Cloud Messaging.
     */
    public void getDeviceToken(String userId) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            storeDeviceTokenOnDevice(token);
                            storeDeviceTokenOnFirebase(token, userId);
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
    private void storeDeviceTokenOnDevice(String token) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("deviceToken", token);
        editor.apply();

    }

    /**
     * Stores the device token in Firestore.
     * @param token The device token to be stored.
     * @param userId The ID of the user to store the device token for.
     */
    private void storeDeviceTokenOnFirebase(String token, String userId){
        if (userId != null) {
            // Update the user document in Firestore with the device token
            db.collection("users").document(userId)
                    .update("deviceToken", token)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseMessages", "Device token stored in Firestore");
                            } else {
                                Log.e("FirebaseMessages", "Failed to store device token in Firestore", task.getException());
                            }
                        }
                    });
        }
        else {
            Log.e("FirebaseMessages", "User ID is null");
        }
    }

    /**
     * Sends a message to a specific device.
     * @param deviceToken The device token of the recipient.
     * @param messageData The data to be sent in the message.
     */
    /**public void sendMessageToDevice(String deviceToken, Map<String, String> messageData) {
        Log.d("FirebaseMessages","sendMessageToDevice");
        RemoteMessage message = new RemoteMessage.Builder(deviceToken)
                .setData(messageData)
                .build();

        FirebaseMessaging.getInstance().send(message);
    }*/

    public void sendMessageToDevice(String deviceToken, String title, String content, String type) {
        String apiKey = "AAAAu_84Npw:APA91bEoQshG5JIrh1pUxwSfC2lm2KXC8cvbbiEN0qDXJyibtHipaNFwcvsHfjlo2nFPzRCn68ew1bUgQinicxHFGHrOviuvQnfD1GNICl4_3OIFC6PsaWS0BAMQ7QbaengCTxCoPM1l";
        String url = "https://fcm.googleapis.com/fcm/send";

        try {
            JSONObject message = new JSONObject();
            message.put("to", deviceToken);

            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", content);

            JSONObject data = new JSONObject();
            data.put("type", type);

            message.put("notification", notification);
            message.put("data", data);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, message,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Message sent successfully
                            Log.d("FirebaseMessages", "Message sent successfully");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof AuthFailureError) {
                                // Authentication failure
                                Log.e("FirebaseMessages", "Authentication failure", error);
                                // Handle the authentication failure, e.g., check the server key
                            } else {
                                // Other error
                                Log.e("FirebaseMessages", "Error sending message", error);
                                // Handle other errors appropriately
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + apiKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            // Add the request to the Volley request queue
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } catch (JSONException e) {
            // Handle the exception
            Log.e("FirebaseMessages", "Error creating JSON payload", e);
        }
    }

    /**
     * Sends a message to a phone using Firebase Messaging
     * @param deviceToken
     * The token of the device to send a message to.
     * @param title
     * The title of the message to send.
     * @param content
     * The content of the message to send.
     * @param type
     * The type of message to send.
     */
    /**public void sendMessageToDevice(String deviceToken, String title, String content, String type) {
        RemoteMessage message = new RemoteMessage.Builder(deviceToken)
                .addData("title", title)
                .addData("content", content)
                .setMessageType(type)
                .build();
    }*/

    /**
     * Sends a message to multiple devices.
     * @param deviceTokens The device tokens of the recipients.
     * @param messageData The data to be sent in the message.
     */
    /**public void sendMessageToMultipleDevices(List<String> deviceTokens, Map<String, String> messageData) {
        for (String deviceToken : deviceTokens) {
            sendMessageToDevice(deviceToken, messageData);
        }
    }*/

    /**
     * Sends a message to multiple devices.
     * @param deviceTokens
     * The device tokens of the recipients.
     * @param title
     * The title of the message to send.
     * @param content
     * The content of the message to send.
     * @param type
     * The type of message to send.
     */
    public void sendMessageToMultipleDevices(List<String> deviceTokens, String title,
                                             String content, String type) {
        Log.d("FirebaseMessages","sendMessageToMultipleDevices: message loop started");
        for (String deviceToken : deviceTokens) {
            Log.d("FirebaseMessages","sendMessageToMultipleDevices: inside loop");
            sendMessageToDevice(deviceToken, title, content, type);
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
     * @param messageId the id of the message
     */
    @Override
    public void onMessageSent(@NonNull String messageId) {
        Log.d("FirebaseMessages", "onMessageSent");
        super.onMessageSent(messageId);
        // Message sent successfully
        Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show();
    }

    /**
     *displays a toast message when a message is not sent successfully
     * @param messageId the id of the message
     * @param exception the error thrown on failure
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
     * @param token device token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Update the stored device token
        storeDeviceTokenOnDevice(token);
        storeDeviceTokenOnFirebase(token, this.userId);
    }

    /**
     * This method is used to do the correct steps to handle account creation
     * @param userId the id of the user
     */
    public void FMSFlow(String userId){
        this.userId = userId;
        getDeviceToken(userId);
        Log.d("FirebaseMessages", "FMSFlow: Device token sent to Firestore");
    }

    /**
     * Displays a notification on the current device
     * @param notification
     * An AppNotification that will be used to provide info about what to display
     * @param id
     * The unique identifier for
     */
    public void displayNotification(AppNotification notification, int id) {
        // get the notification to display
        Notification displayable = notification.getDisplayable(context);

        // get the manager for all the notifications
        NotificationManager manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        // check if a notification channel is necessary on this device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notification.getChannelID(),
                    notification.getChannelName(), NotificationManager.IMPORTANCE_DEFAULT);

            // you can implement the below line if you wish. Just need to give classes a channelDescription
            //channel.setDescription(notification.getChannelDescription());

            // This does nothing if the channel already exists
            manager.createNotificationChannel(channel);
        }

        // TODO CONFIRM THAT THIS ACTUALLY WORKS ON A DEVICE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("FirebaseMessages", "displayNotification");
                manager.notify(id, displayable);
            }
        }
        else {
            manager.notify(id, displayable);
        }

    }

    /**
     * Handles what happens when a user receives a message on their phone.
     * @param remoteMessage Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("FirebaseMessages", "onMessageReceived");
        super.onMessageReceived(remoteMessage);

        // get information from the RemoteMessage
        String notiType = remoteMessage.getMessageType();
        Map<String, String> messageData = remoteMessage.getData();
        String title = messageData.get("title");
        String content = messageData.get("content");

        // FIXME Not sure if this is ever null due to method description
        String notiId = remoteMessage.getMessageId();

        if (notiId == null) {
            Log.d("NotificationIdError", "Firebase did not give notification an ID");
        }
        int notificationId = Integer.parseInt(notiId);

        AppNotification notification;

        // do different things depending on the notification type
        if (notiType.equals("milestone")) {
            notification = new MilestoneNotification(title, Integer.parseInt(content));
        }
        else if (notiType.equals("message")) {
            notification = new MessageNotification(title, content);
        }
        else {
            notification = new MessageNotification("Error!", "We broke something");
        }

        displayNotification(notification, notificationId);
    }
}
