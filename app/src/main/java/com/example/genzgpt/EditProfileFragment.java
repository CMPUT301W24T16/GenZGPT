package com.example.genzgpt;

import static com.example.genzgpt.Controller.GalleryHandler.openGallery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;

/**
 * A Dialog window used for when a User wants to edit their profile information.
 */
public class EditProfileFragment extends DialogFragment {
    private String geolocationName;
    private User selectedUser;


    /**
     * Creates a profile fragment
     * @param user
     */
    public EditProfileFragment(User user){
        this.selectedUser = user;
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
        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            /**
             * Should open the gallery to edit profile picture
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                openGallery(getActivity());
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

            User new_user = new User(null, firstName, lastName, phone, emailName, geoBool, null);
            firebase.deleteUser(emailName);
            firebase.createUser(new_user);
                AppUser.setUserEmail(emailName);
        }).create();
    }
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
