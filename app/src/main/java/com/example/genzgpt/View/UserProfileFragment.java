package com.example.genzgpt.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
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
    private User userCurrent;

    /**
     * Empty required constructor
     */
    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Just a super to make the fragment show properly
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for the user profile
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebase = new Firebase();
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
        String appUser = AppUser.getUserId();
        firebase.getUserData(appUser, new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                Bind(user);
                userCurrent = user;
            }

            @Override
            public void onUserNotFound() {
                Log.d("Firebase", "User not found.");
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.e("Firebase", "User retrieval failed.");
            }
        });
        if (userCurrent != null) {
            editButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Opens the dialog fragment for editing a user profile
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    new EditProfileFragment(userCurrent).show(getParentFragmentManager(), "Edit Profile");
                }
            });
            }
            return view;
        }

    /**
     * Gets the user data from the firebase
     * @param user
     */
    public void Bind(User user){
        userFirstName.setText(user.getFirstName());
        userLastName.setText(user.getLastName());
        userPhoneNumber.setText(String.valueOf(user.getPhone()));
        userEmail.setText(user.getEmail());
        userBanner.setText(user.getFirstName() + " " + user.getLastName());
        userTheme.setText("Black and White");
        if (user.isGeolocation() == Boolean.TRUE){
            userGeolocation.setText("ON");
        }
        if (user.isGeolocation() == Boolean.FALSE){
            userGeolocation.setText("OFF");
        }

    }
}