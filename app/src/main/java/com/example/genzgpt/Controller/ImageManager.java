package com.example.genzgpt.Controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageManager {

    public static void openCamera(Activity activity, int requestCode) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create a temporary file to store the captured image
            File photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(activity,
                        activity.getApplicationContext().getPackageName() + ".provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                // Start the camera activity
                activity.startActivityForResult(cameraIntent, requestCode);
            }
        }
    }

    public static void deleteImage(Uri imageUri, ContentResolver contentResolver, ImageView profileImageView) {
        if (imageUri != null) {
            // Delete the image file from the device
            int rowsDeleted = contentResolver.delete(imageUri, null, null);

            if (rowsDeleted > 0) {
                // Image deleted successfully
                showToast(profileImageView.getContext(), "Image deleted successfully");
                // Optionally, you can update the ImageView or perform any other UI update
                // For example: profileImageView.setImageResource(R.drawable.default_image);
            } else {
                // Failed to delete image
                showToast(profileImageView.getContext(), "Failed to delete image");
            }
        }
    }

    public static void openGallery(Activity activity, int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        activity.startActivityForResult(galleryIntent, requestCode);
    }

    public static long getImageSize(Uri uri, ContentResolver contentResolver) {
        try {
            // Resolve the image size from the content resolver
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream != null) {
                long size = inputStream.available();
                inputStream.close();
                return size;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if size retrieval fails
    }

    // Only needed for EditProfilePic
    public static boolean isDefaultImage(Uri imageUri, int defaultImageResource, Resources resources, String packageName) {
        // Implement the logic to check if the current image is the default image
        // Return true if it's the default image, false otherwise
        if (imageUri == null) {
            return true;
        }

        // Check if the current image URI matches the default image URI
        String defaultImageResourceName = "default_profile_image";
        int defaultImageResourceIdentifier = resources.getIdentifier(defaultImageResourceName, "drawable", packageName);
        Uri defaultImageUri = Uri.parse("android.resource://" + packageName + "/" + defaultImageResourceIdentifier);

        return imageUri.equals(defaultImageUri);
    }

    private static File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
