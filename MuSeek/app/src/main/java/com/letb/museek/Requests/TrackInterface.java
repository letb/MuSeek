package com.letb.museek.Requests;

import com.letb.museek.Models.Track.Track;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


public interface TrackInterface {
    @FormUrlEncoded
    @POST("/index.php")
    Track getTrack(@Field("access_token") String token,
                           @Field("method") String method,
                           @Field("track_id") String trackId);
}
