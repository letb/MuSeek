package com.letb.museek.Events;


import com.google.gson.JsonElement;

/**
 * Created by dannie on 20.12.15.
 */
public class PlaylistEventSuccess {
    private JsonElement data;
    private String EVENT_CONTENTS;

    public PlaylistEventSuccess(JsonElement data, String event_data_contents) {
        this.data = data;
        EVENT_CONTENTS = event_data_contents;
    }

    public JsonElement getData() {
            return data;
        }

    public String getEVENT_CONTENTS() {
        return EVENT_CONTENTS;
    }
}
