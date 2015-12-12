package com.letb.museek;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.letb.museek.Entities.Song;
import com.letb.museek.Services.MediaPlayerService;

import java.util.ArrayList;

public class PlayerActivity extends Activity {

    private MediaPlayerService mediaPlayerService;
    private Intent playIntent;
    private boolean mediaPlayerBound = false;
    private ArrayList<Song> songList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
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

    public void songPicked(View view) {
        // todo: change view get tag to smth useful
        mediaPlayerService.setSong(Integer.parseInt(view.getTag().toString()));
        mediaPlayerService.playSong();
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.Binder binder = (MediaPlayerService.Binder) service;
            mediaPlayerService = binder.getService();
            mediaPlayerService.setSongList(songList);
            mediaPlayerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlayerBound = false;
        }
    };
}
