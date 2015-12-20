package com.letb.museek.Events;


import org.json.JSONObject;

/**
 * Created by dannie on 20.12.15.
 */
public class PlaylistEventSuccess {
        private JSONObject data;

        public PlaylistEventSuccess(JSONObject data) {
            this.data = data;
        }

        public JSONObject getData() {
            return data;
        }
}
