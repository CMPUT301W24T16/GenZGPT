package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.EditProfileFragment;
import com.example.genzgpt.Model.User;
import static com.example.genzgpt.Model.appUser.user_email;
import com.example.genzgpt.R;

/**
 * A simple {@link Fragment} subclass.
 * Serves as a display for a User's Profile
 * Use the {@link UserProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    private Button editButton;
    private TextView userBanner;
    private ImageView userPicture;
    private TextView userFirstName;
    private TextView userLastName;
    private TextView userPhoneNumber;
    private TextView userEmail;
    private TextView userTheme;
    private TextView userGeolocation;
    private Firebase firebase;

    public UserProfileFragment() {
        // Required empty public constructor

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebase = new Firebase();
        User user = firebase.getUserData("dvtaylor@ualberta.ca");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //Initialize all variables
        editButton = view.findViewById(R.id.edit_profile_button);
        userBanner = view.findViewById(R.id.profile_header);
        userPicture = view.findViewById(R.id.profile_picture);
        userFirstName = view.findViewById(R.id.first_name_text);
        userLastName = view.findViewById(R.id.last_name_text);
        userPhoneNumber = view.findViewById(R.id.phone_number_text);
        userEmail = view.findViewById(R.id.email_text);
        userTheme = view.findViewById(R.id.theme_text);
        userGeolocation = view.findViewById(R.id.geolocation_text);
            Bind(user);

      /*  editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditProfileFragment().show(getParentFragmentManager(), "Edit Profile");
            }
        });*/
        return view;
    }
    public void Bind(User user){
        userFirstName.setText(user.getFirstName());
        userLastName.setText(user.getLastName());
        //userPhoneNumber.setText(user.getPhone());
        userEmail.setText(user.getEmail());
        if (user.isGeolocation()){
            userGeolocation.setText("ON");
        }else{
            userGeolocation.setText("OFF");
        }

    }
}