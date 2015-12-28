package com.letb.museek.Requests;

import com.google.gson.JsonElement;

import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by eugene on 28.12.15.
 */
public interface ArtistInterface {
    @GET("/")
    JsonElement getInfo(@Query("method") String method,
                        @Query("artist") String artist,
                        @Query("api_key") String api_key,
                        @Query("format") String format);
}
