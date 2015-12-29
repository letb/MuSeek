package com.letb.museek.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.letb.museek.Events.PlayerEvents.PlayerResponseEvent;
import com.letb.museek.Events.PlayerEvents.RewindTractToPositionRequest;
import com.letb.museek.Events.PlayerEvents.SwitchTrackRequest;
import com.letb.museek.Events.PlayerEvents.TogglePlayPauseRequest;
import com.letb.museek.Events.TrackUrlEventSuccess;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.R;
import com.letb.museek.RequestProcessor.AsynchronousRequestProcessor;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

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

    public enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Playing, // playback active (media player ready!). (but the media player may actually be
        Paused
    };

    private MediaPlayer mMediaPlayer = null;    // The Media Player
    private List<Track> trackList = null;
    private Integer currentTrackIndex = 0;
    private State currentState = State.Stopped;

    private EventBus bus = EventBus.getDefault();

    private void changeState(State state) {
        this.currentState = state;
        bus.post(new PlayerResponseEvent(currentTrackIndex, currentState, trackList));
    }

    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        else
            mMediaPlayer.reset();
    }

    public void onEvent(SwitchTrackRequest event){
        int newCurrentIndex = currentTrackIndex + event.getDirection();
        if ((newCurrentIndex < 0))
            currentTrackIndex = trackList.size() - 1;
        else if (newCurrentIndex > trackList.size() - 1)
            currentTrackIndex = 0;
        else
            currentTrackIndex = newCurrentIndex;
        requestTrack();
    }

    public void onEvent(RewindTractToPositionRequest event){
        if (
                (event.getPosition() > 0) &&
                (event.getPosition() < trackList.get(currentTrackIndex).getData().getLength())) {
            processRewindRequest(event.getPosition());
        }
        else
            processRewindRequest(0);
    }

    public void onEvent(TogglePlayPauseRequest event){
        processTogglePlayPauseRequest();
    }

    public void onEvent(TrackUrlEventSuccess event){
        trackList.get(currentTrackIndex).setUrl(event.getData());
        processPlayRequest();
    }

    private void requestTrack() {
        AsynchronousRequestProcessor.startTrackUrlRequestAction(
                this,
                trackList.get(currentTrackIndex).getData().getId(),
                "Listen"
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY:
                trackList = (List<Track>) intent.getSerializableExtra(PLAYLIST);
                currentTrackIndex = intent.getIntExtra(SELECTED_TRACK_INDEX, 0);
                requestTrack();
                break;
        }
        return START_NOT_STICKY;
    }

    private void processTogglePlayPauseRequest() {
        if (currentState == State.Paused || currentState == State.Stopped) {
            processPlayRequest();
        } else {
            processPauseRequest();
        }
    }

    private void processRewindRequest(Integer position) {
        int milliseconds = 1000;
        if (currentState == State.Playing || currentState == State.Paused)
            mMediaPlayer.seekTo(position * milliseconds);
    }

    private void processPlayRequest() {
        if (
                (currentState == State.Stopped) ||
                (currentState == State.Playing))
        {
            playByIndex();
        }
        else if (currentState == State.Paused) {
            configAndStartMediaPlayer();
        }
//        changeState(State.Playing);
        showNotification("playing...", trackList.get(currentTrackIndex).getTitle());
    }

    private void processPauseRequest() {
        changeState(State.Paused);
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
            changeState(State.Playing);
            showNotification("playing...", trackList.get(currentTrackIndex).getTitle());
            mMediaPlayer.start();
        }
    }

    private void playByIndex() {
        changeState(State.Stopped);
        relaxResources(false);
        try {
            createMediaPlayerIfNeeded();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(trackList.get(currentTrackIndex).getUrl());
            changeState(State.Preparing);
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
        if (mMediaPlayer != null)
            mMediaPlayer.release();

        changeState(State.Retrieving);
        bus.unregister(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        bus.register(this);
    }
}
