package com.letb.museek.Services;

import com.letb.museek.Requests.TokenInterface;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by eugene on 06.12.15.
 */
public class SpiceRequestService extends RetrofitGsonSpiceService {

//    У тебя появится проблема с не тем урлом. Моя идея решения такая: прописываешь для токена
//    в ретрофите полный путь, тогда он не будет использовать эту базу
    // А во всех остальных методаз испольщуешь частный
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
