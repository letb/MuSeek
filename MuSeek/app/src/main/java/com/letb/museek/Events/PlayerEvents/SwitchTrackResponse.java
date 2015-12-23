package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Services.MediaPlayerService;

/**
 * Created by eugene on 23.12.15.
 */
public class SwitchTrackResponse extends PlayerResponseEvent {
    public SwitchTrackResponse(Integer position, MediaPlayerService.State state) {
        super(position, state);
    }
}
