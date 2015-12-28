package com.letb.museek.Events;

import com.google.gson.JsonElement;

import java.util.ArrayList;

/**
 * Created by eugene on 28.12.15.
 */
public class ArtistInfoEvent {
    private ArrayList<JsonElement> artists;

    public ArtistInfoEvent(ArrayList<JsonElement> artists) {
        this.artists = artists;
    }

    public ArrayList<JsonElement> getArtists() {
        return artists;
    }
}
