package com.example.genzgpt.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Handles photo gallery usage within the app.
 */
public class GalleryHandler {
    public static final int REQUEST_PICK_IMAGE = 1;
    public static final int REQUEST_PERMISSION = 2;

    /**
     * Opens the photo gallery.
     * @param activity
     * The current activity to open the gallery from.
     */
    public static void openGallery(Activity activity) {
        if (hasPermission(activity)) {
            launchGallery(activity);
        } else {
            requestPermission(activity);
            launchGallery(activity);
        }
    }

    private static boolean hasPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private static void requestPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    private static void launchGallery(Activity activity) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE);
    }

    /**
     * Handles the result of getting an image from the gallery.
     * @param requestCode
     * A request code for the gallery (not currently used).
     * @param resultCode
     * The code representing the result of using the Gallery.
     * @param data
     * The data from the interaction with the Gallery.
     * @return
     * The data from the gallery or nothing if the result did not go correctly.
     */
    public static Uri handleGalleryResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            return data.getData();
        }
        return null;
    }
}
