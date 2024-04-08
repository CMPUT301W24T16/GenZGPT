package com.example.genzgpt;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.genzgpt.View.AllEventsFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for the AllEventsFragment class.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AllEventsFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityScenarioRule = new
            ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void confirmDataIsShown() {
        // Confirm the data for the AllEventsFragment is shown.
        onView(withId(R.id.event_host)).perform(click());

        // Check that the view with all the events in it is displayed (the important view)
        onView(withId(R.id.eventsRecyclerView)).check(matches(isDisplayed()));
    }
}
