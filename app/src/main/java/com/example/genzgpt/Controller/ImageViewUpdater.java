package com.example.genzgpt.Controller;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

public class ImageViewUpdater {
    public static void updateImageView(Activity activity, Uri selectedImageUri, ImageView imageView) {
        if (selectedImageUri != null) {
            imageView.setImageURI(selectedImageUri);
        }
    }
}
