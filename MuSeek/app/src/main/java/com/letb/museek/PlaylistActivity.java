package com.letb.museek;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlayerFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Playlist;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.RequestProcessor.RequestProcessorService;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Utils.ResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.util.temp.Ln;

public class PlaylistActivity extends BaseSpiceActivity implements PlaylistFragment.OnTrackSelectedListener, PlayerFragment.OnMediaButtonClickListener,  ArtistListFragment.OnArtistSelectedListener {

    private EventBus bus = EventBus.getDefault();
    private final String TAG = "PlaylistActivity";
    private MediaPlayerService mediaPlayerService;
    private Intent playIntent;
    private ArrayList<Track> searchTrackList;
    private ArrayList<Track> ruTopTrackList;
    private ArrayList<Track> enTopTrackList;

    private ArrayList<Track> trackList = new ArrayList<>();
//    private ArrayList<Artist> artistlist = new ArrayList<>();

    private int currentTrackIndex = 0;

    boolean gotEnTop = false;
    boolean gotRuTop = false;
    boolean gotSearchTracks = false;

    boolean isEventArrived = false;


    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        artistList = (List<Artist>) getIntent().getExtras().getSerializable(ArtistListFragment.ARTIST_LIST);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setNavigationIcon(R.drawable.icon_play);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavUtils.navigateUpFromSameTask(MainActivity.this);
//            }
//        });
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Music");
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
//        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
//        requestTrack("11635570PMIz", "listen");
        requestTopTracks(2, 1, "en");
//        requestSearchTracks("bowie", 20, "all");
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

    @IdRes
    public void showFragment (Fragment fragment, Bundle data, @IdRes int container, FragmentTransaction ft) {
        fragment.setArguments(data);
        ft.add(container, fragment);

    }

    @Override
    public void onTrackSelected(Integer trackIndex) {
        Bundle selectedTrackData = new Bundle();

        selectedTrackData.putSerializable(PlayerFragment.TRACK_LIST, (ArrayList<Track>) trackList);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new PlayerFragment(), selectedTrackData, android.R.id.content, ft);
        ft.commit();

        Log.d(TAG, "Playing track " + trackList.get(trackIndex).getTitle());
//        TODO: Spaghetti
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        intent.putExtra(MediaPlayerService.PLAYLIST, (ArrayList<Track>) trackList);
        intent.putExtra(MediaPlayerService.SELECTED_TRACK_INDEX, trackIndex);
        this.startService(intent);
    }


    @Override
    public void onArtistSelected(Integer position) {
//        UserInformer.showMessage(PlaylistActivity.this, "Artist: " + artistList.get(position).getName());
    }

    private void proceedToPlay(Track track) {

    }

    // После того как пришли все урлы к трекам, показываем их
    // Артисты пока что не настроены
    private void showPlaylistFragment() {
        Bundle playlistArgs = new Bundle();
        playlistArgs.putSerializable(PlaylistFragment.TRACK_LIST, trackList);

//        Bundle artistlistArgs = new Bundle();
//        artistlistArgs.putSerializable(ArtistListFragment.ARTIST_LIST, artistList);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new PlaylistFragment(), playlistArgs, R.id.track_container, ft);
//        showFragment(new PlaylistFragment(), artistlistArgs, R.id.track_container, ft);
        ft.commit();
    }



    @Override
    public void onPlayPauseClicked(Integer index) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_TOGGLE_PLAYBACK);
        this.startService(intent);
    }

    @Override
    public void onPositionChanged(Integer index, Integer position) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_REWIND);
        intent.putExtra(MediaPlayerService.REWIND_POSITION, position);
        this.startService(intent);
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



    public void onEvent(PlaylistEventSuccess event) {
        try {
            if (!gotEnTop) {
                enTopTrackList = ResponseParser.parsePlaylistResponse(event.getData());
                trackList.addAll(enTopTrackList.subList(0, 5));
                gotEnTop = true;
                requestTopTracks(2, 1, "ru");
            } else if (!gotRuTop) {
                ruTopTrackList = ResponseParser.parsePlaylistResponse(event.getData());
                trackList.addAll(ruTopTrackList.subList(0, 5));
                gotRuTop = true;
                requestSearchTracks("bowie", 20, "all");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(SearchEventSuccess event) {
        try {
            searchTrackList = ResponseParser.parseSearchResponse(event.getData());
            trackList.addAll(searchTrackList.subList(0, 5));
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
            spinner.setVisibility(View.GONE);
            showPlaylistFragment();
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
        UserInformer.showMessage(PlaylistActivity.this, event.getException());
    }
}
