package com.letb.museek.Events.PlayerEvents;

/**
 * Created by eugene on 27.12.15.
 */
public class RewindTractToPositionRequest {
    private Integer position;

    public RewindTractToPositionRequest(Integer position) {
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }
}
