package com.llwantedll.webhearts.models.utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidationUtils {

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static boolean validate(String email) {

        if(Objects.isNull(email)){
            return false;
        }

        Matcher matcher = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }
}
