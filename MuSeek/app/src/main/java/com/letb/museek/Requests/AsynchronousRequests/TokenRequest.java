package com.letb.museek.Requests.AsynchronousRequests;

import android.util.Base64;

import com.letb.museek.Models.Token;
import com.letb.museek.Requests.TokenInterface;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by eugene on 06.12.15.
 */
//                  <Result, Interface>
public class TokenRequest extends RetrofitSpiceRequest<Token, TokenInterface> {

    private final String grantType = "client_credentials";
    private static String AUTH_HTTP_HEADER = "Basic " + Base64.encodeToString(String.format("%s:%s", "823734", "111111").getBytes(), Base64.NO_WRAP);


    public TokenRequest() {
        super(Token.class, TokenInterface.class);
    }

    @Override
    public Token loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getToken(AUTH_HTTP_HEADER, grantType);
    }
}
