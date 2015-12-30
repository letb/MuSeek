package com.letb.museek;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Events.ClearPlayListEvent;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Events.TrackInfoEvent;
import com.letb.museek.Fragments.VerticalTrackListFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.SynchronousRequests.SearchTrackListTask;
import com.letb.museek.Requests.SynchronousRequests.TrackInfoTask;
import com.letb.museek.Utils.ResponseParser;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchActivity extends BaseSpiceActivity implements VerticalTrackListFragment.OnTrackSelectedListener {

    public static final String SEARCH_STRING = "SEARCH_STRING";
    private final String TAG = "SearchActivity";

    protected final Integer SEARCH_LIST_CONTAINER = R.id.search_results;

    private ArrayList<Track> searchTrackList = new ArrayList<>();
    private Artist searchArtist;
    private String searchString;
    private EditText searchField;
    private Button searchButton;
    private ImageView artistImage;
    private EventBus bus = EventBus.getDefault();
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null)
            searchArtist = (Artist) intent.getSerializableExtra(SEARCH_STRING);

        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);
        artistImage = (ImageView) findViewById(R.id.artist_picture);

        if ((searchArtist != null)) {
            searchField.setText(searchArtist.getName());
            Picasso.with(this).load(searchArtist.getPic()).into(artistImage);
            doSearch();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doSearch();
            }
        });

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

        if (menuItem.getItemId() == R.id.nav_first_button) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);
            setTitle(menuItem.getTitle());
        }


        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        dlDrawer.closeDrawers();
    }

    private void showSearchResults () {
        Bundle fragmentArgs = new Bundle();
        android.support.v4.app.Fragment fragmentToShow;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentArgs.putSerializable(VerticalTrackListFragment.TRACK_LIST, searchTrackList);
        fragmentToShow = new VerticalTrackListFragment();
        fragmentToShow.setArguments(fragmentArgs);
        ft.replace(SEARCH_LIST_CONTAINER, fragmentToShow);
        ft.commit();
    }

    private void doSearch () {
        clearSearchResult();
        searchString = String.valueOf(searchField.getText());
        if (StringUtils.isNotEmpty(searchString)) {
            new Thread(new SearchTrackListTask(searchString)).start();
            if (searchArtist != null && !searchArtist.getName().equals(searchString)) {
                artistImage.setImageResource(0);
            }
        }
    }

    private void clearSearchResult () {
        bus.post(new ClearPlayListEvent());
    }


    public void onEvent(final SearchEventSuccess event) {
        Log.d(TAG, "Event arrived" + event.getData().toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchTrackList = ResponseParser.parseSearchResponse(event.getData());
                    showSearchResults();
                    new Thread(new TrackInfoTask(searchTrackList)).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onEvent(final TrackInfoEvent event) throws JSONException {
        Log.d(TAG, "Event arrived" + event.getTracks().toString());
        searchTrackList = ResponseParser.parseTrackInfoResponse(event.getTracks(), searchTrackList);
    }

    @Override
    public void onTrackSelected(Integer position, List<Track> trackList) {
        Log.d(TAG, trackList.get(position).getTitle());
        prepareAndShowPlayerFragment(position, (ArrayList<Track>) trackList);
        prepareAndStartService(trackList, position);
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

}
