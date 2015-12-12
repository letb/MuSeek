package com.letb.museek.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.letb.museek.Entities.Song;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marina.titova on 13.12.15.
 */
public class MediaPlayerService
        extends Service
        implements MediaPlayer.OnPreparedListener,
                    MediaPlayer.OnErrorListener,
                    MediaPlayer.OnCompletionListener {

    private final IBinder binder        = new Binder();
    private MediaPlayer mediaPlayer     = null;
    private ArrayList<Song> songList    = null;
    private int currentSong             = 0;

    public class Binder extends android.os.Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    public void onCreate() {
        super.onCreate();
        currentSong = 0;
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
    }

    public void initMediaPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setSongList(ArrayList<Song> songs) {
        songList = songs;
    }

    public void setSong(int songId) {
        currentSong = songId;
    }

    public void playSong() {
        mediaPlayer.reset();
        Song    song = songList.get(currentSong);
        String  url = song.getUrl();

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer player) {}

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        return false;
    }
}
