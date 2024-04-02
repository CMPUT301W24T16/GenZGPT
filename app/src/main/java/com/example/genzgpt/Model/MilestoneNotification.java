package com.example.genzgpt.Model;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.genzgpt.R;

/**
 * The contents of a notification that an attendee shall receive after reaching a milestone
 * number of attendees at their event.
 * FIXME: Currently does nothing in the app
 */
public class MilestoneNotification extends AppNotification{
    /**
     * @see AppNotification#title
     */
    protected String title = "Event Milestone";

    /**
     * @see AppNotification#channelID
     */
    private String channelID = "Milestone";

    /**
     * @see AppNotification#notificationID
     */
    protected int notificationID = 0;

    /**
     * Constructs the format for how to notify an event organizer they have reached a certain
     * milestone.
     * @param eventName
     *      The name of the event whose milestone we are notifying an organizer about.
     * @param milestone
     *      The milestone number of attendees that the event has reached.
     */
    public MilestoneNotification(String eventName, Integer milestone) {
        this.message = "Congratulations! " + eventName + " has reached " + milestone.toString() +
                " attendees!";
    }

    /**
     * Creates a builder that can create an event milestone notification.
     * FIXME: The builder we return needs some other methods later (and maybe some removed).
     * FIXME: Mainly, we need to add Intent (or not since this might make the assignment 1000x harder)
     * @param context
     *      The information needed about the app to construct this notification builder.
     * @return
     *      A way for other processes to build a milestone notification.
     */
    public Notification getDisplayable(@NonNull Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, channelID)
                .setSmallIcon(R.drawable.test_picture) // FIXME: set to app icon later
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(VISIBILITY_PUBLIC);

        return builder.build();
    }
}
