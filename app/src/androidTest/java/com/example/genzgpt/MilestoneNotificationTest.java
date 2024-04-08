package com.example.genzgpt;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.app.Notification;


import com.example.genzgpt.Model.MilestoneNotification;

import org.junit.Test;

/**
 * Test Cases for MilestoneNotification
 */
public class MilestoneNotificationTest {
    public MilestoneNotification mileNoti;


    @Test
    public void testNotificationCreation() {
        // Test that we actually get a notification from this class
        String name = "Event";
        mileNoti = new MilestoneNotification(name, 5);

        Notification notification = mileNoti.getDisplayable(getApplicationContext());

        assertNotNull(notification);
    }

    @Test
    public void testCompareMultipleNotifications() {
        // Test that the notifications are actually different from each other
        String name = "Event";
        mileNoti = new MilestoneNotification(name, 5);

        Notification notification = mileNoti.getDisplayable(getApplicationContext());

        mileNoti = new MilestoneNotification(name, 10);

        Notification notification1 = mileNoti.getDisplayable(getApplicationContext());
        assertNotEquals(notification, notification1);

        name = "New Event";

        mileNoti = new MilestoneNotification(name, 5);
        Notification notification2 = mileNoti.getDisplayable(getApplicationContext());

        assertNotEquals(notification, notification2);
        assertNotEquals(notification1, notification2);
    }
}
