package com.letb.museek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_splash);

        tokenRequest = new TokenRequest(Token.authHTTPHeader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new APIRequestListener());
    }

//    Пока для реквестов создаем такие, я потом вынесу все в отдельный класс-обработчик
    public final class APIRequestListener implements RequestListener<Token> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Token result) {
            // Получили, сохранили, передали
            Toast.makeText(SplashActivity.this, "Success!: " + result.getAccessToken(), Toast.LENGTH_SHORT).show();
        }
    }
}
