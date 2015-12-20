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

}
