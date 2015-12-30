package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null)
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
}
