package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.TokenModel;
import com.letb.museek.Models.TrackModel;
import com.letb.museek.Requests.TokenRequest;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class SplashActivity extends BaseSpiceActivity {
    private TokenRequest tokenRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tokenRequest = new TokenRequest(TokenModel.authHTTPHeader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TokenRequestListener());
    }

    private void proceedToPlayList () {
        ArrayList<TrackModel> trackList = new ArrayList<>();
        TrackModel track = new TrackModel();
        track.setTitle("MyFavSong");
        TrackModel anotherTrack = new TrackModel();
        anotherTrack.setTitle("MyFavSoniiiiig");
        trackList.add(track);
        trackList.add(anotherTrack);


        Intent intent = new Intent(this, PlayListActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }

//    Пока для реквестов создаем такие, я потом вынесу все в отдельный класс-обработчик
//    Концы для реквестов
    public final class TokenRequestListener implements RequestListener<TokenModel> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final TokenModel result) {
            Toast.makeText(SplashActivity.this, "Success!: " + result.getAccessToken(), Toast.LENGTH_SHORT).show();
            TokenHolder.setAccessToken(result.getAccessToken());
            TokenHolder.setExpiresIn(result.getExpiresIn());
            proceedToPlayList();
        }
    }
}
