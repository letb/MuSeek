package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.letb.museek.Entities.Token;
import com.letb.museek.Requests.TokenRequest;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class SplashActivity extends BaseSpiceActivity {
    private TokenRequest tokenRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new APIRequestListener());
    }

    public void proceedNextActivity(String accessToken) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PLEER.COM ACCESS TOKEN", accessToken);
        startActivity(intent);

        finish();
    }

//    Пока для реквестов создаем такие, я потом вынесу все в отдельный класс-обработчик
//    Концы для реквестов
    public final class APIRequestListener implements RequestListener<Token> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Token result) {
            proceedNextActivity(result.getAccessToken());

        }
    }
}
