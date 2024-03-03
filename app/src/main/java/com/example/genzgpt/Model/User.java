package com.example.genzgpt.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure of a User
 * Holds the info for a specific user such as name, id, email
 * and their roles such as Organizer/Attendee
 */
public class User {
    private String id;
    private String name;
    private String email;
    private List<Role> roles; // Organizer/Attendee

    // Constructor
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = new ArrayList<>();
    }

    // Method to add role to the user
    public void addRole(Role role) {
        roles.add(role);
    }

    // Method to check if the user has a specific role
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    // Getters
    public String getId() {
        return id;}
    public String getName() {
        return name;}
    public String getEmail() {
        return email;}

    // Setters
    public void setId(String id) {
        this.id = id;}
    public void setName(String name) {
        this.name = name;}
    public void setEmail(String email) {
        this.email = email;}

    public void login() {
        // Login implementation
    }

    public void logout() {
        // Logout implementation
    }
}