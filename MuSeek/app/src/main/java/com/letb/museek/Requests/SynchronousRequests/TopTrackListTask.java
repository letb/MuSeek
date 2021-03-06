package com.letb.museek.Requests.SynchronousRequests;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.RequestProcessor.Pleer_SynchronousRequestProcessor;
import com.letb.museek.Requests.PlaylistInterface;
import com.letb.museek.Requests.AsynchronousRequests.PlaylistRequest;

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
    public static final String EN_LIST = "EN_LIST";
    public static final String RU_LIST = "RU_LIST";

    private String EVENT_CONTENTS;

    public TopTrackListTask(String event_contents) {
        EVENT_CONTENTS = event_contents;
    }

    @Override
    public void run() {
        PlaylistInterface trackService = Pleer_SynchronousRequestProcessor.createService(PlaylistInterface.class);
        /**
         * Some default stuff
         */
        tracks = trackService.getTopTracks(
                TokenHolder.getAccessToken(),
                PlaylistRequest.METHOD_GET_TOP_LIST,
                5,
                1,
                PlaylistRequest.ENG_LANG
        );
        sendResult();
    }

    private void sendResult() {
        bus.post(new PlaylistEventSuccess(tracks, EVENT_CONTENTS));
    }
}
