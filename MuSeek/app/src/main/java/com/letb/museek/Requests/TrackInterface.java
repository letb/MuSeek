package com.letb.museek.Requests;

import com.google.gson.JsonElement;
import com.letb.museek.Models.Track.Track;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;


public interface TrackInterface {
    @FormUrlEncoded
    @POST("/index.php")
    Track getTrack(@Field("access_token") String token,
                   @Field("method") String method,
                   @Field("track_id") String trackId);

//    Метод tracks_get_download_link — получить ссылку на загрузку
//
//    Параметры:
//
//    track_id (string, обязательный) идентификатор трека
//    reason (string, обязательный) цель обращения. Должна быть равна listen (загрузка для прослушивания)
//      или save (загрузка для сохранения)
//    Возвращаемое значение: URL на файл mp3.

    @FormUrlEncoded
    @POST("/index.php")
    Track getTrackUrl(@Field("access_token") String token,
                      @Field("method") String method,
                      @Field("track_id") String trackId,
                      @Field("reason") String reason);

    /**
     *
     * @param method track.getInfo
     * @param artist artist (Required (unless mbid)] : The artist name
     * @param track track (Required (unless mbid)] : The track name
     * @param api_key api_key (Required) : A Last.fm API key.
     * @param autocorrect Transform misspelled artist and track names into correct artist and track names,
     *                    returning the correct version instead.
     *                    The corrected artist and track name will be returned in the response.
     * @param format JSON
     * @return
     */
    @GET("/")
    JsonElement getInfo(@Query("method") String method,
                        @Query("artist") String artist,
                        @Query("track") String track,
                        @Query("api_key") String api_key,
                        @Query("autocorrect") Boolean autocorrect,
                        @Query("format") String format);
}
