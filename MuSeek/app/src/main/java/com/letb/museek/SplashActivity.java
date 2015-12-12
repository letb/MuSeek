package com.letb.museek;

import android.support.v7.app.AppCompatActivity;
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
        setContentView(R.layout.activity_splash);

        tokenRequest = new TokenRequest(Token.authHTTPHeader);
        StartAnimations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new APIRequestListener());
    }

//    Ну в общем сплеш скрин - это тоже не rocket science
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

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
            // Получили, сохранили, передали
            Toast.makeText(SplashActivity.this, "Success!: " + result.getAccessToken(), Toast.LENGTH_SHORT).show();
        }
    }
}
