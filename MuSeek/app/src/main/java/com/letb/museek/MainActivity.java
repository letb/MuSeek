package com.letb.museek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Utils.UserInformer;

import java.util.List;

public class MainActivity extends PlaylistActivity implements ArtistListFragment.OnArtistSelectedListener {

    private List<Artist> artistList;
    private List<Track>  trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistList = (List<Artist>) getIntent().getExtras().getSerializable(ArtistListFragment.ARTIST_LIST);
        trackList =  (List<Track>) getIntent().getExtras().getSerializable(PlaylistFragment.TRACK_LIST);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new ArtistListFragment(), getIntent(), R.id.artist_container, ft);
        showFragment(new PlaylistFragment(), getIntent(), R.id.track_container, ft);
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onArtistSelected(Integer position) {
        UserInformer.showMessage(MainActivity.this, "Artist: " + artistList.get(position).getName());
    }

    @IdRes
    public void showFragment (Fragment fragment, Intent data, @IdRes int container, FragmentTransaction ft) {
        fragment.setArguments(data.getExtras());
        ft.add(container, fragment);
    }
}
