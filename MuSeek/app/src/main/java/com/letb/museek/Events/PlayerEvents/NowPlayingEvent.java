package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Models.Track.Track;

/**
 * Created by dannie on 30.12.15.
 */
public class NowPlayingEvent {
    private Track track;

    public NowPlayingEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
