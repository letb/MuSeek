package com.letb.museek.Entities;

/**
 * Created by eugene on 13.12.15.
 */
public final class TokenHolder {
    private static String accessToken;

    private static Integer expiresIn;

    public static Integer getExpiresIn() {
        return expiresIn;
    }

    public static void setExpiresIn(Integer expiresIn) {
        TokenHolder.expiresIn = expiresIn;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TokenHolder.accessToken = accessToken;
    }
}
