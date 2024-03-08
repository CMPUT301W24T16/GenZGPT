package com.example.genzgpt.Model;

/**
 * A class representing the User of the App on this device.
 */
public class AppUser extends User{
    private static String userEmail;

    /**
     * A constructor for the AppUser Class.
     * @param id
     * The id for the User of the App.
     *
     * @param firstName
     * The first name of the User of the App.
     *
     * @param lastName
     * The last name of the User of the App.
     *
     * @param phone
     * The phone number of the User of the App.
     *
     * @param email
     * The email of the User of the App.
     *
     * @param geolocation
     * A boolean indicating whether the user of the App allows for Geolocation.
     */
    public AppUser(String id, String firstName, String lastName, long phone, String email, Boolean geolocation) {
        super(id, firstName, lastName, phone, email, geolocation);
        userEmail = email;
    }

    /**
     * A static version of the getter for a User email. Used for getting the email as a Firebase
     * argument.
     *
     * @return
     * The email of the user currently using the app.
     */
    public static String getAppUserEmail() {
        return userEmail;
    }

    /**
     * A setter for the static version of the User's email.
     *
     * @param newEmail
     * The email to set the User Profile to.
     */
    public static void setUserEmail(String newEmail) {
        userEmail = newEmail;
    }

}
