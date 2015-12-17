package com.letb.museek.Services;

import com.letb.museek.Requests.TokenInterface;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by eugene on 06.12.15.
 */
public class SpiceRequestService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "http://api.pleer.com";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(TokenInterface.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }
}
