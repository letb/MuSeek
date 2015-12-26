package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Events.TokenEventSuccess;

import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.RequestProcessor.RequestProcessorService;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.ResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import de.greenrobot.event.EventBus;
import roboguice.util.temp.Ln;

public class SplashActivity extends BaseSpiceActivity {

    private final String TAG = "SplashActivity";

    private EventBus bus = EventBus.getDefault();
    public static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestToken();
    }

    @Override
    public void onResume () {
        bus.register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        bus.unregister(this);
        super.onPause();
    }

    public void requestToken() {
        RequestProcessorService.startTokenRequestAction(this);
    }

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        proceedToPlayList();
        Log.i(TAG, event.getData().getAccessToken());
    }

    public void onEvent(EventFail event) {
        UserInformer.showMessage(SplashActivity.this, event.getException());
    }

    private void proceedToPlayList () {

//        ArrayList<Artist> artistList = new ArrayList<>();
//        Intent intent = new Intent(this, PlaylistActivity.class);
        Intent intent = new Intent(this, PlaylistActivity.class);
//        intent.putExtra(ArtistListFragment.ARTIST_LIST, artistList);
//        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);

        startActivity(intent);
    }
}
