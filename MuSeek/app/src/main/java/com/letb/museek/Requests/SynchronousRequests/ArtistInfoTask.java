package com.letb.museek.Requests.SynchronousRequests;

import com.google.gson.JsonElement;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Events.ArtistInfoEvent;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.RequestProcessor.Lastfm_SynchronousRequestProcessor;
import com.letb.museek.RequestProcessor.Pleer_SynchronousRequestProcessor;
import com.letb.museek.Requests.ArtistInterface;
import com.letb.museek.Requests.AsynchronousRequests.TrackUrlRequest;
import com.letb.museek.Requests.TrackUrlInterface;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Создали экземпляр этого класса внутри активити
 * onResume, например
 * И вызвали у него run()
 */
public class ArtistInfoTask implements Runnable {
    private List<Artist> artistList;
    private EventBus bus = EventBus.getDefault();
    private ArrayList<JsonElement> artistInfos = new ArrayList<>();

    public ArtistInfoTask(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public String method = "artist.getInfo";
    public String api_key = "a39a62a55486131cc5ae5588670f594b";
    public String format = "json";

    @Override
    public void run() {
        ArtistInterface artistService = Lastfm_SynchronousRequestProcessor.createService(ArtistInterface.class);
        for (Artist artist : artistList) {
            JsonElement jsonArtist = artistService.getInfo(
                    method,
                    artist.getName(),
                    api_key,
                    format
            );
            artistInfos.add(jsonArtist);
        }
        sendResult();
    }

    private void sendResult() {
        bus.post(new ArtistInfoEvent(artistInfos));
    }
}
