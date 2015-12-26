package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Services.MediaPlayerService;

/**
 * Created by eugene on 23.12.15.
 */
public class StatePlayingResponse extends PlayerResponseEvent {
    public StatePlayingResponse(Integer position, MediaPlayerService.State state) {
        super(position, state);
    }
}
