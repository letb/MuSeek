package com.letb.museek.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.letb.museek.Models.Track.Track;

import java.io.IOException;
import java.util.List;

/**
 * Created by marina.titova on 13.12.15.
 */
public class MediaPlayerService
        extends Service
        implements MediaPlayer.OnPreparedListener,
                    MediaPlayer.OnErrorListener,
                    MediaPlayer.OnCompletionListener {

    private final IBinder binder    = new Binder();
//    TODO: Зачему делать это явно?
    private MediaPlayer mediaPlayer = null;
    private List<Track> trackList   = null;
    private int currentTrackIndex   = 0;

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
        currentTrackIndex = 0;
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

    public void setTrackList(List<Track> tracks) {
        trackList = tracks;
    }

    public void setTrack(int trackIndex) {
        currentTrackIndex = trackIndex;
    }

    public void playTrack() {
        mediaPlayer.reset();
        Track track = trackList.get(currentTrackIndex);
        String  url = track.getUrl();
        Log.i("MUSIC SERVICE", "Trying play track");
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
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
