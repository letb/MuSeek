package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Models.Track.Track;
import com.letb.museek.Services.MediaPlayerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 23.12.15.
 */
public class PlayerResponseEvent {
    private Integer trackIndex;
    private MediaPlayerService.State state;
    private List<Track> currentTrackList;

    public PlayerResponseEvent(Integer position,
                               MediaPlayerService.State state,
                               List<Track> currentTrackList) {
        this.trackIndex = position;
        this.state = state;
        this.currentTrackList = currentTrackList;
    }

    public Integer getTrackIndex() {
        return trackIndex;
    }

    public MediaPlayerService.State getState() {
        return state;
    }

    public List<Track> getCurrentTrackList() {
        return currentTrackList;
    }
}
