package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Services.MediaPlayerService;

/**
 * Created by eugene on 23.12.15.
 */
public abstract class PlayerResponseEvent {
    private Integer trackIndex;
    private MediaPlayerService.State state;
    public PlayerResponseEvent(Integer position, MediaPlayerService.State state) {
        this.trackIndex = position;
        this.state = state;
    }

    public Integer getTrackIndex() {
        return trackIndex;
    }

    public MediaPlayerService.State getState() {
        return state;
    }
}
