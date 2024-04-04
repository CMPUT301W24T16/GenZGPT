package com.example.genzgpt.View;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.genzgpt.Model.Event;
import com.example.genzgpt.Model.QRCodeGenerator;
import com.example.genzgpt.R;

public class CheckInQRCodeFragment extends Fragment {

    private Event event;
    private ImageView qrCodeImageView;

    // Constructor that takes an Event object
    public CheckInQRCodeFragment(Event event) {
        this.event = event;
    }

    // Default constructor
    public CheckInQRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_in_qr_code, container, false);
        qrCodeImageView = view.findViewById(R.id.qr_code_image_view);
        ImageView backArrowImageView = view.findViewById(R.id.backArrowImageView);
        backArrowImageView.setOnClickListener(v -> {
            // Perform action on back arrow click, typically go back
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        displayCheckInQrCode();
        return view;
    }

    private void displayCheckInQrCode() {
        // Generate a QR code for Check-In and display it
        if (event != null) {
            Bitmap checkInQrCode = QRCodeGenerator.generateCheckInQRCode(event.getEventId(), 200, 200);
            if (checkInQrCode != null) {
                qrCodeImageView.setImageBitmap(checkInQrCode);
                qrCodeImageView.setOnClickListener(v -> showSaveQrCodeDialog());
            }
        }
    }
    private void showSaveQrCodeDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Save QR Code")
                .setMessage("Do you want to save this QR code to your device?")
                .setPositiveButton("Yes", (dialog, which) -> saveQrCodeToDevice())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create().show();
    }
    private void saveQrCodeToDevice() {
        if (qrCodeImageView.getDrawable() == null) {
            Toast.makeText(getContext(), "QR Code image is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert ImageView content to Bitmap
        Bitmap bitmap = ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap();

        // Use MediaStore to save the bitmap
        String savedImageURL = MediaStore.Images.Media.insertImage(
                requireContext().getContentResolver(),
                bitmap,
                event.getEventName() + " Check-in", // Use event name as part of the image title
                "Check-in QR Code for " + event.getEventName()); // Use event name as part of the image description

        if (savedImageURL == null) {
            Toast.makeText(getContext(), "Failed to save QR code", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "QR Code saved to Gallery", Toast.LENGTH_SHORT).show();
            Log.d("EventInfoFragment", "Image saved to: " + savedImageURL);
        }
    }
}