package com.example.genzgpt;

import static com.example.genzgpt.Controller.GalleryHandler.openGallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.example.genzgpt.Model.User;

/**
 * A Dialog window used for when a User wants to edit their profile information.
 */
public class EditProfileFragment extends DialogFragment {
    private String geolocationName;
    private User selectedUser;
    public EditProfileFragment(User user){
        this.selectedUser = user;
    }
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
        //editPhone.setText(selectedUser.getPhone());
        if (selectedUser.isGeolocation()){
            geolocationSwitch.setChecked(Boolean.TRUE);
        }
        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(getActivity());
            }
        });
        deleteProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUser.setImageURL("ic_launcher.xml");
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).setTitle("Edit Profile").setNegativeButton("Cancel", null).setPositiveButton("Save Changes", (dialog, which) ->{
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();
            String emailName = editEmail.getText().toString();
            //May need to update if it is not a String object
            String phoneNum = editPhone.getText().toString();
            SwitchCompat geoStatus = geolocationSwitch;
            if (geoStatus.isChecked()){
                geolocationName = "ON";
            }else{
                geolocationName = "OFF";
            }
        }).create();
    }
}
