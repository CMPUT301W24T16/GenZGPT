package com.example.genzgpt;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for the UserListFragment.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserListFragmentTest {
    @Rule
    public ActivityScenarioRule<AdminActivity> activityActivityScenarioRule = new
            ActivityScenarioRule<>(AdminActivity.class);

    @Test
    public void confirmIsDisplayed() {
        onView(withId(R.id.profile)).perform(click());

        // we want to confirm that the list of users is displayed.
        onView(withId(R.id.userRecyclerView)).check(matches(isDisplayed()));
    }


    @Test
    public void confirmDeleteUser() {
        onView(withId(R.id.profile)).perform(click());

        onView(withId(R.id.userRecyclerView)).perform(click());
        try{
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.d("UserListTest", "Deletion Wait Failure");
        }

        onData(withText("Delete User")).perform(click());
    }

    @Test
    public void confirmDeleteUserProfile() {
        onView(withId(R.id.profile)).perform(click());

        onView(withId(R.id.userRecyclerView)).perform(click());
        try{
            Thread.sleep(10000);
        }
        catch (InterruptedException ie) {
            Log.d("UserListTest", "Deletion Wait Failure");
        }

        onData(withText("Delete User Profile")).perform(click());
    }
}
