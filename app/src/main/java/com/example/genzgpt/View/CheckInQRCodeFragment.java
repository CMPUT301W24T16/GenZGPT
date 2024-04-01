package com.example.genzgpt.View;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
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
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on back arrow click, typically go back
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
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
            }
        }
    }
}