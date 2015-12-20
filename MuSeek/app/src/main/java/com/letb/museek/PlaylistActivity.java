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

public class PlaylistActivity extends BaseSpiceActivity implements PlaylistFragment.OnTrackSelectedListener, PlayerFragment.OnMediaButtonClickListener {

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
        Intent fragmentIntent = new Intent(this, MediaPlayerService.class);
        fragmentIntent.putExtra(PlayerFragment.TRACK_LIST, (ArrayList<Track>) trackList);
        showFragment(new PlayerFragment(), fragmentIntent);

//        TODO:For test
        UserInformer.showMessage(PlaylistActivity.this, "Playing track " + trackList.get(position).getTitle());
//        TODO: Spaghetti
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

    @Override
    public void onPlayPauseClicked(Integer index) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_TOGGLE_PLAYBACK);
        this.startService(intent);
    }

    @Override
    public void onNextClicked(Integer index) {

    }

    @Override
    public void onPrevClicked(Integer index) {

    }
}
