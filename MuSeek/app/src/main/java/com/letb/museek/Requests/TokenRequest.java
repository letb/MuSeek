package com.letb.museek.Requests;

import com.letb.museek.Models.TokenModel;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by eugene on 06.12.15.
 */
//                  <Result, Interface>
public class TokenRequest extends RetrofitSpiceRequest<TokenModel, TokenInterface> {

    private String authorization;
    private final String grantType = "client_credentials";


    public TokenRequest(String authorization) {
        super(TokenModel.class, TokenInterface.class);
        this.authorization = authorization;
    }

    @Override
    public TokenModel loadDataFromNetwork() {
        Ln.d("Call web service ");
        return getService().getToken(authorization, grantType);
    }
}
