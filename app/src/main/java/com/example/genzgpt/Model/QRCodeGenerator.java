package com.example.genzgpt.Model;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    /**
     * Generates a QR code Bitmap for checking in. The QR code content will start with "1" indicating
     * a check-in code, followed by the event ID.
     *
     * @param eventId The ID of the event to encode in the QR code.
     * @param width   The desired width of the QR code image.
     * @param height  The desired height of the QR code image.
     * @return A Bitmap representing the QR code, or null if an error occurs.
     */
    public static Bitmap generateCheckInQRCode(String eventId, int width, int height) {
        String qrCodeContent = "1" + eventId; // Prepend "1" for check-in QR codes
        return createQRCodeBitmap(qrCodeContent, width, height);
    }

    /**
     * Generates a QR code Bitmap for signing up. The QR code content will start with "0" indicating
     * a sign-up code, followed by the event ID.
     *
     * @param eventId The ID of the event to encode in the QR code.
     * @param width   The desired width of the QR code image.
     * @param height  The desired height of the QR code image.
     * @return A Bitmap representing the QR code, or null if an error occurs.
     */
    public static Bitmap generateSignUpQRCode(String eventId, int width, int height) {
        String qrCodeContent = "0" + eventId; // Prepend "0" for sign-up QR codes
        return createQRCodeBitmap(qrCodeContent, width, height);
    }

    /**
     * Helper method to create a QR code bitmap from the given content string.
     *
     * @param content The content to be encoded in the QR code.
     * @param width   The width of the QR code image.
     * @param height  The height of the QR code image.
     * @return A bitmap of the QR code or null if an error occurs during generation.
     */
    private static Bitmap createQRCodeBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}

/**
 * How to use:
 * // set the ImageView to display the QR code
 * ImageView checkInImageView = findViewById(R.id.checkInImageView);
 *
 * // Generate the QR code for an eventId
 * Bitmap checkInQrCode = QRCodeGenerator.generateCheckInQRCode(eventId, 200, 200);
 *
 * // Set the generated QR code bitmap to the ImageView
 * checkInImageView.setImageBitmap(checkInQrCode);
 */