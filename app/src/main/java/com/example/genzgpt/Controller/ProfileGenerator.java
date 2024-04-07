package com.example.genzgpt.Controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Generates a deterministic profile picture based on a user's name.
 */
public class ProfileGenerator {
    // Set the width and the height, and config of a profile picture.
    private final int PROFILE_WIDTH = 360;
    private final int PROFILE_HEIGHT = 360;
    private final int CREATE_WIDTH = 5;
    private final int CREATE_HEIGHT = 5;
    private final Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;
    private final int bgColour = Color.rgb(240, 248, 250);
    private final int upscaling = 100;
    private final int downscaling = -50;

    /**
     * An empty constructor for the ProfileGenerator.
     */
    public ProfileGenerator() {

    }

    /**
     * Generates a profile picture based on the name provided.
     * @param firstName
     * The first name to generate with.
     * @param lastName
     * The last name to generate with.
     * @return
     * A deterministically generated profile picture based on the first and last name provided.
     */
    public Bitmap generateProfile(String firstName, String lastName) {
        // get colours and true false values
        int colour = generateColour(firstName, lastName);

        // char[] charLetters = {firstName.charAt(0), lastName.charAt(0)};
        // String letters = String.copyValueOf(charLetters);

        // Create an off white mutable bitmap
        Bitmap bitmap = Bitmap.createBitmap(CREATE_WIDTH, CREATE_HEIGHT, CONFIG);
        bitmap.eraseColor(bgColour);

        // 5x5: need 25 bits for this profile picture
        String nameBinary = getBinaryValue(firstName, lastName);
        nameBinary = extendBinaryValue(nameBinary);

        bitmap = setProfileImage(bitmap, colour, nameBinary);

        return bitmap;
    }

    /**
     * Given two strings, converts information from them into an rgb colour.
     * @param first
     * The first string to use for rgb conversion.
     * @param last
     * The last string to use for rgb conversion.
     * @return
     * An integer representing an rgb colour.
     */
    private int generateColour(String first, String last) {
        char extraChar;

        if (first.length() >= 2) {
            extraChar = first.toLowerCase().charAt(1);
        }
        else if (last.length() >= 2) {
            extraChar = last.toLowerCase().charAt(1);
        }
        else {
            extraChar = '}';
        }

        // create the r value for the colour (arbitrary method)
        int r = first.toLowerCase().charAt(0);
        if (r <= 109) {
            r += upscaling;
        }
        else {
            r += downscaling;
        }

        // create the g value for the colour (arbitrary method)
        int g = last.toLowerCase().charAt(0);
        if (g <= 109) {
            g += upscaling;
        }
        else {
            g += (upscaling/2);
        }

        // create the b value for the colour (arbitrary method)
        int b = (extraChar);
        if (b <= 109) {
            b += downscaling;
        }
        else {
            b += upscaling;
        }

        return Color.rgb(r,g,b);
    }

    /**
     * Creates a String representing the combined binary values of two strings.
     * @param first
     * The first String to get binary values from.
     * @param last
     * The last String to get binary values from.
     * @return
     * A String representing binary values.
     */
    private String getBinaryValue(String first, String last) {
        StringBuilder binary = new StringBuilder();

        // Concatenate the binary values for the hash codes of each character in first.
        for (int i = 0; i < first.length(); i++) {
            binary.append(Integer.toBinaryString((first.charAt(i))));
        }

        // Concatenate the binary values for the hash codes of each character in last.
        for (int i = 0; i < last.length(); i++) {
            binary.append(Integer.toBinaryString((last.charAt(i))));
        }

        // Return the desired hex value String.
        return binary.toString();
    }

    /**
     * Extends a binary value to 25 bits if it contains less than that.
     * @param binary
     * The current binary string to potentially extend.
     * @return
     * A binary string with at least 25 bit characters.
     */
    private String extendBinaryValue(String binary) {
        boolean extender = false;

        StringBuilder binaryBuilder = new StringBuilder(binary);

        // Only extend the binary String if necessary.
        while (binaryBuilder.length() < 25) {
            if (!extender) {
                binaryBuilder.append("0");
            }
            else {
                binaryBuilder.append("1");
            }
            extender = !extender;
        }

        return binaryBuilder.toString();
    }

    /**
     * Sets an identicon image inside of a bitmap.
     * @param bitmap
     * The bitmap to set an image inside.
     * @param colour
     * The colour to set the image with.
     * @param binary
     * The binary value we will use to help make the profile picture.
     * @return
     * The finished version of the bitmap.
     */
    private Bitmap setProfileImage(Bitmap bitmap, int colour, String binary) {
        // set the pixels in the bitmap
        bitmap = bitmap.copy(CONFIG, true);
        Log.d("BINARY ENCODING", binary);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (binary.charAt(x+(y*5)) == '0') {
                    bitmap.setPixel(x, y, colour);
                }
            }
        }

        // upscale the bitmap to the correct profile picture size
        bitmap = Bitmap.createScaledBitmap(bitmap, PROFILE_WIDTH, PROFILE_HEIGHT, false);
        return bitmap;
    }
}