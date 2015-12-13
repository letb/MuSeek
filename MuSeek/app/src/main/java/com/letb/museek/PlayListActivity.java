package com.letb.museek;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.letb.museek.Adapters.TrackAdapter;
import com.letb.museek.Fragments.PlaylistFragment;

public class PlayListActivity extends BaseSpiceActivity  implements PlaylistFragment.OnTrackSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlaylistFragment playlistFragment = new PlaylistFragment();
        playlistFragment.setArguments(getIntent().getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, playlistFragment);
        ft.commit();
    }

    @Override
    public void onTrackSelected(String title) {
        Toast.makeText(PlayListActivity.this, title, Toast.LENGTH_SHORT).show();
    }
}
