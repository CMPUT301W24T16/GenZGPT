package com.example.genzgpt.Model;

public class appUser extends User{
    public static String user_email;
    public appUser(String id, String firstName, String lastName, long phone, String email, Boolean geolocation) {
        super(id, firstName, lastName, phone, email, geolocation);
        user_email = email;
    }

}
