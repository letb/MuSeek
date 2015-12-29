package com.letb.museek.Requests.SynchronousRequests;

import com.google.gson.JsonElement;
import com.letb.museek.Events.ArtistInfoEvent;
import com.letb.museek.Events.TrackInfoEvent;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.RequestProcessor.Lastfm_SynchronousRequestProcessor;
import com.letb.museek.Requests.ArtistInterface;
import com.letb.museek.Requests.TrackInterface;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by eugene on 28.12.15.
 */
public class TrackInfoTask implements Runnable {
    private List<Track> trackList;
    private EventBus bus = EventBus.getDefault();
    private ArrayList<JsonElement> trackInfos = new ArrayList<>();

    public TrackInfoTask(List<Track> trackList) {
        this.trackList = trackList;
    }

    private final String method = "track.getInfo";
    private final String api_key = "a39a62a55486131cc5ae5588670f594b";
    private final String format = "json";
    private final String autocorrect = "1";

    @Override
    public void run() {
        TrackInterface trackService = Lastfm_SynchronousRequestProcessor.createService(TrackInterface.class);
        for (Track track : trackList) {
            JsonElement jsonArtist = trackService.getInfo(
                    method,
                    track.getData().getArtist(),
                    track.getTitle(),
                    api_key,
                    autocorrect,
                    format
            );
            trackInfos.add(jsonArtist);
        }
        sendResult();
    }

    private void sendResult() {
        bus.post(new TrackInfoEvent(trackInfos));
    }
}
