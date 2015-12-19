package com.letb.museek;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Fragments.PlayerFragment;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Utils.UserInformer;

import java.util.ArrayList;
import java.util.List;

public class PlayListActivity extends BaseSpiceActivity implements PlaylistFragment.OnTrackSelectedListener {

    private MediaPlayerService mediaPlayerService;
    private Intent playIntent;
    private List<Track> trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackList = (List<Track>) getIntent().getExtras().getSerializable(PlaylistFragment.TRACK_LIST);
        showFragment(new PlaylistFragment(), getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onTrackSelected(Integer position) {
        UserInformer.showMessage(PlayListActivity.this, "Playing track " + trackList.get(position).getTitle());
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        intent.putExtra(MediaPlayerService.PLAYLIST, (ArrayList<Track>) trackList);
        this.startService(intent);
    }

    public void showFragment (Fragment fragment, Intent data) {
        fragment.setArguments(data.getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment);
        ft.commit();
    }
}
