package com.example.genzgpt;

import static android.icu.number.NumberRangeFormatter.with;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.MatcherAssert.assertThat;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.View.EditProfileFragment;
import com.example.genzgpt.View.UserProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for edit profile fragment
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditProfileFragmentTest {
        @Rule
        public ActivityScenarioRule<MainActivity> activityScenarioRule =
                new ActivityScenarioRule<>(MainActivity.class);
        @Test
        public void testEditProfileDisplay(){
            AppUser.setUserId("yECi9vAeWEtfvMF3qW99");
            onView(withId(R.id.profile)).perform(click());
            onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.edit_profile_button)).perform(click());
            onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Log.e("UserTest", "Timer error in UserTest");
            }
        }
    }