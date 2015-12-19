package com.letb.museek.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
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

    // These are the Intent actions that we are prepared to handle. Notice that the fact these
    // constants exist in our class is a mere convenience: what really defines the actions our
    // service can handle are the <action> tags in the <intent-filters> tag for our service in
    // AndroidManifest.xml.
    public static final String ACTION_TOGGLE_PLAYBACK =
            "com.letb.museek.musicplayer.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY = "com.letb.museek.musicplayer.action.PLAY";
    public static final String ACTION_PAUSE = "com.letb.museek.musicplayer.action.PAUSE";
    public static final String ACTION_STOP = "com.letb.museek.musicplayer.action.STOP";
    public static final String ACTION_SKIP = "com.letb.museek.musicplayer.action.SKIP";
    public static final String ACTION_REWIND = "com.letb.museek.musicplayer.action.REWIND";
    public static final String ACTION_URL = "com.letb.museek.musicplayer.action.URL";

    public static final String PLAYLIST = "com.letb.museek.musicplayer.data.PLAYLIST";

    private MediaPlayer mMediaPlayer = null;    // The Media Player
    private List<Track> trackList = null;
    private Integer currentTrackIndex = 0;


    // indicates the state our service:
    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Playing, // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused
        // playback paused (media player ready!)
    };

    State mState = State.Stopped;

    @Override
    public void onCreate() {
    }

    /**
     * Makes sure the media player exists and has been reset. This will create the media player
     * if needed, or reset the existing media player if one already exists.
     */
    void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            // Make sure the media player will acquire a wake-lock while playing. If we don't do
            // that, the CPU might go to sleep while the song is playing, causing playback to stop.
            //
            // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
            // permission in AndroidManifest.xml.
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
                processTogglePlaybackRequest();
                break;
            case ACTION_PLAY:
                trackList = (List<Track>) intent.getSerializableExtra(PLAYLIST);
                processPlayRequest();
                break;
            case ACTION_PAUSE:
                processPauseRequest();
                break;
        }
        return START_NOT_STICKY; // Means we started the service, but don't want it to
        // restart in case it's killed.
    }

    void processTogglePlaybackRequest() {
        if (mState == State.Paused || mState == State.Stopped) {
            processPlayRequest();
        } else {
            processPauseRequest();
        }
    }

    void processPlayRequest() {
        if (mState == State.Stopped) {
            // If we're stopped, just go ahead to the next song and start playing
            playNextSong(0);
        }
        else if (mState == State.Paused) {
            // If we're paused, just continue playback and restore the 'foreground service' state.
            mState = State.Playing;
//            setUpAsForeground(mSongTitle + " (playing)");
            configAndStartMediaPlayer();
        }
    }

    void processPauseRequest() {
        if (mState == State.Playing) {
            // Pause media player and cancel the 'foreground service' state.
            mState = State.Paused;
            mMediaPlayer.pause();
            relaxResources(false); // while paused, we always retain the MediaPlayer
            // do not give up audio focus
        }
    }

    /**
     * Releases resources used by the service for playback. This includes the "foreground service"
     * status and notification, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also be released or not
     */
    void relaxResources(boolean releaseMediaPlayer) {
        // stop being a foreground service
        stopForeground(true);
        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    void configAndStartMediaPlayer() {
        if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
    }

    void playNextSong(Integer newCurrentIndex) {
        mState = State.Stopped;
        relaxResources(false); // release everything except MediaPlayer
        try {
            if (newCurrentIndex != null) {
                currentTrackIndex = newCurrentIndex;
                createMediaPlayerIfNeeded();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(trackList.get(currentTrackIndex).getUrl());
            }
            mState = State.Preparing;
//            setUpAsForeground(mSongTitle + " (loading)");
            mMediaPlayer.prepareAsync();
        }
        catch (IOException ex) {
            Log.e("MusicService", "IOException playing next song: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /** Called when media player is done preparing. */
    public void onPrepared(MediaPlayer player) {
        // The media player is done preparing. That means we can start playing!
        mState = State.Playing;
//        updateNotification(mSongTitle + " (playing)");
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


}
