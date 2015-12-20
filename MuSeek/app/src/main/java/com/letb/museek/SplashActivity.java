package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.TokenEventSuccess;
import com.letb.museek.Events.TrackEventSuccess;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Playlist;
import com.letb.museek.RequestProcessor.RequestProcessorService;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.UserInformer;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SplashActivity extends BaseSpiceActivity {

    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proceedToMain(new Artist("Pink Floyd", "blabla.png"));
//        requestToken();
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

    public void onEvent(TokenEventSuccess event){
        TokenHolder.setData(event.getData().getAccessToken(), event.getData().getExpiresIn());
        UserInformer.showMessage(SplashActivity.this, event.getData().getAccessToken());
//        TODO: For Test
        requestTrack("4425964VcAZ", "listen");
    }

    public void onEvent(TrackEventSuccess event){
        proceedToPlayList(event.getData());
    }

    public void onEvent(EventFail event){
        UserInformer.showMessage(SplashActivity.this, event.getException());
    }


    private void proceedToPlayList (final Track trackForTest) {
        //  TODO: For test
        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(trackForTest);
        Intent intent = new Intent(this, PlaylistActivity.class);
        intent.putExtra(PlaylistFragment.TRACK_LIST, trackList);
        startActivity(intent);
    }

    private void proceedToMain (Artist artistForTest) {
        //  TODO: For test
        ArrayList<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("Lol", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));
        artistList.add(new Artist("Qweqwe", "lolpic"));

        artistList.add(artistForTest);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ArtistListFragment.ARTIST_LIST, artistList);
        startActivity(intent);
    }
}
