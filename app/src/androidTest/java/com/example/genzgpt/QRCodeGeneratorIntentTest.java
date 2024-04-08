package com.example.genzgpt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.genzgpt.Model.QRCodeGenerator;
import com.example.genzgpt.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class QRCodeGeneratorIntentTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testGenerateCheckInQRCode() {
        String eventId = "12345";
        int width = 300;
        int height = 300;

        Bitmap qrCodeBitmap = QRCodeGenerator.generateCheckInQRCode(eventId, width, height);

        assertThat(qrCodeBitmap, notNullValue());
        assertThat(qrCodeBitmap.getWidth(), equalTo(width));
        assertThat(qrCodeBitmap.getHeight(), equalTo(height));
    }

    @Test
    public void testGenerateSignUpQRCode() {
        String eventId = "54321";
        int width = 300;
        int height = 300;

        Bitmap qrCodeBitmap = QRCodeGenerator.generateSignUpQRCode(eventId, width, height);

        assertThat(qrCodeBitmap, notNullValue());
        assertThat(qrCodeBitmap.getWidth(), equalTo(width));
        assertThat(qrCodeBitmap.getHeight(), equalTo(height));
    }
}