package com.letb.museek.RequestProcessor;

import com.letb.museek.Models.Track.Track;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by eugene on 26.12.15.
 */
public class SynchronousRequestProcessor {
    public static final String API_BASE_URL = "http://your.api-base.url";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}
