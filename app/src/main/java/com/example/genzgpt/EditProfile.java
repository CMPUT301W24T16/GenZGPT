package com.example.genzgpt;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button changeProfilePicButton;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    String storagePermission[];
    Uri imageuri;
    String profileOrCoverPhoto;


    //    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profile_picture_image);
        changeProfilePicButton = findViewById(R.id.image_action_button);

        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(v);
            }
        });
    }

    private void showDropdownMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, popupMenu.getMenu());

        // Assuming R.drawable.default_profile is the resource ID of your default image
        int defaultImageResource = R.drawable.default_profile_image;

        // Check if the current image is the default image
        boolean isDefaultImage = (imageUri == null) || (imageUri.equals(Uri.parse("android.resource://" + getPackageName() + "/" + defaultImageResource)));

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
                    openGallery();
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

    private void openCamera() {
        //this//;
    }


    private void openGallery() {
        // Ask user permission first before accessing gallery
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image
        }
    }
    private void deleteProfilePicture() {
        profileImageView.setImageResource(R.drawable.default_profile_image);
    }
}