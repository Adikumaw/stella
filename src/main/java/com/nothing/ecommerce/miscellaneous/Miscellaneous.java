package com.nothing.ecommerce.miscellaneous;

import java.util.regex.Pattern;

public class Miscellaneous {

    public static boolean containsNonNumeric(String value) {
        // Regular expression to match any character that is not a digit
        Pattern pattern = Pattern.compile("\\D");

        // Check if the string contains any non-numeric character
        return pattern.matcher(value).find();
    }
}
