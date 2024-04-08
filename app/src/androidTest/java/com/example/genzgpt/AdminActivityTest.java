package com.example.genzgpt;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.genzgpt.AdminActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminActivityTest {

    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    @Test
    public void testAdminProfilesNavigation() {
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.BaseAdminFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdminHomeNavigation() {
        onView(withId(R.id.admin_home)).perform(click());
        onView(withId(R.id.BaseAdminFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdminEventsNavigation() {
        onView(withId(R.id.allevents)).perform(click());
        onView(withId(R.id.BaseAdminFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testDefaultFragment() {
        // Ensure that the default fragment (adminHome) is displayed initially
        onView(withId(R.id.BaseAdminFragment)).check(matches(isDisplayed()));
    }
}
