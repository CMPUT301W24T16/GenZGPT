package com.example.genzgpt;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.genzgpt.Controller.ProfileGenerator;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Cases for the ProfileGenerator Test.
 */
public class ProfileGeneratorTest {
    ProfileGenerator generator = new ProfileGenerator();

    @Test
    public void testProfileCreation() {
        // Test we actually get a profile bitmap
        String first = "User";
        String last = "Person";

        Bitmap bitmap = generator.generateProfile(first, last);

        assertNotNull(bitmap);
    }

    @Test
    public void testDeterministicGeneration() {
        // Test generation is same for same input and different for different inputs
        String first = "User";
        String last = "Person";

        Bitmap bitmap = generator.generateProfile(first, last);
        Bitmap bitmap1 = generator.generateProfile(first, last);

        assertTrue(bitmap1.sameAs(bitmap));

        first = "tb";
        last = "steal";

        bitmap = generator.generateProfile(first, last);
        assertFalse(bitmap1.sameAs(bitmap));
        bitmap1 = generator.generateProfile(first, last);

        assertTrue(bitmap1.sameAs(bitmap));

        first = "Zacharius";
        last = "West";

        bitmap = generator.generateProfile(first, last);
        assertFalse(bitmap1.sameAs(bitmap));
        bitmap1 = generator.generateProfile(first, last);

        assertTrue(bitmap1.sameAs(bitmap));

        first = "!xeowru2903flkasfd";
        last = "209i32b&&123";

        bitmap = generator.generateProfile(first, last);
        assertFalse(bitmap1.sameAs(bitmap));
        bitmap1 = generator.generateProfile(first, last);

        assertTrue(bitmap1.sameAs(bitmap));
    }

    @Test
    public void testTwoColours() {
        // Test that two colours are always present in the bitmap
        HashMap<Color, Boolean> colourChecker = new HashMap<Color, Boolean>();
        int colourCounter = 0;
        String first = "User";
        String last = "Person";

        Bitmap bitmap = generator.generateProfile(first, last);

        for (int i = 0; i < 360; i++) {
            for (int j = 0; j < 360; j++) {
                if (!colourChecker.containsKey(bitmap.getColor(i, j))) {
                    colourChecker.put(bitmap.getColor(i, j), true);
                    colourCounter++;
                }
            }
        }

        assertEquals(colourCounter, 2);

        first = "tb";
        last = "steal";
        colourChecker.clear();
        colourCounter = 0;

        bitmap = generator.generateProfile(first, last);

        for (int i = 0; i < 360; i++) {
            for (int j = 0; j < 360; j++) {
                if (!colourChecker.containsKey(bitmap.getColor(i, j))) {
                    colourChecker.put(bitmap.getColor(i, j), true);
                    colourCounter++;
                }
            }
        }

        assertEquals(colourCounter, 2);
    }
}
