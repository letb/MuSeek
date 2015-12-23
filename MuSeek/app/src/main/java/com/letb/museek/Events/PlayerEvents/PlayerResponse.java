package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Services.MediaPlayerService;

/**
 * Created by eugene on 23.12.15.
 */
public class PlayerResponse extends PlayerResponseEvent {

    public PlayerResponse(Integer position, MediaPlayerService.State state) {
        super(position, state);
    }
}
