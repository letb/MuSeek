package com.letb.museek.Requests;

import com.letb.museek.Models.TokenModel;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by eugene on 06.12.15.
 */
public interface TokenInterface {
    @FormUrlEncoded
    @POST("/token.php")
    TokenModel getToken(@Header("Authorization") String authorization, @Field("grant_type") String type);
}
