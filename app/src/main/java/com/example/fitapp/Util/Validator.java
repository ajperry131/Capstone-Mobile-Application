package com.example.fitapp.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isValidUsername(String username) {
        //must be 6-20 characters long
        //must have no spaces
        //must have no symbols
        //underscores are allowed
        //a-z A-Z 0-9 allowed
        Pattern pattern = Pattern.compile("^[A-Za-z]\\w{5,19}$");
        Matcher matcher = pattern.matcher(username);

        return matcher.find();
    }

    public static boolean isValidPassword(String password) {
        //must be 8-30 characters long
        //underscores are allowed
        //must contain atleast one !@#$%^&-+=()
        //must contain atleast one 0-9
        //must contain atleast one a-z
        //must contain atleast one A-Z
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&\\-+=()])(?=\\S+$).{8,30}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static boolean isValidPhone(String phone) {
        //US phone number with or without dashes/parenthesis
        Pattern pattern = Pattern.compile("^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.find();
    }
}