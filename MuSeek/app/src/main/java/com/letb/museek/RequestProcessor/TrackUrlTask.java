package com.letb.museek.RequestProcessor;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.Requests.TrackUrlInterface;
import com.letb.museek.Requests.TrackUrlRequest;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Создали экземпляр этого класса внутри активити
 * onResume, например
 * И вызвали у него run()
 */
public class TrackUrlTask implements Runnable {
    private List<Track> trackList;
    private EventBus bus = EventBus.getDefault();
    private ArrayList<String> trackUrls;

    public TrackUrlTask(List<Track> trackList) {
        this.trackList = trackList;
        bus.register(this);
    }

    @Override
    public void run() {
        TrackUrlInterface trackService = SynchronousRequestProcessor.createService(TrackUrlInterface.class);
        for (Track track : trackList) {
            JsonElement jsonUrl = trackService.getTrackUrl(
                    TokenHolder.getAccessToken(),
                    TrackUrlRequest.METHOD_GET_TRACK_URL,
                    track.getData().getId(),
                    "Listen"
            );
            trackUrls.add(jsonUrl.getAsJsonObject().get("url").getAsString());
        }
        sendResult();
    }

    private void sendResult() {
        List <Track> resultTrackList = new ArrayList<>(trackList.size());
        for (int i = 0; i < trackUrls.size(); i++) {
            Track trackToInsertUrl = trackList.get(i);
            trackToInsertUrl.setUrl(trackUrls.get(i));
            resultTrackList.add(trackToInsertUrl);
        }

        // Где-то здесь должен быть окончательный bus.post() с результатом


        bus.unregister(this);
    }
}
