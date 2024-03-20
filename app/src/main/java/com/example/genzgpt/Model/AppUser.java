package com.example.genzgpt.Model;

/**
 * A class representing the User of the App on this device.
 */
public class AppUser extends User{
    private static String userId;
    private static String userEmail;
    private static boolean hasSignedIn;

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
    public AppUser(String id, String firstName, String lastName, long phone, String email, Boolean geolocation, String imageURL) {
        super(id, firstName, lastName, phone, email, geolocation, imageURL);
    }

    /**
     * An alternate constructor for the AppUser that does not require an ID
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
    public AppUser(String firstName, String lastName, long phone, String email, Boolean geolocation, String imageURL) {
        super(firstName, lastName, phone, email, geolocation, imageURL);
    }

    /**
     * A getter for the static version of the User's Id
     * @return
     * The Id for the current user.
     */
    public static String getUserId() {
        return userId;
    }

    /**
     * A setter for the Id of the app user (used on first sign in)
     * @param _userId
     * The Id we will be assigning the app user.
     */
    public static void setUserId(String _userId) {
        userId = _userId;
    }

    public static boolean getHasSignedIn() {
        return hasSignedIn;
    }

    public static void setHasSignedIn(boolean status) {
        hasSignedIn = status;
    }

    //FIXME THIS SHOULD NOT BE NECESSARY ANYMORE
    public static void setUserEmail(String _userEmail) {
        userEmail = _userEmail;
    }

    //FIXME THIS SHOULD NOT BE NECESSARY ANYMORE
    public static String getAppUserEmail() {
        return userEmail;
    }
}
