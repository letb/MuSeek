package com.letb.museek.RequestProcessor;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.PlaylistEventSuccess;
import com.letb.museek.Events.SearchEventSuccess;
import com.letb.museek.Requests.PlaylistInterface;
import com.letb.museek.Requests.PlaylistRequest;

import de.greenrobot.event.EventBus;

/**
 * Created by eugene on 26.12.15.
 */
public class SearchTrackListTask implements Runnable {
    private EventBus bus = EventBus.getDefault();
    private JsonElement tracks;
    private String query;
    /**
     * разбирать потом результат в активити по этому тегу
     */

    public SearchTrackListTask(String query) {
        this.query = query;
    }

    @Override
    public void run() {
        PlaylistInterface trackService = SynchronousRequestProcessor.createService(PlaylistInterface.class);
        /**
         * Some default stuff
         */
        tracks = trackService.getSearchResult(
                TokenHolder.getAccessToken(),
                PlaylistRequest.METHOD_TRACKS_SEARCH,
                query,
                10,
                "all" // TODO: Whatever this means
        );
        sendResult();
    }

    private void sendResult() {
        bus.post(new SearchEventSuccess(tracks));
    }
}
