package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Token;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.UserInformer;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class SplashActivity extends BaseSpiceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestToken();
    }

    public void requestToken() {
        TokenRequest tokenRequest = new TokenRequest(Token.authHTTPHeader);
        getSpiceManager().execute(tokenRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TokenRequestListener());
    }

    public void requestTrack(String token, String trackId, String reason) {
        TrackRequest trackRequest = new TrackRequest(token, trackId, reason);
        getSpiceManager().execute(trackRequest, 0, DurationInMillis.ALWAYS_EXPIRED, new TrackRequestListener());
    }


    private void proceedToPlayList (final Track trackForTest) {
        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(trackForTest);
        Intent intent = new Intent(this, PlayListActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }


    public final class TokenRequestListener implements RequestListener<Token> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            UserInformer.showMessage(SplashActivity.this, getResources().getString(R.string.TOKEN_FAILURE));
        }

        @Override
        public void onRequestSuccess(final Token result) {
            TokenHolder.setAccessToken(result.getAccessToken());
            TokenHolder.setExpiresIn(result.getExpiresIn());
//            TODO: ForTest
            requestTrack(result.getAccessToken(), "4425964VcAZ", "listen");
        }
    }


    public final class TrackRequestListener implements RequestListener<Track> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            UserInformer.showMessage(SplashActivity.this, getResources().getString(R.string.TOKEN_FAILURE));
        }

        @Override
        public void onRequestSuccess(final Track result) {
            UserInformer.showMessage(SplashActivity.this, result.getTitle());
            proceedToPlayList(result);
        }
    }
}
