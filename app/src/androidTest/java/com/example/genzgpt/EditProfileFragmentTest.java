package com.example.genzgpt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.genzgpt.Model.AppUser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditProfileFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testEditProfileButtonClick(){
        AppUser.setUserId("yECi9vAeWEtfvMF3qW99");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_profile_button)).perform(click());
        onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
    }
    @Test
    public void testEditProfile(){
        testEditProfileButtonClick();
        onView(withId(R.id.edit_first_name)).perform(typeText("New"));
        onView(withId(R.id.edit_last_name)).perform(typeText("Name"));
        onView(withId(R.id.edit_email)).perform(typeText("NewEmail@newemail.com"));
        onView(withId(R.id.edit_phone_number)).perform(typeText("0987654321"));
        onView(withId(R.id.geolocation_switch)).perform(click());
    }
}
