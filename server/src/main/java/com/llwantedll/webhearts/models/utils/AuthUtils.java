package com.llwantedll.webhearts.models.utils;

import org.springframework.util.StringUtils;

import java.util.Base64;

public class AuthUtils {

    private static final String BASIC_AUTH = "Basic";

    public static String getNameFromHeader(String header){

        String authToken = getAuthToken(header);
        if (authToken == null) return null;
        return new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }

    public static String getPasscodeFromHeader(String header){

        String authToken = getAuthToken(header);
        if (authToken == null) return null;
        return new String(Base64.getDecoder().decode(authToken)).split(":")[1];
    }

    private static String getAuthToken(String header) {
        int prefixLength = BASIC_AUTH.length();

        if(StringUtils.isEmpty(header) || header.length() < prefixLength){
            return null;
        }

        String authToken = header.substring(prefixLength).trim();
        return authToken;
    }
}
