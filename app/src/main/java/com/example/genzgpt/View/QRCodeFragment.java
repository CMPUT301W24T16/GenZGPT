package com.example.genzgpt.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.genzgpt.Controller.Firebase;
import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.R;
import com.example.genzgpt.View.EventInfoFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeFragment extends Fragment {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private String qrCodeResult;
    private Firebase firebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (checkPermissions()) {
            view = inflater.inflate(R.layout.qr_activity, container, false);
            initiateQrScan();
        } else {
            requestPermissions();
            view = inflater.inflate(R.layout.qr_activity, container, false);
        }

        return view;
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void initiateQrScan() {
        IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String eventCode;
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("QRCodeFragment", "Cancelled scan");
            } else {
                qrCodeResult = result.getContents();
                if (qrCodeResult.startsWith("0")) {
                    eventCode = qrCodeResult.substring(1);
                    firebase.getEventData(eventCode, new Firebase.OnEventLoadedListener() {
                        @Override
                        public void onEventLoaded(Event event) {
                            // Replace the current fragment with the EventInfoFragment
                            EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.BaseFragment, eventInfoFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onEventNotFound() {
                            // Handle the case when the event is not found
                            Log.d("QRCodeFragment", "Event not found");
                        }

                        @Override
                        public void onEventLoadFailed(Exception e) {
                            // Handle the case when loading the event data fails
                            Log.e("QRCodeFragment", "Failed to load event data", e);
                        }
                    });
                } else if (qrCodeResult.startsWith("1")) {
                    eventCode = qrCodeResult.substring(1);
                    firebase.getEventData(eventCode, new Firebase.OnEventLoadedListener() {
                        @Override
                        public void onEventLoaded(Event event) {
                            // Check the user in to the event
                            checkInUser(eventCode);
                            // Replace the current fragment with the EventInfoFragment
                            EventInfoFragment eventInfoFragment = new EventInfoFragment(event);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.BaseFragment, eventInfoFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }

                        @Override
                        public void onEventNotFound() {
                            // Handle the case when the event is not found
                            Log.d("QRCodeFragment", "Event not found");
                        }

                        @Override
                        public void onEventLoadFailed(Exception e) {
                            // Handle the case when loading the event data fails
                            Log.e("QRCodeFragment", "Failed to load event data", e);
                        }
                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkInUser(String substring) {
        // Check in the user to the event
        firebase.addUserToCheckedInAttendees(substring, AppUser.getAppUserEmail());
    }
}
