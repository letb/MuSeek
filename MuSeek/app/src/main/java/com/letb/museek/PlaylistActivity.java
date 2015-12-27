package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.HorizontalTrackListFragment;
import com.letb.museek.Fragments.PlayerFragment;
import com.letb.museek.Fragments.VerticalTrackListFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.RequestProcessor.SearchTrackListTask;
import com.letb.museek.RequestProcessor.TopTrackListTask;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Utils.ResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PlaylistActivity extends BaseSpiceActivity implements
        HorizontalTrackListFragment.OnTrackSelectedListener,
        VerticalTrackListFragment.OnTrackSelectedListener,
        PlayerFragment.OnMediaButtonClickListener,
        ArtistListFragment.OnArtistSelectedListener {

    private EventBus bus = EventBus.getDefault();
    private final String TAG = "PlaylistActivity";
    private Intent playIntent;
    private ArrayList<Track> searchTrackList;
    private ArrayList<Track> ruTopTrackList;
    private ArrayList<Track> enTopTrackList;

    private ArrayList<Track> trackList = new ArrayList<>();
    private ArrayList<Artist> artistList = new ArrayList<>();
    protected final Integer EN_TRACK_LIST_CONTAINER = R.id.en_container;
    protected final Integer RU_TRACK_LIST_CONTAINER = R.id.ru_container;
    protected final Integer ARTIST_LIST_CONTAINER = R.id.artist_container;


    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        new Thread(new TopTrackListTask(TopTrackListTask.EN_LIST)).start();
        new Thread(new SearchTrackListTask("bowie")).start();
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
    public void showFragment(Fragment fragment, Bundle data, @IdRes int container, FragmentTransaction ft) {
        fragment.setArguments(data);
        ft.add(container, fragment);
    }

    @Override
    public void onTrackSelected(Integer trackIndex, List<Track> trackList) {
        Log.d("PlaylistActivity", "Clicked item" + trackIndex);
        Bundle selectedTrackData = new Bundle();
        selectedTrackData.putSerializable(PlayerFragment.TRACK_LIST, (ArrayList<Track>) trackList);
        selectedTrackData.putSerializable(PlayerFragment.CURRENT_TRACK, trackIndex);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new PlayerFragment(), selectedTrackData, android.R.id.content, ft);
        ft.commit();

        Log.d(TAG, "Playing track " + this.trackList.get(trackIndex).getTitle());
//        TODO: Spaghetti
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        intent.putExtra(MediaPlayerService.PLAYLIST, this.trackList);
        intent.putExtra(MediaPlayerService.SELECTED_TRACK_INDEX, trackIndex);
        this.startService(intent);
    }

    @Override
    public void onArtistSelected(Integer position) {
//        UserInformer.showMessage(PlaylistActivity.this, "Artist: " + artistList.get(position).getName());
    }

    protected void placeContainerContents(int container, ArrayList<?> listToShow) {
        Bundle fragmentArgs = new Bundle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (listToShow.get(0) instanceof Track) {
            fragmentArgs.putSerializable(HorizontalTrackListFragment.TRACK_LIST, listToShow);
            showFragment(new HorizontalTrackListFragment(), fragmentArgs, container, ft);
        }
        else if (listToShow.get(0) instanceof Artist) {
            fragmentArgs.putSerializable(ArtistListFragment.ARTIST_LIST, listToShow);
            showFragment(new ArtistListFragment(), fragmentArgs, container, ft);
        }
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

    private void showArtistsFromTrackList(ArrayList<Track> tracks) {
        artistList.clear();
        for (Track track : tracks) {
            artistList.add(new Artist(
                    track.getData().getArtist(), "http://ecx.images-amazon.com/images/I/61gMjdj6bjL._SL500_.jpg"
            ));
        }
        placeContainerContents(ARTIST_LIST_CONTAINER, artistList);
    }

    public void onEvent(final PlaylistEventSuccess event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    trackList = ResponseParser.parsePlaylistResponse(event.getData());
                    String trackListType = event.getEVENT_CONTENTS();
                    switch (trackListType) {
                        case TopTrackListTask.EN_LIST:
                            enTopTrackList = trackList;
                            placeContainerContents(EN_TRACK_LIST_CONTAINER, enTopTrackList);
                            showArtistsFromTrackList(enTopTrackList);
                            break;
                    }
                    spinner.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onEvent(final SearchEventSuccess event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchTrackList = ResponseParser.parseSearchResponse(event.getData());
                    placeContainerContents(RU_TRACK_LIST_CONTAINER, searchTrackList);
                    spinner.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void onEvent(EventFail event) {
        UserInformer.showMessage(PlaylistActivity.this, event.getException());
    }
}
