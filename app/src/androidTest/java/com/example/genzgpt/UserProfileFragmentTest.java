package com.example.genzgpt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



import android.app.Instrumentation;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.View.UserProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testUserProfileDisplay(){
        AppUser.setUserId("yECi9vAeWEtfvMF3qW99");
        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.BaseFragment)).check(matches(isDisplayed()));
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            Log.e("UserTest", "Timer error in UserTest");
        }
    }
}
