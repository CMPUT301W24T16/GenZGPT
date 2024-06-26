package com.example.genzgpt.Model;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.genzgpt.R;

/**
 * A notification containing a message from an event organizer specified for attendees.
 */
public class MessageNotification extends AppNotification {
    /**
     * The name of the event we are sending information out for.
     */
    private String eventName;

    /**
     * @see AppNotification#channelID
     */
    protected String channelID = "Event Message";

    /**
     * @see AppNotification#notificationID
     */
    protected int notificationID = 1;

    /**
     * Retrieves information about the MessageNotification and formats the title.
     * @param _eventName
     *      The name of the event an organizer is sending out a message for.
     * @param _message
     *      The body of the message that the organizer wants to send out.
     */
    public MessageNotification(String _eventName, String _message) {
        message = _message;
        eventName = _eventName;
        title = eventName + " Message";
    }

    /**
     * Creates a builder for the message notification an organizer wants to send out.
     * Uses specific parameters such as NotificationCompat.BigTextStyle to fit all information
     * about this message into the notification.
     * @param context
     *      The information needed about the app to construct this notification builder.
     * @return
     *      A way for other processes to build a message notification.
     */
    public Notification getDisplayable(@NonNull Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context.getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.test_picture)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(VISIBILITY_PUBLIC);

        return builder.build();
    }
}
