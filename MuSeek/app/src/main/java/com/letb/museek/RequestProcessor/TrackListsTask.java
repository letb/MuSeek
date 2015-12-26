package com.letb.museek.RequestProcessor;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.PlaylistInterface;
import com.letb.museek.Requests.PlaylistRequest;
import com.letb.museek.Requests.TrackUrlInterface;
import com.letb.museek.Requests.TrackUrlRequest;
import com.letb.museek.Utils.ResponseParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by eugene on 26.12.15.
 */
public class TrackListsTask implements Runnable {
    private EventBus bus = EventBus.getDefault();
    private JsonElement tracks;

    public TrackListsTask() {
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        PlaylistInterface trackService = SynchronousRequestProcessor.createService(PlaylistInterface.class);
        tracks = trackService.getTopTracks(
                TokenHolder.getAccessToken(),
                PlaylistRequest.METHOD_GET_TOP_LIST,
                1,
                1,
                PlaylistRequest.ENG_LANG
        );
        sendResult();
    }

    private void sendResult() {
        bus.post(new PlaylistEventSuccess(tracks));
    }
}
