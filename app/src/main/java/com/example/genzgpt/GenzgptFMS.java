package com.example.genzgpt;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * This class handles the Firebase Messaging Service for this app
 * FIXME: Can we just send some version of the appNotification to the firebase, receive, then use CreateNotification on that?
 */
public class GenzgptFMS extends FirebaseMessagingService {

    // I am not entirely sure if we need this? It may need to be modified
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        /*
        * super.onMessageReceived(message);
        * Not entirely certain how to handle this right now.
        * We need to be able to send an AppNotification to the database
        */
    }

    public void CreateNotification(AppNotification notification, @NonNull Context context) {
        NotificationCompat.Builder builder = notification.getBuilder(context);

        NotificationManager manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notification.getChannelID(),
                    notification.getChannelName(), NotificationManager.IMPORTANCE_DEFAULT);

            // This does nothing if the channel already exists
            manager.createNotificationChannel(channel);
        }

        manager.notify(notification.getNotificationID(), builder.build());
    }
}