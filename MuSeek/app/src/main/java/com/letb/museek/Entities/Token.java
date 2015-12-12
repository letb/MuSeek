package com.letb.museek.Entities;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eugene on 06.12.15.
 */
public class Token {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Integer expiresIn;

    public static String authHTTPHeader = "Basic " + Base64.encodeToString(String.format("%s:%s", "823734", "111111").getBytes(), Base64.NO_WRAP);

    public String getAccessToken() {
        return accessToken;
    }
}