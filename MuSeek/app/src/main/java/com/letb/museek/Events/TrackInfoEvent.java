package com.letb.museek.Events;

import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by eugene on 29.12.15.
 */
public class TrackInfoEvent {
    private ArrayList<JsonElement> tracks;

    public TrackInfoEvent(ArrayList<JsonElement> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<JsonElement> getTracks() {
        return tracks;
    }
}
