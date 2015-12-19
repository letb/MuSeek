package com.letb.museek;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.letb.museek.BaseClasses.BaseSpiceActivity;
import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Utils.UserInformer;

import java.util.List;

public class PlaylistActivity extends BaseSpiceActivity implements PlaylistFragment.OnTrackSelectedListener {

    private MediaPlayerService mediaPlayerService;
    private Intent playIntent;
    private boolean mediaPlayerBound = false;
    private List<Track> trackList;
    private ServiceConnection musicConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MediaPlayerService.Binder binder = (MediaPlayerService.Binder) service;
                mediaPlayerService = binder.getService();
                mediaPlayerService.setTrackList(trackList);
                mediaPlayerBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mediaPlayerBound = false;
            }
        };

        trackList = (List<Track>) getIntent().getExtras().getSerializable(PlaylistFragment.TRACK_LIST);
        showFragment(new PlaylistFragment(), getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onTrackSelected(Integer position) {
        UserInformer.showMessage(PlaylistActivity.this, "Playing track " + trackList.get(position).getTitle());
        mediaPlayerService.setTrack(position);
        mediaPlayerService.playTrack();
    }

    public void showFragment (Fragment fragment, Intent data) {
        fragment.setArguments(data.getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment);
        ft.commit();
    }
}
