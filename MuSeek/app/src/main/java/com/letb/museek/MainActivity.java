package com.letb.museek;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Events.ArtistInfoEvent;
import com.letb.museek.Events.EventFail;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.TrackInfoEvent;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.HorizontalTrackListFragment;
import com.letb.museek.Fragments.VerticalTrackListFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.ArtistNames;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.SynchronousRequests.ArtistInfoTask;
import com.letb.museek.Requests.SynchronousRequests.TopTrackListTask;
import com.letb.museek.Requests.SynchronousRequests.TrackInfoTask;
import com.letb.museek.Utils.ResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseSpiceActivity implements
        HorizontalTrackListFragment.OnTrackSelectedListener,
        VerticalTrackListFragment.OnTrackSelectedListener,
        ArtistListFragment.OnArtistSelectedListener {

    private final String TAG = "MainActivity";
    private Intent playIntent;
    private ArrayList<Track> searchTrackList;
    private ArrayList<Track> ruTopTrackList;
    private ArrayList<Track> enTopTrackList;

    private ArrayList<Track> trackList = new ArrayList<>();
    private ArrayList<Artist> artistList = new ArrayList<>();
    private ArrayList<String> artistNames = new ArrayList<>();

    protected final Integer EN_TRACK_LIST_CONTAINER = R.id.en_container;
    protected final Integer RU_TRACK_LIST_CONTAINER = R.id.ru_container;
    protected final Integer ARTIST_LIST_CONTAINER = R.id.artist_container;
    protected final Integer WHOLE_CONTAINER = R.id.total_container;


    private ProgressBar artistSpinner;
    private ProgressBar enPlayListSpinner;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refreshArtists();
        refreshTopEnList();

        // Find our drawer view
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        dlDrawer.setDrawerListener(drawerToggle);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        // Inflate the header view at runtime
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        toolbar.setTitle("Main");
        // We can now look up items within the header if needed
//        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.imageView);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.nav_second_button) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }


        // Highlight the selected item, update the title, and close the drawer
        dlDrawer.closeDrawers();
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "I was resumed");
        super.onResume();
        toolbar.setTitle("Main");

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "I was paused");
        super.onPause();
    }

    private void refreshArtists () {
        artistSpinner = (ProgressBar)findViewById(R.id.artistProgressBar);
        artistSpinner.setVisibility(View.VISIBLE);
        artistSpinner.bringToFront();

        artistList.clear();
        // FIXME: 28.12.15 Ну типа константы надо именовать, все дела
        for (int i = 0; i < 10; ++i) {
            Artist artist = new Artist(ArtistNames.getRandomName());
            while (artistNames.contains(artist.getName()))
                artist = new Artist(ArtistNames.getRandomName());
            artistList.add(artist);
            artistNames.add(artist.getName());
        }

        new Thread(new ArtistInfoTask(artistList)).start();
    }

    private void refreshTopEnList () {
        enPlayListSpinner = (ProgressBar)findViewById(R.id.enPlayListProgressBar);
        enPlayListSpinner.setVisibility(View.VISIBLE);
        enPlayListSpinner.bringToFront();

        new Thread(new TopTrackListTask(TopTrackListTask.EN_LIST)).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public void onTrackSelected(Integer trackIndex, List<Track> trackList) {
        Log.d(TAG, "Clicked track item" + trackIndex);
        prepareAndShowPlayerFragment(trackIndex, (ArrayList<Track>) trackList);
        prepareAndStartService(trackList, trackIndex);
    }

    @Override
    public void onTrackListRefreshed() {
        refreshTopEnList();
    }

    @Override
    public void onArtistSelected(Integer position) {
        Log.d(TAG, "Clicked artist item" + position);
        Artist artist = artistList.get(position);
        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.putExtra(SearchActivity.SEARCH_STRING, artist);
        searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(searchIntent);
    }

    @Override
    public void onArtistListRefreshed() {
        refreshArtists();
    }

    protected void placeListContainerContents(int container, ArrayList<?> listToShow) {
        Bundle fragmentArgs = new Bundle();
        Fragment fragmentToShow;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (listToShow.get(0) instanceof Track) {
            fragmentArgs.putSerializable(HorizontalTrackListFragment.TRACK_LIST, listToShow);
            fragmentArgs.putSerializable(HorizontalTrackListFragment.TITLE, "Popular Tracks");
            fragmentToShow = new HorizontalTrackListFragment();
        }
        else {
            fragmentArgs.putSerializable(ArtistListFragment.ARTIST_LIST, listToShow);
            fragmentArgs.putSerializable(ArtistListFragment.TITLE, "Top Artists");
            fragmentToShow = new ArtistListFragment();
        }
        showFragment(fragmentToShow, fragmentArgs, container, ft);
        ft.commit();
    }

    public void onEvent(final PlaylistEventSuccess event) {
        Log.d(TAG, "Event arrived" + event.getData().toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    trackList = ResponseParser.parsePlaylistResponse(event.getData());
                    String trackListType = event.getEVENT_CONTENTS();
                    switch (trackListType) {
                        case TopTrackListTask.EN_LIST:
                            enTopTrackList = trackList;
                            new Thread(new TrackInfoTask(enTopTrackList)).start();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onEvent(final TrackInfoEvent event) throws JSONException {
        Log.d(TAG, "Event arrived" + event.getTracks().toString());
        trackList = ResponseParser.parseTrackInfoResponse(event.getTracks(), trackList);
        placeListContainerContents(EN_TRACK_LIST_CONTAINER, trackList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enPlayListSpinner.setVisibility(View.GONE);
            }
        });
    }

    public void onEvent(final ArtistInfoEvent event) throws JSONException {
        Log.d(TAG, "Event arrived" + event.getArtists().toString());
        artistList = ResponseParser.parseArtistInfoResponse(event.getArtists());
        placeListContainerContents(ARTIST_LIST_CONTAINER, artistList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                artistSpinner.setVisibility(View.GONE);
            }
        });
    }

    public void onEvent(EventFail event) {
        UserInformer.showMessage(MainActivity.this, event.getException());
    }
}
