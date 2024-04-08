package com.nothing.ecommerce.miscellaneous;

import java.util.regex.Pattern;

public class Miscellaneous {

    public static boolean verifyEmail(String email) {
        // Regular expression to match any character that is not a digit
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");

        // Check if the string contains any non-numeric character
        return pattern.matcher(email).find();
    }

    public static boolean verifyMobileNumber(String number) {
        // Regular expression to match any character that is not a digit
        Pattern pattern = Pattern.compile("[0-9]{10}");

        // Check if the string contains any non-numeric character
        return pattern.matcher(number).find();
    }
}
