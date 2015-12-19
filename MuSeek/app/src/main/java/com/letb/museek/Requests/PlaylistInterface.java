package com.letb.museek.Requests;


import com.letb.museek.Models.Playlist;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by dannie on 19.12.15.
 */
public interface PlaylistInterface {
//    Метод get_top_list — получить топ за период
//    Параметры:
//    list_type (int, обязательный) тип списка, 1- неделя, 2 - месяц, 3 - 3 месяца, 4 - полгода, 5 - год
//    page (int) — текущая страница.
//    language (string) — тип топа en - иностранный, ru - русский.

    @FormUrlEncoded
    @POST("/index.php")
    Playlist getTopTracks(@Field("access_token") String token,
                          @Field("method") String method,
                          @Field("list_type") int timePeriod,
                          @Field("page") int page,
                          @Field("language") String language);

}
