package com.letb.museek.Requests;

import com.google.gson.JsonElement;
import com.letb.museek.Models.Track.Track;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by dannie on 22.12.15.
 */
public interface TrackUrlInterface {

    @FormUrlEncoded
    @POST("/index.php")
    JsonElement getTrackUrl(@Field("access_token") String token,
                      @Field("method") String method,
                      @Field("track_id") String trackId,
                      @Field("reason") String reason);
}
