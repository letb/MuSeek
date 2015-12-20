package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Fragments.ArtistListFragment;
import com.letb.museek.Models.Artist;
import com.letb.museek.Utils.UserInformer;

import java.util.List;

public class MainActivity extends FragmentActivity implements ArtistListFragment.OnArtistSelectedListener {

    private List<Artist> artistList;
    private ArtistListFragment artistListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistList = (List<Artist>) getIntent().getExtras().getSerializable(ArtistListFragment.ARTIST_LIST);
        artistListFragment = new ArtistListFragment();

        Intent data = getIntent();
        showFragment(artistListFragment, data.getExtras());
    }

    @Override
    public void onArtistSelected(Integer position) {
        UserInformer.showMessage(MainActivity.this, "Artist clicked " + artistList.get(position).getName());
    }

    public void showFragment (Fragment fragment, Bundle data) {
        fragment.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.artist_list, fragment);
        ft.commit();
    }
}
