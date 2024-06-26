package com.example.genzgpt.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

/**
 * This class handles camera and image related data.
 */
public class CameraHandler {
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private final Fragment fragment;

    public CameraHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * A constructor for situations where this class is not used in a fragment situation.
     */
    public CameraHandler() {
        fragment = null;
    }

    /**
     * Opens the camera for the device within the app.
     */
    public void openCamera() {
        if (fragment.getContext() != null && fragment.getActivity() != null) {
            if (ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{Manifest.permission.CAMERA},
                        REQUEST_IMAGE_CAPTURE);
            } else {
                // Permission granted, launch the camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Given a bitmap: create an image URI.
     * @param bitmap
     * The bitmap to create a URI for.
     * @return
     * The URI for the given bitmap.
     */
    public Uri getImageUri(Bitmap bitmap) {
        Context context = fragment.getContext();
        if (context != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    /**
     * An alternate ImageUri getter for non-fragment situations.
     * @param bitmap
     * The bitmap to get the Uri for.
     * @param context
     * The context of the app needed to invoke this method.
     * @return
     * The imageURI for a bitmap.
     */
    public Uri getImageUri(Bitmap bitmap, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                "Title", null);
        return Uri.parse(path);
    }
}
