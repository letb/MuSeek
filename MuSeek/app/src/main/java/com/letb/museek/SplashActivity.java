package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Token;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class SplashActivity extends BaseSpiceActivity {
    private TokenRequest tokenRequest;
    private TrackRequest trackRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestToken();
        proceedToPlayList();
    }

    public void requestToken() {
        tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TokenRequestListener());
    }

    public void requestTrack(String token, String trackId, String reason) {
        trackRequest = new TrackRequest(token, trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TrackRequestListener());
    }


    private void proceedToPlayList () {
        ArrayList<Track> trackList = new ArrayList<>();
        Track track = new Track();
        track.setTitle("SUPERBAND", "MyFavSong");
        track.setUrl("http://s6-1.pleer.com/0042fdbee27d1b9b6f22c8e2cf02a27e8df1ab4109a38bc5d71f4c82bf51df1ed407d5506460e69e1063dad85f4b83435faa027b32d62204cd77cb/e0cf43cecb.mp3");
        Track anotherTrack = new Track();
        anotherTrack.setTitle("ZUPABAND", "MyFavSoniiiiig");
        anotherTrack.setUrl("http://s21.pleer.com/0042abbee57d489d6f23c9e2cd05ad67ceece848799f9c8afc061aafa35f9016914b813d2156f1dd5f49d19d2416cf7252bd0c5638c30b0ae5779141b55ad12a3868ac7d1e3f51435de589188d1081/2139149f8f.mp3");
        trackList.add(track);
        trackList.add(anotherTrack);


        Intent intent = new Intent(this, PlayListActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }


//    Пока для реквестов создаем такие, я потом вынесу все в отдельный класс-обработчик
//    Концы для реквестов
    public final class TokenRequestListener implements RequestListener<Token> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Token result) {
//            proceedNextActivity(result.getAccessToken());
            TokenHolder.setAccessToken(result.getAccessToken());
            TokenHolder.setExpiresIn(result.getExpiresIn());
            requestTrack(result.getAccessToken(), "851340JUky", "save");

        }
    }


    public final class TrackRequestListener implements RequestListener<Track> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SplashActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Track result) {
            String info = result.getTitle();
            Toast.makeText(SplashActivity.this, "Success!: " + info, Toast.LENGTH_SHORT).show();
        }
    }
}
