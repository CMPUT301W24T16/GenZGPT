package com.example.genzgpt.Controller;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

/**
 * A way to update ImageViews.
 */
public class ImageViewUpdater {
    /**
     * Sets the imageURI of an ImageView
     * @param activity
     * The activity of the ImageView to set
     * @param selectedImageUri
     * The URI to set
     * @param imageView
     * The imageview to set the URI for
     */
    public static void updateImageView(Activity activity, Uri selectedImageUri, ImageView imageView) {
        if (selectedImageUri != null) {
            imageView.setImageURI(selectedImageUri);
        }
    }
}
