package com.example.genzgpt;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button changeProfilePicButton;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final long MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profile_picture_image);
        changeProfilePicButton = findViewById(R.id.image_action_button);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(v);
            }
        });
    }

    private void showDropdownMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

        // Check if the current image is the default image
        boolean isDefaultImage = isDefaultImage();

        // Get the "Delete Profile" menu item
        MenuItem deleteProfileMenuItem = popupMenu.getMenu().findItem(R.id.menu_deleteProfile);

        // Set the visibility based on whether the current image is the default image
        deleteProfileMenuItem.setVisible(!isDefaultImage);

        popupMenu.show();

        // Handle item clicks if needed
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_addCamera) {
                    openCamera();
                    return true;
                } else if (itemId == R.id.menu_addGallery) {
                    checkAndRequestGalleryPermissions();
                    return true;
                } else if (itemId == R.id.menu_deleteProfile) {
                    deleteProfilePicture();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private boolean isDefaultImage() {
        // Implement the logic to check if the current image is the default image
        // Return true if it's the default image, false otherwise
        String defaultImageResourceName = "default_profile_image";
        int defaultImageResource = getResources().getIdentifier(defaultImageResourceName, "drawable", getPackageName());

        // Check if the current image URI is null or matches the default image URI
        return imageUri == null || imageUri.equals(Uri.parse("android.resource://" + getPackageName() + "/" + defaultImageResource));    }

    private void openCamera() {
        //this//;
    }
    private void deleteProfilePicture() {
        if (!isDefaultImage()) {
            // Revert to the default image (change this line according to your needs)
            imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.default_profile_image);
            profileImageView.setImageURI(imageUri);
            // Update your UI or perform any additional actions
            // For example, you might want to set the new image to your ImageView
            // profileImageView.setImageURI(imageUri);
            showToast("Profile picture reverted to default");
        } else {
            showToast("No profile picture to delete");
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                // Check image size
                long imageSize = getImageSize(imageUri);
                if (imageSize <= MAX_IMAGE_SIZE_BYTES) {
                    // Image is within the size limit, set the image to ImageView or process as needed
                    profileImageView.setImageURI(imageUri);
                    uploadImageToFirebaseStorage();
                } else {
                    // Image size exceeds the limit, show an error message
                    showToast("Image size exceeds the limit of 10 MB");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToast("Error reading image file");
            }
        }
    }

    private long getImageSize(Uri uri) throws IOException {
        // Resolve the image size from the content resolver
        return getContentResolver().openInputStream(uri).available();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkAndRequestGalleryPermissions() {
        // Check if the app has the READ_EXTERNAL_STORAGE permission
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, open the gallery
            openGallery();
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Gallery permission granted, open the gallery
                openGallery();
            } else {
                // Gallery permission denied, handle accordingly (e.g., show a message)
                showToast("Gallery permission denied. Cannot open the gallery.");
            }
        }
    }

    private void uploadImageToFirebaseStorage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + randomKey);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image successfully uploaded
                        pd.dismiss();
                        showToast("Profile picture uploaded to Firebase Storage");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        showToast("Failed to get download URL: " + e.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }
}