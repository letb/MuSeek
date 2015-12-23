package com.letb.museek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.TokenEventSuccess;

import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Playlist;
import com.letb.museek.Models.Track.Data;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.RequestProcessor.RequestProcessorService;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.PlaylistResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.HttpsURLConnection;

import de.greenrobot.event.EventBus;
import roboguice.util.temp.Ln;

public class SplashActivity extends BaseSpiceActivity {

    private final String TAG = "SplashActivity";

    private EventBus bus = EventBus.getDefault();
    public static CountDownLatch latch = new CountDownLatch(1);
    private ArrayList<Track> trackList;
    private int currentTrackIndex = 0;

    boolean isEventArrived = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        proceedToMain(new Artist("Pink Floyd", "blabla.png"));
        requestToken();
//        proceedToPlayList(new Track().setData(new Data()));
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

    public void requestTopTracks(int timePeriod, int page, String language) {
        RequestProcessorService.startTopTracksRequestAction(this, timePeriod, page, language);
    }

    public void requestTrackUrl(String trackId, String reason) {
        RequestProcessorService.startTrackUrlRequestAction(this, trackId, reason);
    }

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        Log.i(TAG, event.getData().getAccessToken());
        requestTopTracks(2, 1, "en");
    }

    public void onEvent(PlaylistEventSuccess event) {
        try {
            trackList = PlaylistResponseParser.parsePlaylistResponse(event.getData());
            getCurrentTrackUrl();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(TrackUrlEventSuccess event) {
        trackList.get(currentTrackIndex).setUrl(event.getData());
        currentTrackIndex += 1;

        if (currentTrackIndex < trackList.size()) {
            getCurrentTrackUrl();
        } else {
            proceedToPlayList(trackList);
        }

    }

    private void getCurrentTrackUrl() {
        Track track;
        track = trackList.get(currentTrackIndex);
        if (track.getUrl() == null) {
            isEventArrived = false;
            requestTrackUrl(track.getData().getId(), "listen");
        }
    }

    public void onEvent(EventFail event){
        UserInformer.showMessage(SplashActivity.this, event.getException());
    }

    private void proceedToPlayList (final Track trackForTest) {
        //  TODO: For test
        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(trackForTest);

        ArrayList<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("Lol", "lolpic"));
        artistList.add(new Artist("Pink Floyd", "lolpic"));
        artistList.add(new Artist("Blabla", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ArtistListFragment.ARTIST_LIST, artistList);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);

        startActivity(intent);
    }
}
