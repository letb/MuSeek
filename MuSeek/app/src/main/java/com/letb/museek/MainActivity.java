package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Utils.UserInformer;

import java.util.List;

public class MainActivity extends PlaylistActivity implements ArtistListFragment.OnArtistSelectedListener {

    private List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistList = (List<Artist>) getIntent().getExtras().getSerializable(ArtistListFragment.ARTIST_LIST);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new ArtistListFragment(), getIntent(), R.id.artist_container, ft);
        showFragment(new PlaylistFragment(), getIntent(), R.id.track_container, ft);
        ft.commit();

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
