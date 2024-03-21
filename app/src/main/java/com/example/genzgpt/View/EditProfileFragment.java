package com.example.genzgpt.View;

import static com.example.genzgpt.Controller.GalleryHandler.openGallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.genzgpt.Controller.CameraFragment;
import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.GalleryHandler;
import com.example.genzgpt.Controller.ImageViewUpdater;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;

/**
 * A Dialog window used for when a User wants to edit their profile information.
 */
public class EditProfileFragment extends DialogFragment {
    private String geolocationName;
    private User selectedUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<String> mGetContent;
    ImageView profilePicture;
    Uri selectedImageUri;


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
                        profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        profilePicture.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
                        selectedImageUri = result;
                    }
                });
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
                            openCamera();
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
                selectedUser.setImageURL("ic_launcher.xml");
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

    private void openCamera() {
        CameraFragment cameraFragment = new CameraFragment();
        FragmentManager fragmentManager = getChildFragmentManager(); // Use getChildFragmentManager() for fragments within fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, cameraFragment); // Replace with the ID of the container in your EditProfileFragment layout
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData(); // Remove Uri declaration to avoid creating a new variable
                // Assuming you have the userID available
                String userID = selectedUser.getId();
                // Call Firebase method to upload image for user
                Firebase firebase = new Firebase();
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                firebase.uploadImageForUser(userID, selectedImageUri, progressDialog, getContext());
            }
        }
    }
}
