package com.example.genzgpt;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.genzgpt.AdminActivity;
import com.example.genzgpt.FirstSignInActivity;
import com.example.genzgpt.LoadingActivity;
import com.example.genzgpt.MainActivity;

@RunWith(AndroidJUnit4.class)
public class LoadingActivityTest {

    @Rule
    public ActivityScenarioRule<LoadingActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoadingActivity.class);

    @Rule
    public GrantPermissionRule permissionNotifications = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);

    private LoadingActivity loadingActivity;

    @Before
    public void setUp() {
        // Get the activity instance before each test
        activityScenarioRule.getScenario().onActivity(activity -> loadingActivity = activity);
    }

    @Test
    public void testSendToFirstTime() {
        // Set isSignedIn to false
        loadingActivity.isSignedIn = false;

        // Call the method to send to first time sign in
        loadingActivity.sendToFirstTime();

        // Verify that FirstSignInActivity is started
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Sent to First Time")));
    }

    @Test
    public void testSendToAdmin() {
        // Set isAdmin to true
        loadingActivity.isAdmin = true;

        // Call the method to send to admin activity
        loadingActivity.sendToAdmin();

        // Verify that AdminActivity is started
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Sent to Admin")));
    }

    @Test
    public void testSendToMain() {
        // Set isSignedIn to true
        loadingActivity.isSignedIn = true;

        // Call the method to send to main activity
        loadingActivity.sendToMain();

        // Verify that MainActivity is started
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Sent to Main")));
    }
}
