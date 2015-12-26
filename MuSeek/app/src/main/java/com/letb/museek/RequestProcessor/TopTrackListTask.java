package com.letb.museek.RequestProcessor;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Requests.PlaylistInterface;
import com.letb.museek.Requests.PlaylistRequest;

import de.greenrobot.event.EventBus;

/**
 * Created by eugene on 26.12.15.
 */
public class TopTrackListTask implements Runnable {
    private EventBus bus = EventBus.getDefault();
    private JsonElement tracks;
    /**
     * Можно положить en, ru или что угодно и
     * разбирать потом результат в активити по этому тегу
     */
    public static final String TOP_EVENT = "TOP_EVENT";

    private String EVENT_CONTENTS;

    public TopTrackListTask(String event_contents) {
        EVENT_CONTENTS = event_contents;
    }

    @Override
    public void run() {
        PlaylistInterface trackService = SynchronousRequestProcessor.createService(PlaylistInterface.class);
        /**
         * Some default stuff
         */
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
        bus.post(new PlaylistEventSuccess(tracks, EVENT_CONTENTS));
    }
}
