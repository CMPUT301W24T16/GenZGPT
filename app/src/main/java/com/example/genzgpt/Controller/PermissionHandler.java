package com.example.genzgpt.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.example.genzgpt.Controller.ImageManager;

/**
 * A class that controls permission usage within an app.
 */
public class PermissionHandler {

    /**
     * Checks with the current device that the app has permission to use its Gallery feature.
     *
     * @param activity
     * The Activity currently being accessed by the app.
     *
     * @param requestCode
     * An identifier for the request being made.
     */
    public static void checkAndRequestGalleryPermissions(Activity activity, int requestCode) {
        // Check if the app has the READ_EXTERNAL_STORAGE permission
        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode
            );
        } else {
            // Permission is already granted, open the gallery
            ImageManager.openGallery(activity, requestCode);
        }
    }

    /**
     * Checks with the current device that the app has permission to use its Camera feature.
     *
     * @param activity
     * The Activity currently being accessed by the app.
     *
     * @param requestCode
     * An identifier for the request being made.
     */
    public static void checkAndRequestCameraPermissions(Activity activity, int requestCode) {
        // Check if the app has the CAMERA permission
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode
            );
        } else {
            // Permission is already granted, open the camera
            ImageManager.openCamera(activity, requestCode);
        }
    }

    /**
     * FIXME
     * @param requestCode
     *
     * @param permissions
     *
     * @param grantResults
     *
     *
     * @param context
     * The current context of the application necessary to invoke this method.
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Context context) {
        if (requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Gallery or camera permission granted, open the corresponding feature
                if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ImageManager.openGallery((Activity) context, requestCode);
                } else if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    ImageManager.openCamera((Activity) context, requestCode);
                }
            } else {
                // Gallery or camera permission denied, handle accordingly (e.g., show a message)
                showToast(context, "Permission denied. Cannot access the requested feature.");
            }
        }
    }

    /**
     * Show a toast for specific information about what the PermissionHandler is doing.
     *
     * @param context
     * The current context of the app necessary to invoke this method.
     *
     * @param message
     * The message to be displayed in the Toast.
     */
    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
