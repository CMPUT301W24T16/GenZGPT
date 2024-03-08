package com.example.genzgpt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

/**
 * The General Structure for a notification that is used in this app.
 * FIXME: Currently does nothing
 */
abstract class AppNotification {
    /**
     * The message that the notification will be sent with.
     */
    protected String message;

    /**
     * The title of the notification.
     */
    protected String title;

    /**
     * The name of the channel that this notification will be sent through;
     */
    protected CharSequence channelName;

    /**
     * The description of the channel that this notification will be sent through;
     */
    protected String channelDescription;

    /**
     * The identifier for the channel that this notification will be sent through.
     */
    protected String channelID;

    /**
     * A unique identifier specific to an individual instance of a notification
     * (used for potential to modify / delete notifications that already exist).
     */
    protected int notificationID;

    /**
     * Returns a builder that will allow other processes to build a notification.
     * @param context
     *      The information needed about the app to construct this notification builder.
     *      FIXME: I HAVE NO IDEA WHAT KIND OF CONTEXT I AM ACTUALLY LOOKING FOR.
     * @return
     *      The method of building the specific notification that should be sent out.
     */
    abstract NotificationCompat.Builder getBuilder(@NonNull Context context);

    /**
     * Allows access to the channelID of an AppNotification.
     * @return
     *      The channelID for a particular AppNotification.
     */
    public String getChannelID() {
        return channelID;
    }

    /**
     *  Allows access to the name of the channel of an AppNotification.
     * @return
     *      The name of a channel of a particular AppNotification.
     */
    public CharSequence getChannelName() {
        return channelName;
    }

    /**
     * A getter for the Channel Description of a specific AppNotification.
     *
     * @return
     * The channel description for a notification.
     */
    public String getChannelDescription() {
        return channelDescription;
    }

    /**
     * A getter for the notificationID for a specific
     * @return
     */
    public int getNotificationID() {
        return notificationID;
    }
}
