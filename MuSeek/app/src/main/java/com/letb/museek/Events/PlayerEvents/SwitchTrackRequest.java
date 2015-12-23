package com.letb.museek.Events.PlayerEvents;

import com.letb.museek.Services.MediaPlayerService;

/**
 * Created by eugene on 23.12.15.
 */
public class SwitchTrackRequest {
    private Integer direction;

    public SwitchTrackRequest(Integer direction) {
        this.direction = direction;
    }

    public Integer getDirection() {
        return direction;
    }
}
