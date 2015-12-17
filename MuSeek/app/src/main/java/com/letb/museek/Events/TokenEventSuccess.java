package com.letb.museek.Events;

import com.letb.museek.Models.Token;

/**
 * Created by eugene on 18.12.15.
 */
public class TokenEventSuccess {
    private Token data;

    public TokenEventSuccess(Token data){
        this.data = data;
    }

    public Token getData(){
        return data;
    }
}
