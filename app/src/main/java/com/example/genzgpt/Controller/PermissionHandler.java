package com.example.genzgpt.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.example.genzgpt.Controller.ImageManager;

public class PermissionHandler {
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

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
