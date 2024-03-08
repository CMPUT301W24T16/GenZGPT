package com.example.genzgpt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.genzgpt.Controller.GalleryHandler;

@RunWith(AndroidJUnit4.class)
public class GalleryHandlerTest {

    @Test
    public void testOpenGalleryWithPermission() {
        ActivityScenario.launch(MainActivity.class);

        GalleryHandler.openGallery(getActivity());

    }

    @Test
    public void testOpenGalleryWithoutPermission() {
        // Arrange
        ActivityScenario.launch(MainActivity.class);

        // Act
        GalleryHandler.openGallery(getActivity());

    }


    @Test
    public void testHandleGalleryResultFailure() {
        // Arrange
        int requestCode = GalleryHandler.REQUEST_PICK_IMAGE;
        int resultCode = Activity.RESULT_CANCELED;
        Intent data = null;

        // Act
        Uri result = GalleryHandler.handleGalleryResult(requestCode, resultCode, data);

        // Assert
        assertNull(result);
    }

    private Activity getActivity() {
        final Activity[] activity = new Activity[1];
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity1 -> activity[0] = activity1);
        return activity[0];
    }
}
