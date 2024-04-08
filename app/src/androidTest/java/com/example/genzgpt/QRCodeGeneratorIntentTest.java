package com.example.genzgpt;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.graphics.Bitmap;

import com.example.genzgpt.Model.QRCodeGenerator;

import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.Test;

/**
 * Test Cases for QRCodeGenerator class.
 */
public class QRCodeGeneratorIntentTest {

    @Test
    public void testQRCodeCreation() {
        // Confirm we actually generate a QR Code
        Bitmap bitmap = QRCodeGenerator.createQRCodeBitmap("Event", 100, 100);
        assertNotNull(bitmap);

        bitmap = QRCodeGenerator.createQRCodeBitmap("Event1", 10, 10);
        assertNotNull(bitmap);
    }

    @Test
    public void testSignUpCreation() {
        // Confirm we actually generate a SignUp QR Code
        Bitmap bitmap = QRCodeGenerator.generateSignUpQRCode("Event", 100, 100);
        assertNotNull(bitmap);

        bitmap = QRCodeGenerator.generateSignUpQRCode("Event2", 1000, 1000);
        assertNotNull(bitmap);
    }

    @Test
    public void testCheckInCreation() {
        // Confirm we actually generate a CheckIn QR Code
        Bitmap bitmap = QRCodeGenerator.generateCheckInQRCode("Event", 300, 300);
        assertNotNull(bitmap);

        bitmap = QRCodeGenerator.generateSignUpQRCode("Event3", 5000, 5000);
        assertNotNull(bitmap);
    }

    @Test
    public void testUniquenessOfSignUpAndCheckIn() {
        // Confirm a checkInQRCode is not the same as a SignUPQRCode
        Bitmap bitmap = QRCodeGenerator.generateCheckInQRCode("EVENTID", 360, 360);
        Bitmap bitmap2 = QRCodeGenerator.generateSignUpQRCode("EVENTID", 360, 360);

        assertNotEquals(bitmap, bitmap2);

        bitmap = QRCodeGenerator.generateSignUpQRCode("NEWEVENTID", 200, 200);
        bitmap2 = QRCodeGenerator.generateCheckInQRCode("NEWEVENTID", 200, 200);

        assertNotEquals(bitmap, bitmap2);
    }
}
