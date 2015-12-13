package com.letb.museek.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.letb.museek.Fragments.PlaylistFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.R;

import java.util.ArrayList;

/**
 * Created by marina.titova on 13.12.15.
 */
public class PlayerFragment extends Fragment implements PlaylistFragment.OnTrackSelectedListener {

    public static final String TRACK_LIST = "TRACK_LIST";

    private Context appContext = null;
    private MediaPlayerService mediaPlayerService;
    private Intent  playIntent = null;
    private boolean mediaPlayerBound   = false;
    private ArrayList<Track> trackList = null;

    public PlayerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        if (getArguments() != null) {
            trackList = (ArrayList<Track>) getArguments().getSerializable(TRACK_LIST);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null)
            appContext = getActivity().getApplicationContext();

//        trackList = (ArrayList<Track>) getIntent().getExtras().getSerializable(PlaylistFragment.TRACK_LIST);
//        PlaylistFragment playlistFragment = new PlaylistFragment();
//        playlistFragment.setArguments(getIntent().getExtras());
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(android.R.id.content, playlistFragment);
//        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(appContext, MediaPlayerService.class);
            appContext.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            appContext.startService(playIntent);
        }
    }

    public void trackPicked(View view) {
        // todo: change view get tag to smth useful

    }

//    TODO: Когда это происходит?
    private ServiceConnection musicConnection = new ServiceConnection() {
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

    @Override
    public void onTrackSelected(Integer position) {
        Toast.makeText(appContext, position, Toast.LENGTH_SHORT).show();
    }
}
