package com.example.genzgpt.Model;


import com.example.genzgpt.Model.Role;


import java.util.ArrayList;
import java.util.List;

/**
 * Structure of a User
 * Holds the info for a specific user such as name, id, email
 * and their roles such as Organizer/Attendee
 */
public class User {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private long phone;
    private boolean geolocation;
    private List<Role> roles; // Organizer/Attendee
    private String imageURL;
    private int checkInCount;
    private String token;

    /**
     * The constructor for a User.
     *
     * @param id
     * The User's unique identifier.
     *
     * @param firstName
     * The First Name of the User.
     *
     * @param lastName
     * The Last Name of the User.
     *
     * @param phone
     * The Phone Number of the User.
     *
     * @param email
     * The Email Address of the User.
     *
     * @param geolocation
     * Whether or not the User wants geolocation tracking enabled.
     */
    public User(String id, String firstName, String lastName, long phone, String email, boolean geolocation, String imageURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.roles = new ArrayList<>();
        this.geolocation = geolocation;
        this.imageURL = imageURL;
    }
    public User(String id, String firstName, String lastName, long phone, String email, boolean geolocation, String imageURL,String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.roles = new ArrayList<>();
        this.geolocation = geolocation;
        this.imageURL = imageURL;
        this.token = token;
    }

    /**
     * An alternate constructor for a User. Allows for a user without an ID to be created.
     *
     * @param firstName
     * The first name of the User
     *
     * @param lastName
     * The last name of the User
     *
     * @param phone
     * The phone number of the User
     *
     * @param email
     * The email of the User
     *
     * @param geolocation
     * Whether or not the User wants geolocation enabled
     */
    public User(String firstName, String lastName, long phone, String email, boolean geolocation, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.roles = new ArrayList<>();
        this.geolocation = geolocation;

    }

    /**
     * Adds a role to a User.
     *
     * @param role
     * The role to add to the User.
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    /**
     * Checks if the User has a given Role.
     *
     * @param roleName
     * The role ot check if a User has.
     *
     * @return
     * True if the User has roleName. False otherwise.
     */
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }


    /**
     * A getter for the unique ID of the user.
     *
     * @return
     * The User's ID
     */
    public String getId() {
        return id;}

    /**
     * A getter for the first name of the user.
     *
     * @return
     * The first name of the user.
     */
    public String getFirstName() {
        return firstName;}

    /**
     * A getter for the Email of the User
     *
     * @return
     * The User's email address.
     */
    public String getEmail() {
        return email;}

    /**
     * A getter for the last name of the User.
     *
     * @return
     * The last name of the User.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * A getter for the phone number of the User.
     *
     * @return
     * The User's phone number
     */
    public long getPhone() {
        return phone;
    }

    /**
     * A getter for whether or not this user has geolcation tracking enabled.
     *
     * @return
     * True if geolocation tracking is enabled. False otherwise.
     */
    public boolean isGeolocation() {
        return geolocation;
    }

    public int getCheckInCount() {
        return checkInCount;
    }

    // Setters

    /**
     * Sets the id of a User.
     * @param id
     * The ID to set for a User.
     */
    public void setId(String id) {
        this.id = id;}

    /**
     * Sets the First Name of the User
     * @param firstName
     */
    public void setName(String firstName) {
        this.firstName = firstName;}

    /**
     * A setter for the email address of the User.
     *
     * @param email
     * The email to set for the User.
     */
    public void setEmail(String email) {
        this.email = email;}

    /**
     * A setter for the last name of the User.
     *
     * @param lastName
     * The last name to set for the User.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * A setter for the phone number of the User.
     *
     * @param phone
     * The phone number to set for the User.
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * A setter for whether or not geolocation is enabled.
     *
     * @param geolocation
     * The geoloaction tracking to enable for the User.
     */
    public void setGeolocation(boolean geolocation) {
        this.geolocation = geolocation;
    }

    public void setCheckInCount(int checkInCount) {
        this.checkInCount = checkInCount;
    }

    /**
     * The one time login for the user.
     */
    public void login() {
        // Login implementation
    }

    /**
     * A logout for the user.
     */
    public void logout() {
        // Logout implementation
    }

    /**
     * A getter for the imageURL of a User.
     * @return
     * The current ImageURL of the user.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * A setter for the imageURL of a User.
     * @param imageURL
     * The imageURL to set.
     */
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
    public String getToken(){
        return this.token;
    }
    public void setToken(String token){
        this.token = token;
    }

}
