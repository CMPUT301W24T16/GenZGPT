package com.example.genzgpt.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Controller.FirebaseMessages;
import com.example.genzgpt.Controller.GeolocationTracking;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.R;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.example.genzgpt.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment for the QR Code Scanner used in the app.
 */
public class QRCodeFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private String qrCodeResult;
    private Firebase firebase;
    private Event loadedEvent;
    private GeolocationTracking geolocationTracking;

    /**
     * Handles the creation of the QRCodeFragment from within the app.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * A View for the QRCode Fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        firebase = new Firebase();
        if (!checkPermissions()) {
            requestPermissions();
        }
        view = inflater.inflate(R.layout.qr_activity, container, false);
        initiateQrScan();

        return view;
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void initiateQrScan() {
        IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .initiateScan();
    }

    /**
     * This method is called when the QR code scanner activity returns a result.
     * It will handle the result and take the appropriate action.
     * leading 0 sends to eventinfo without checking in
     * leading 1 sends to eventinfo and checks in
     *
     * @param requestCode The request code for the activity.
     * @param resultCode The result code for the activity.
     * @param data The intent data returned by the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("QRCodeFragment", "scanned QR code");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String eventCode;
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("QRCodeFragment", "Cancelled scan");
            } else {
                qrCodeResult = result.getContents();
                Log.d("QRCodeFragment", "Scanned QR code: " + qrCodeResult);
                if (qrCodeResult.startsWith("0") || qrCodeResult.startsWith("1")) {
                    eventCode = qrCodeResult.substring(1);
                    Log.d("QRCodeFragment", "Event code: " + eventCode);
                    firebase.getEventDataById(eventCode, new Firebase.OnEventLoadedListener() {
                        @Override
                        public void onEventLoaded(Event event) {
                            Log.d("QRCodeFragment", "Event loaded: " + event.getEventName());
                            loadedEvent = event;
                            if (qrCodeResult.startsWith("1")) {
                                // Check the user in to the event
                                checkInUser(eventCode);
                                geolocationTracking = new GeolocationTracking(event);
                                GeoPoint location = geolocationTracking.getDeviceLocation();
                                Log.d("GeoTest", location.toString());
                                firebase.addLocationToEvent(event.getEventId(), location);
                            }
                            handleEventLoaded();
                        }

                        @Override
                        public void onEventNotFound() {
                            // Handle the case when the event is not found
                            Log.d("QRCodeFragment", "Event not found");
                            handleEventNotFound();
                        }

                        @Override
                        public void onEventLoadFailed(Exception e) {
                            // Handle the case when loading the event data fails
                            Log.e("QRCodeFragment", "Failed to load event data", e);
                            handleEventLoadFailed();
                        }
                    });
                } else {
                    Log.d("QRCodeFragment", "Invalid QR code format");
                    // Show an error message or perform appropriate action
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleEventLoaded() {
        if (loadedEvent != null) {
            replaceFragment(new EventInfoFragment(loadedEvent));
        }
    }

    private void handleEventNotFound() {
        // Perform actions when the event is not found
    }

    private void handleEventLoadFailed() {
        // Perform actions when loading the event data fails
    }

    private void replaceFragment(Fragment fragment) {
        try {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.BaseFragment, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("QRCodeFragment", "Failed to replace fragment", e);
            // Show an error message or perform appropriate action
        }
    }

    private void checkInUser(String substring) {
        // Check in the user to the event
        FirebaseMessages fms = new FirebaseMessages(this.getContext());
        firebase.addUserToCheckedInAttendees(substring, AppUser.getUserId());
        List<String> organizerList = loadedEvent.getOrganizers();
        for(String organizer : organizerList) {
            firebase.getUserDataAndToken(organizer, new Firebase.OnUserLoadedListener(){
                @Override
                public void onUserLoaded(User user){
                    firebase.getUserData(AppUser.getUserId(),new Firebase.OnUserLoadedListener(){
                        @Override
                        public void onUserLoaded(User user2){
                            fms.sendMessageToDevice(user.getToken(), "New Checkin", user2.getFirstName() + " " + user2.getLastName()+" just checked-in","message");
                        }
                        @Override
                        public void onUserNotFound(){
                            Log.d("QRCodeFragment","UserNotFound");
                        }
                        @Override
                        public void onUserLoadFailed(Exception e){
                            Log.e("QRCodeFragment","error on Milestone");

                        };
                    });
                }
                @Override
                public void onUserNotFound(){
                    Log.d("QRCodeFragment","UserNotFound");
                }
                @Override
                public void onUserLoadFailed(Exception e){
                    Log.e("QRCodeFragment","error on Milestone");

                };
            });
        }
    }
}