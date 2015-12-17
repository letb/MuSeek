package com.letb.museek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Listeners.RequestProcessingService;
import com.letb.museek.Models.Token;
import com.letb.museek.Requests.TokenRequest;
import com.letb.museek.Requests.TrackRequest;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.UserInformer;
import com.octo.android.robospice.persistence.DurationInMillis;

import java.util.ArrayList;

public class SplashActivity extends BaseSpiceActivity {

    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestToken();
    }

    @Override
    public void onResume () {
        IntentFilter filter = new IntentFilter(RequestProcessingService.BROADCAST_TOKEN_REQUEST_ANSWER);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UserInformer.showMessage(SplashActivity.this, intent.getAction());
                UserInformer.showMessage(SplashActivity.this, intent.getStringExtra(RequestProcessingService.BROADCAST_TOKEN_REQUEST_PARAMETER));
            }
        };
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(receiver);
        super.onPause();
    }

    public void requestToken() {
        RequestProcessingService.startTokenRequestAction(this);
    }

    public void requestTrack(String token, String trackId, String reason) {
    }


    private void proceedToPlayList (final Track trackForTest) {
        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(trackForTest);
        Intent intent = new Intent(this, PlayListActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }
}
