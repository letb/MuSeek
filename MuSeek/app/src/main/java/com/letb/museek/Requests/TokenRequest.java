package com.letb.museek.Requests;

import com.letb.museek.Entities.Token;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by eugene on 06.12.15.
 */
//                  <Result, Interface>
public class TokenRequest extends RetrofitSpiceRequest<Token, TokenInterface> {

    private String authorization;
    private final String grantType = "client_credentials";


    public TokenRequest(String authorization) {
        super(Token.class, TokenInterface.class);
        this.authorization = authorization;
    }

    @Override
    public Token loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getToken(authorization, grantType);
    }
}
