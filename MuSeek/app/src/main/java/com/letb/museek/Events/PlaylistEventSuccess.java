package com.letb.museek.Events;


import com.google.gson.JsonElement;

import org.json.JSONObject;

/**
 * Created by dannie on 20.12.15.
 */
public class PlaylistEventSuccess {
        private JsonElement data;

        public PlaylistEventSuccess(JsonElement data) {
            this.data = data;
        }

        public JsonElement getData() {
            return data;
        }
}
