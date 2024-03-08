package com.example.genzgpt;

import android.app.Instrumentation;
import android.content.Intent;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.fragment.app.testing.FragmentScenario.FragmentAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.genzgpt.R;
import com.example.genzgpt.FirstActivity;
import com.example.genzgpt.View.MainPageFragment;
import com.example.genzgpt.View.UserListFragment;
import com.example.genzgpt.View.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Initialize Intents for testing Intent navigation
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents after testing Intent navigation
        Intents.release();
    }

    @Test
    public void testNavigationBarClicks() {
        // Check if the initial fragment is displayed
        Espresso.onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));

        // Click on the "Events" tab
        Espresso.onView(withId(R.id.events)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));

        // Click on the "QR" tab
//        Espresso.onView(withId(R.id.qr)).perform(ViewActions.click());
//        Espresso.onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));

        // Click on the "Event Host" tab
        Espresso.onView(withId(R.id.event_host)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));

        // Click on the "Profile" tab
        Espresso.onView(withId(R.id.profile)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testSendToFirstTime() {
        // Mock the Intent to FirstActivity
        Intents.intending(IntentMatchers.hasComponent(FirstActivity.class.getName()))
                .respondWith(new Instrumentation.ActivityResult(0, null));

        // Perform the action that triggers the Intent to FirstActivity
        activityTestRule.getActivity().sendToFirstTime();

        // Verify that the Intent to FirstActivity was sent
        Intents.intended(IntentMatchers.hasComponent(FirstActivity.class.getName()));
    }
}
