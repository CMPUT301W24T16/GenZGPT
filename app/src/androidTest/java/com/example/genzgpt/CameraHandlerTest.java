package com.example.genzgpt;

import static com.google.common.base.Verify.verify;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.example.genzgpt.Controller.CameraHandler;

// Subclass of Fragment for testing purposes
class TestFragment extends Fragment {
    // Implement any necessary constructors or methods for testing
}

@RunWith(AndroidJUnit4.class)
public class CameraHandlerTest {

    @Test
    public void testOpenCameraWithPermission() {
        // Arrange
        ActivityScenario.launch(MainActivity.class);
        TestFragment testFragment = new TestFragment();

        // Act
        CameraHandler cameraHandler = new CameraHandler(testFragment);
        cameraHandler.openCamera();
    }
}
