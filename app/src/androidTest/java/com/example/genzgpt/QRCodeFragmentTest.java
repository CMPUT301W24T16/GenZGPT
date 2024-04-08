package com.example.genzgpt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.google.common.base.Verify.verify;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import static java.util.regex.Pattern.matches;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.View.QRCodeFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class QRCodeFragmentTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Mock
    private Firebase mockFirebase;

    @Test
    public void testCameraPermissionDenied() {
        FragmentScenario<QRCodeFragment> scenario = FragmentScenario.launchInContainer(QRCodeFragment.class);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PackageManager.PERMISSION_DENIED, null);
        intending(hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)).respondWith(result);
    }


    @Test
    public void testCameraPermissionGranted() {
        // Launch the fragment in a container
        FragmentScenario<QRCodeFragment> scenario = FragmentScenario.launchInContainer(QRCodeFragment.class);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PackageManager.PERMISSION_GRANTED, null);
        intending(hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)).respondWith(result);
    }
}
