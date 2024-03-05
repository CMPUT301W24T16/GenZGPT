package com.example.genzgpt.Model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.genzgpt.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This class handles QR codes by scanning and generating QR codes.
 */
public class QR_Code extends Activity {
    private String code;
    private int LeadingDigit;
    private Boolean SignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize
        // setContentView(R.layout.your_layout);
    }

    /**
     * Initiates the QR code scanner using the ZXing library.
     */
    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);  // Enable or disable beep on scan
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /**
     * Handles the result of the QR code scanner.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The Intent data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                code = result.getContents();
                // Handle the scanned QR code data
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Method to generate a QR Code bitmap
    private Bitmap generateQRCodeImage(String text) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512); // Width and height in pixels
        Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

        for (int x = 0; x < 512; x++) {
            for (int y = 0; y < 512; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    /**
     * Example method to handle QR code generation and display.
     *
     * @param data The data to be encoded in the QR code.
     */
    public void createAndDisplayQRCode(String data) {
        try {
            Bitmap qrCodeBitmap = generateQRCodeImage(data);
            ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overrides the onPointerCaptureChanged method.
     *
     * @param hasCapture The capture status.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}


