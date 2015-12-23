package com.letb.museek.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by marina.titova on 13.12.15.
 */
public class MediaPlayerService
        extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_TOGGLE_PLAYBACK =
            "com.letb.museek.musicplayer.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY = "com.letb.museek.musicplayer.action.PLAY";
    public static final String ACTION_REWIND = "com.letb.museek.musicplayer.action.REWIND";

    public static final String PLAYLIST = "com.letb.museek.musicplayer.data.PLAYLIST";
    public static final String SELECTED_TRACK_INDEX = "com.letb.museek.musicplayer.data.SELECTED_TRACK_INDEX";
    public static final String REWIND_POSITION = "com.letb.museek.musicplayer.data.REWIND_POSITION";

    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Playing, // playback active (media player ready!). (but the media player may actually be
        Paused
    };

    private MediaPlayer mMediaPlayer = null;    // The Media Player
    private List<Track> trackList = null;
    private Integer currentTrackIndex = 0;
    private State mState = State.Stopped;


    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        else
            mMediaPlayer.reset();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_TOGGLE_PLAYBACK:
                processTogglePlayPauseRequest();
                break;
            case ACTION_PLAY:
                trackList = (List<Track>) intent.getSerializableExtra(PLAYLIST);
                currentTrackIndex = intent.getIntExtra(SELECTED_TRACK_INDEX, 0);
                processPlayRequest();
                break;
            case ACTION_REWIND:
                int defaultPosition = 0;
                processRewindRequest(intent.getIntExtra(REWIND_POSITION, defaultPosition));
                break;
        }
        return START_NOT_STICKY;
    }

    private void processTogglePlayPauseRequest() {
        if (mState == State.Paused || mState == State.Stopped) {
            processPlayRequest();
        } else {
            processPauseRequest();
        }
    }

    private void processRewindRequest(Integer position) {
        int milliseconds = 1000;
        if (mState == State.Playing || mState == State.Paused)
            mMediaPlayer.seekTo(position * milliseconds);
    }

    private void processPlayRequest() {
        if (mState == State.Stopped) {
            playNextSong(currentTrackIndex);
        }
        else if (mState == State.Paused) {
            configAndStartMediaPlayer();
        }
        showNotification("playing...", trackList.get(currentTrackIndex).getTitle());
    }

    private void processPauseRequest() {
        mState = State.Paused;
        mMediaPlayer.pause();
        relaxResources(false);
        showNotification("paused...", trackList.get(currentTrackIndex).getTitle());
    }

    /**
     * Releases resources used by the service for playback. This includes the "foreground service"
     * status and notification, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void configAndStartMediaPlayer() {
        if (!mMediaPlayer.isPlaying()) {
            mState = State.Playing;
            showNotification("playing...", trackList.get(currentTrackIndex).getTitle());
            mMediaPlayer.start();
        }
    }

    private void playNextSong(Integer newCurrentIndex) {
        mState = State.Stopped;
        relaxResources(false);
        try {
            if (newCurrentIndex != null && trackList.get(newCurrentIndex) != null) {
                currentTrackIndex = newCurrentIndex;
            } else {
                currentTrackIndex = 0;
            }
            createMediaPlayerIfNeeded();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(trackList.get(currentTrackIndex).getUrl());
            mState = State.Preparing;
            showNotification("Loading...", trackList.get(currentTrackIndex).getTitle());
            mMediaPlayer.prepareAsync();
        }
        catch (IOException ex) {
            Log.e("MusicService", "IOException playing next song: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showNotification (String text, String title) {
        int notificationId = 999;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_play)
                        .setContentTitle(title)
                        .setContentText(text);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, mBuilder.build());
    }

    public void onPrepared(MediaPlayer player) {
        configAndStartMediaPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mState = State.Retrieving;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
    }
}
