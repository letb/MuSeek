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
import com.letb.museek.Models.Playlist;
import com.letb.museek.Models.Track.Data;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.Fragments.PlaylistFragment;
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

    public void requestToken() {
        RequestProcessorService.startTokenRequestAction(this);
    }

    public void requestTopTracks(int timePeriod, int page, String language) {
        RequestProcessorService.startTopTracksRequestAction(this, timePeriod, page, language);
    }

    public void requestSearchTracks(String query, int resultsOnPage, String quality) {
        RequestProcessorService.startSearchTracksRequestAction(this, query, resultsOnPage, quality);
    }

    public void requestTrackUrl(String trackId, String reason) {
        RequestProcessorService.startTrackUrlRequestAction(this, trackId, reason);
    }

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        Log.i(TAG, event.getData().getAccessToken());
//        requestTrack("11635570PMIz", "listen");
//        requestTopTracks(2, 1, "en");
        requestSearchTracks("bowie", 20, "all");
    }

    public void onEvent(PlaylistEventSuccess event) {
        try {
            trackList = ResponseParser.parsePlaylistResponse(event.getData());
            getCurrentTrackUrl();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(SearchEventSuccess event) {
        try {
            trackList = ResponseParser.parseSearchResponse(event.getData());
            getCurrentTrackUrl();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(TrackUrlEventSuccess event) {
        trackList.get(currentTrackIndex).setUrl(event.getData());
        currentTrackIndex += 1;

        Ln.d("TRACK #" + Integer.toString(currentTrackIndex));

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

    public void onEvent(EventFail event) {
        UserInformer.showMessage(SplashActivity.this, event.getException());
    }

    private void proceedToPlayList (final ArrayList<Track> trackList) {

        ArrayList<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("Lol", "lolpic"));
        artistList.add(new Artist("Pink Floyd", "lolpic"));
        artistList.add(new Artist("Blabla", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));

//        Intent intent = new Intent(this, PlaylistActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ArtistListFragment.ARTIST_LIST, artistList);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);

        startActivity(intent);
    }
}
