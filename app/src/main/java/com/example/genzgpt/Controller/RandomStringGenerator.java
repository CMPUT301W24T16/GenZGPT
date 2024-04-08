package com.example.genzgpt.Controller;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/**
 * FIXME REMOVE THIS CLASS IT DOES NOTHING RIGHT NOW
 */
public class RandomStringGenerator {
    private static final String UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUMERIC = UPPER_CHARS + LOWER_CHARS + DIGITS;

    private static final Random random = new SecureRandom();

    /**
     * Generates a random string of the specified length using alphanumeric characters.
     * @param length The desired length of the random string.
     * @return The generated random string.
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            char randomChar = ALPHANUMERIC.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * Generates a random string of the specified length using the provided character set.
     * @param length The desired length of the random string.
     * @param charset The character set to use for generating the random string.
     * @return The generated random string.
     */
    public static String generateRandomString(int length, String charset) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charset.length());
            char randomChar = charset.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
