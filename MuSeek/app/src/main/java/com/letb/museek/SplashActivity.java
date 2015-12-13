package com.letb.museek;

import android.os.Bundle;
import android.widget.Toast;

import com.letb.museek.Models.Token;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class SplashActivity extends BaseSpiceActivity {
    private TokenRequest tokenRequest;
    private TrackRequest trackRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestToken();
    }

    public void requestToken() {
        tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TokenRequestListener());
    }

    public void requestTrack(String token, String trackId, String reason) {
        trackRequest = new TrackRequest(token, trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TrackRequestListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setProgressBarIndeterminate(false);
        setProgressBarVisibility(true);

        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TokenRequestListener());
    }

//    private void proceedToPlayList () {
//        ArrayList<SongModel> trackList = new ArrayList<>();
//        SongModel track = new SongModel();
//        track.setTitle("MyFavSong");
//        track.set_url("http://s21.pleer.com/0042abbee57d489d6f23c9e2cd05ad67ceece848799f9c8afc061aafa35f9016914b813d2156f1dd5f49d19d2416cf7252bd0c5638c30b0ae5779141b55ad12a3868ac7d1e3f51435de589188d1081/2139149f8f.mp3");
//        SongModel anotherTrack = new SongModel();
//        anotherTrack.setTitle("MyFavSoniiiiig");
//        anotherTrack.set_url("http://s21.pleer.com/0042abbee57d489d6f23c9e2cd05ad67ceece848799f9c8afc061aafa35f9016914b813d2156f1dd5f49d19d2416cf7252bd0c5638c30b0ae5779141b55ad12a3868ac7d1e3f51435de589188d1081/2139149f8f.mp3");
//        trackList.add(track);
//        trackList.add(anotherTrack);
//
//
//        Intent intent = new Intent(this, PlayListActivity.class);
//        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
//        startActivity(intent);
//    }


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
            String info = result.getData().getmArtist() + "—" + result.getData().getmTrack();
            Toast.makeText(SplashActivity.this, "Success!: " + info, Toast.LENGTH_SHORT).show();
        }
    }
}
