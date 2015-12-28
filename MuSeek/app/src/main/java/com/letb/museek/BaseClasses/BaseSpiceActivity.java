package com.letb.museek.BaseClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.letb.museek.Fragments.PlayerFragment;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;
import com.letb.museek.Services.MediaPlayerService;
import com.letb.museek.Services.SpiceRequestService;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.List;

// Sliding Activity - это базовый класс для любого активити с красивым меню
// В джаве экстендить можно только один класс, поэтому придется экстендить рано
// Не работает пока меню не используется
//public abstract class BaseSpiceActivity extends SlidingActivity {
public abstract class BaseSpiceActivity extends AppCompatActivity  {
    private final String TAG = "BaseSpiceActivity";
    private SpiceManager spiceManager = new SpiceManager( SpiceRequestService.class );

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @IdRes
    protected void showFragment(Fragment fragment, Bundle data, @IdRes int container, FragmentTransaction ft) {
        fragment.setArguments(data);
        ft.add(container, fragment);
    }

    protected void prepareAndShowPlayerFragment (Integer currentTrackIndex, ArrayList<Track> currentTrackList) {
        Bundle selectedTrackData = new Bundle();
        selectedTrackData.putSerializable(PlayerFragment.TRACK_LIST, currentTrackList);
        selectedTrackData.putSerializable(PlayerFragment.CURRENT_TRACK, currentTrackIndex);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        showFragment(new PlayerFragment(), selectedTrackData, android.R.id.content, ft);
        ft.addToBackStack(PlayerFragment.TAG).commit();
    }

    protected void prepareAndStartService(List<Track> trackListToPlay, Integer currentTrackIndex) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        intent.putExtra(MediaPlayerService.PLAYLIST, (ArrayList<Track>) trackListToPlay);
        intent.putExtra(MediaPlayerService.SELECTED_TRACK_INDEX, currentTrackIndex);
        this.startService(intent);
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
