package com.example.genzgpt.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.genzgpt.Controller.CameraHandler;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.GalleryHandler;
import com.example.genzgpt.Controller.ImageViewUpdater;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;

/**
 * A Dialog window used for when a User wants to edit their profile information.
 */
public class EditProfileFragment extends DialogFragment {
    private String geolocationName;
    private User selectedUser;
    private ActivityResultLauncher<String> mGetContent;
    ImageView profilePicture;
    Uri selectedImageUri;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private CameraHandler cameraHandler;

    /**
     * Creates a profile fragment
     * @param user
     */
    public EditProfileFragment(User user){
        this.selectedUser = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        ImageViewUpdater.updateImageView(getActivity(), result, profilePicture);
                        selectedImageUri = result;
                    }
                });
        cameraHandler = new CameraHandler(this);
    }

    /**
     * Creates a Dialog for editing the user profile
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Dialog object
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_profile, null);
        EditText editFirstName = view.findViewById(R.id.edit_first_name);
        EditText editLastName = view.findViewById(R.id.edit_last_name);
        EditText editEmail = view.findViewById(R.id.edit_email);
        EditText editPhone = view.findViewById(R.id.edit_phone_number);
        profilePicture = view.findViewById(R.id.profile_picture);
        Button editThemeButton = view.findViewById(R.id.edit_theme_button);
        Button editProfilePicture = view.findViewById(R.id.edit_profile_picture_button);
        Button deleteProfilePicture = view.findViewById(R.id.delete_profile_picture_button);
        SwitchCompat geolocationSwitch = view.findViewById(R.id.geolocation_switch);
        editFirstName.setText(selectedUser.getFirstName());
        editLastName.setText(selectedUser.getLastName());
        editEmail.setText(selectedUser.getEmail());
        editPhone.setText(String.valueOf(selectedUser.getPhone()));
        if (selectedUser.isGeolocation()){
            geolocationSwitch.setChecked(Boolean.TRUE);
        }
        if (selectedUser != null && selectedUser.getImageURL() != null) {
            Glide.with(requireContext())
                    .load(selectedUser.getImageURL()) // Load image URL
//                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
//                    .error(R.drawable.error_image) // Image to show in case of error
                    .into(profilePicture); // ImageView to load the image into
        }
        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            /**
             * Should open the gallery to edit profile picture
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.getMenuInflater().inflate(R.menu.edit_profile_picture_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_gallery) {
                            mGetContent.launch("image/*");
                            return true;
                        } else if (item.getItemId() == R.id.menu_camera) {
                            cameraHandler.openCamera();
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        deleteProfilePicture.setOnClickListener(new View.OnClickListener() {
            /**
             * should delete the profile picture from the user (will set to default photo)
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (selectedUser.getImageURL() == null) {
                    Toast.makeText(getContext(), "No image to delete", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if there's no image to delete
                }
                selectedUser.setImageURL(null);
                profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
                selectedImageUri = null;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Edit Profile").setNegativeButton("Cancel", null).setPositiveButton("Save Changes", (dialog, which) ->{
            Firebase firebase = new Firebase();
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();
            String emailName = editEmail.getText().toString();
            //May need to update if it is not a String object
            String phoneNum = editPhone.getText().toString();
            long phone = Long.parseLong(phoneNum);
            boolean geoBool = Boolean.FALSE;
            if (checkPermissions()) {
                geoBool = geolocationSwitch.isChecked();
            }
            User new_user = new User(selectedUser.getId(), firstName,lastName, phone, emailName, geoBool, selectedUser.getImageURL());
            firebase.updateUser(new_user, new Firebase.OnUserUpdatedListener() {
                @Override
                public void onUserUpdated() {
                    if (getContext() != null) {
                        Toast.makeText(getContext(),"Successfully updated user information!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onUserUpdateFailed(Exception e) {
                    Log.e("Firebase", "Error updating user information.");
                }
            });
            if (selectedImageUri != null) {
                // Upload the selected image
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                firebase.uploadImageForUser(selectedUser.getId(), selectedImageUri, progressDialog, getContext());
            }
        }).create();
    }
    private boolean checkPermissions() {
        if (getActivity() != null) {
            return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }else{
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                // Get the captured image
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // Do something with the image, like set it to ImageView or upload it
                if (imageBitmap != null) {
                    // Set the image bitmap to ImageView
                    profilePicture.setImageBitmap(imageBitmap);
                    // Convert bitmap to Uri
                    selectedImageUri = cameraHandler.getImageUri(imageBitmap);
                }
            }
        } else if (requestCode == GalleryHandler.REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Get the selected image URI from gallery
                selectedImageUri = data.getData();
                // Set the selected image to ImageView
                profilePicture.setImageURI(selectedImageUri);
            }
        }
    }
}
