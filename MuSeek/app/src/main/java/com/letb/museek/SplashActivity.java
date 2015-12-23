package com.letb.museek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.TokenEventSuccess;
import com.letb.museek.Events.TrackEventSuccess;
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

    private EventBus bus = EventBus.getDefault();
    public static CountDownLatch latch = new CountDownLatch(1);
    private ArrayList<Track> trackList;
    private int currentTrackIndex = 0;

    boolean isEventArrived = false;

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

//    Дернули сервис с текущим контекстом и доп, данными. Он поймет
    public void requestToken() {
        RequestProcessorService.startTokenRequestAction(this);
    }

    public void requestTrack(String trackId, String reason) {
        RequestProcessorService.startTrackRequestAction(this, trackId, reason);
    }

    public void requestTopTracks(int timePeriod, int page, String language) {
        RequestProcessorService.startTopTracksRequestAction(this, timePeriod, page, language);
    }

    public void requestTrackUrl(String trackId, String reason) {
        RequestProcessorService.startTrackUrlRequestAction(this, trackId, reason);
    }

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        UserInformer.showMessage(SplashActivity.this, event.getData().getAccessToken());
//        TODO: For Test
//        requestTrack("11635570PMIz", "listen");
        requestTopTracks(2, 1, "en");
    }


    public void onEvent(TrackEventSuccess event){
        proceedToPlayList(event.getData());
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

    private void proceedToPlayList(final ArrayList<Track> trackList) {
//        TODO: For test
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }

    private void proceedToPlayList(final Track trackForTest) {
//        TODO: For test
        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(trackForTest);
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }


}
