package com.example.genzgpt;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.service.notification.StatusBarNotification;

import androidx.core.app.ActivityCompat;
import androidx.test.rule.ActivityTestRule;

import com.example.genzgpt.Model.MessageNotification;

import org.junit.Test;

/**
 * Test Cases for the MessageNotificationClass.
 */
public class MessageNotificationTest {
    public MessageNotification messageNoti;


    @Test
    public void testNotificationCreation() {
        // Test that we actually get a notification from this class
        String name = "Event";
        String message = "Hello World!";
        messageNoti = new MessageNotification(name, message);

        Notification notification = messageNoti.getDisplayable(getApplicationContext());

        assertNotNull(notification);
    }

    @Test
    public void testCompareMultipleNotifications() {
        // Test that the notifications are actually different from each other
        String name = "Event";
        String message = "Hello World!";
        messageNoti = new MessageNotification(name, message);

        Notification notification = messageNoti.getDisplayable(getApplicationContext());

        name = "Event";
        message = "Hello Again!";

        messageNoti = new MessageNotification(name, message);
        Notification notification1 = messageNoti.getDisplayable(getApplicationContext());
        assertNotEquals(notification, notification1);

        name = "New Event";
        message = "Hello Once More!";

        messageNoti = new MessageNotification(name, message);
        Notification notification2 = messageNoti.getDisplayable(getApplicationContext());

        assertNotEquals(notification, notification2);
        assertNotEquals(notification1, notification2);
    }
}
