package com.letb.museek.Entities;

/**
 * Created by eugene on 13.12.15.
 */
public final class TokenHolder {
    private static String accessToken;

    private static Integer expiresIn;

    public static void setData (String accessToken, Integer expiresIn) {
        TokenHolder.accessToken = accessToken;
        TokenHolder.expiresIn = expiresIn;
    }

    public static Integer getExpiresIn() {
        return TokenHolder.expiresIn;
    }

    public static void setExpiresIn(Integer expiresIn) {
        TokenHolder.expiresIn = expiresIn;
    }

    public static String getAccessToken() {
        return TokenHolder.accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TokenHolder.accessToken = accessToken;
    }
}
