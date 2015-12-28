package com.letb.museek;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.HorizontalTrackListFragment;
import com.letb.museek.Fragments.PlayerFragment;
import com.letb.museek.Fragments.VerticalTrackListFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.ArtistNames;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.SynchronousRequests.ArtistInfoTask;
import com.letb.museek.Requests.SynchronousRequests.SearchTrackListTask;
import com.letb.museek.Requests.SynchronousRequests.TopTrackListTask;
import com.letb.museek.Requests.SynchronousRequests.TrackInfoTask;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Utils.ResponseParser;
import com.letb.museek.Utils.UserInformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseSpiceActivity implements
        HorizontalTrackListFragment.OnTrackSelectedListener,
        VerticalTrackListFragment.OnTrackSelectedListener,
        ArtistListFragment.OnArtistSelectedListener {

    private EventBus bus = EventBus.getDefault();
    private final String TAG = "MainActivity";
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
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        artistList.clear();
        // FIXME: 28.12.15 Ну типа константы надо именовать, все дела
        for (int i = 0; i < 20; ++i) {
            Artist artist = new Artist(ArtistNames.getRandomName());
            if (artistList.contains(artist)) {
                --i;
            } else {
                artistList.add(artist);
            }
        }

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

//        Class fragmentClass;
//        switch(menuItem.getItemId()) {
//            case R.id.nav_first_fragment:
//                fragmentClass = FirstFragment.class;
//                break;
//            case R.id.nav_second_fragment:
//                fragmentClass = SecondFragment.class;
//                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
//                break;
//            default:
//                fragmentClass = FirstFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        dlDrawer.closeDrawers();
    }


    @Override
    protected void onStart() {
        super.onStart();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        new Thread(new TopTrackListTask(TopTrackListTask.EN_LIST)).start();
        new Thread(new SearchTrackListTask("bowie")).start();
        new Thread(new ArtistInfoTask(artistList)).start();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @IdRes
    public void showFragment(Fragment fragment, Bundle data, @IdRes int container, FragmentTransaction ft) {
        fragment.setArguments(data);
        ft.add(container, fragment);
    }

    @Override
    public void onTrackSelected(Integer trackIndex, List<Track> trackList) {
        Log.d(TAG, "Clicked track item" + trackIndex);
        prepareAndShowPlayerFragment(trackIndex, (ArrayList<Track>) trackList);
        prepareAndStartService(trackList, trackIndex);
    }

    @Override
    public void onArtistSelected(Integer position) {
        Log.d(TAG, "Clicked artist item" + position);
        Artist artist = artistList.get(position);
        new Thread(new SearchTrackListTask(artist.getName())).start();
    }

    protected void placeContainerContents(int container, ArrayList<?> listToShow) {
        Bundle fragmentArgs = new Bundle();
        Fragment fragmentToShow;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (listToShow.get(0) instanceof Track) {
            fragmentArgs.putSerializable(HorizontalTrackListFragment.TRACK_LIST, listToShow);
            fragmentToShow = new HorizontalTrackListFragment();
        }
        else {
            fragmentArgs.putSerializable(ArtistListFragment.ARTIST_LIST, listToShow);
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
                            placeContainerContents(EN_TRACK_LIST_CONTAINER, enTopTrackList);
                            /**
                             * TODO: <username>, тебе сюда!
                             */
                            new Thread(new TrackInfoTask(enTopTrackList)).start();
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
        Log.d(TAG, "Event arrived" + event.getData().toString());
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

    public void onEvent(final ArtistInfoEvent event) throws JSONException {
        Log.d(TAG, "Event arrived" + event.getArtists().toString());
        artistList = ResponseParser.parseArtistInfoResponse(event.getArtists());
        placeContainerContents(ARTIST_LIST_CONTAINER, artistList);
    }

    public void onEvent(EventFail event) {
        UserInformer.showMessage(MainActivity.this, event.getException());
    }

    /** LOGICS GOES HERE
     * Только логика, только хардкор
     * (после этого комментария все методы делают что-то до ужаса умное. Парсят артистов, например)
     */
    private void prepareAndShowPlayerFragment (Integer currentTrackIndex, ArrayList<Track> currentTrackList) {
        Bundle selectedTrackData = new Bundle();
        selectedTrackData.putSerializable(PlayerFragment.TRACK_LIST, currentTrackList);
        selectedTrackData.putSerializable(PlayerFragment.CURRENT_TRACK, currentTrackIndex);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new PlayerFragment(), selectedTrackData, android.R.id.content, ft);
        ft.addToBackStack(PlayerFragment.TAG).commit();
    }

    private void prepareAndStartService(List<Track> trackListToPlay, Integer currentTrackIndex) {
        Log.d(TAG, "Playing track " + trackList.get(currentTrackIndex).getTitle());

        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        intent.putExtra(MediaPlayerService.PLAYLIST, (ArrayList<Track>) trackListToPlay);
        intent.putExtra(MediaPlayerService.SELECTED_TRACK_INDEX, currentTrackIndex);

        this.startService(intent);
    }
}
