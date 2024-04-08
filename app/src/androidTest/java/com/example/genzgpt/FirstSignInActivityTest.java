package com.example.genzgpt;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.example.genzgpt.FirstSignInActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FirstSignInActivityTest {

    @Rule
    public ActivityScenarioRule<FirstSignInActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FirstSignInActivity.class);

    @Rule
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS);

    @Test
    public void testValidUserProfileCreation() {
        // Input valid user profile information
        onView(withId(R.id.first_name_fill)).perform(replaceText("John"));
        onView(withId(R.id.last_name_fill)).perform(replaceText("Doe"));
        onView(withId(R.id.email_fill)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.edit_phone)).perform(replaceText("1234567890"));

        // Click on profile button to create user
        onView(withId(R.id.user_profile_button)).perform(click());

        // Add Espresso assertion using custom matcher to check log message
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("EMAIL ALREADY EXISTS")));
    }

    @Test
    public void testInvalidUserNumber() {
        // Input valid user profile information
        onView(withId(R.id.first_name_fill)).perform(replaceText("John"));
        onView(withId(R.id.last_name_fill)).perform(replaceText("Doe"));
        onView(withId(R.id.email_fill)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.edit_phone)).perform(replaceText("190"));

        // Click on profile button to create user
        onView(withId(R.id.user_profile_button)).perform(click());

        // Add Espresso assertion using custom matcher to check log message
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Please ensure phone number is correct number of digits.")));
    }

    @Test
    public void testInvalidUserFirstName() {
        // Input valid user profile information
        onView(withId(R.id.first_name_fill)).perform(replaceText("Jo2hn"));
        onView(withId(R.id.last_name_fill)).perform(replaceText("Doe"));
        onView(withId(R.id.email_fill)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.edit_phone)).perform(replaceText("1234567890"));

        // Click on profile button to create user
        onView(withId(R.id.user_profile_button)).perform(click());

        // Add Espresso assertion using custom matcher to check log message
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Please ensure first name is filled out and only has letters.")));
    }

    @Test
    public void testInvalidUserEmail() {
        // Input valid user profile information
        onView(withId(R.id.first_name_fill)).perform(replaceText("John"));
        onView(withId(R.id.last_name_fill)).perform(replaceText("Doe"));
        onView(withId(R.id.email_fill)).perform(replaceText("johndoe"));
        onView(withId(R.id.edit_phone)).perform(replaceText("1234567890"));

        // Click on profile button to create user
        onView(withId(R.id.user_profile_button)).perform(click());

        // Add Espresso assertion using custom matcher to check log message
        onView(withId(android.R.id.content)).check(matches(new LogMessageMatcher("Please ensure email is filled out and is a valid email address.")));
    }

    @Test
    public void testGeolocationSwitchToggle() {
        // Check initial state of switch (should be off)
        onView(withId(R.id.geolocation_switch)).check(matches(isNotChecked()));

        // Perform click action on switch to toggle it
        onView(withId(R.id.geolocation_switch)).perform(click());

        // Check if switch state changes to on after click
        onView(withId(R.id.geolocation_switch)).check(matches(isChecked()));
    }

    @Test
    public void testAdminButtonClick() {
        // Click on admin button
        onView(withId(R.id.admin_button)).perform(click());

        // Verify that admin preference is set to true
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.example.genzgpt", Context.MODE_PRIVATE);
        boolean isAdmin = preferences.getBoolean("admin", false);
        assertTrue(isAdmin);
    }
}
