package com.letb.museek.Utils;

import com.letb.museek.Models.Track.Track;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dannie on 20.12.15.
 */
public class PlaylistResponseParser {

    private JSONObject response;

    static void parsePlaylistResponse(JSONObject jsonPlaylist) {
        Iterator<?> keys = jsonPlaylist.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();

            try {
                if (jsonPlaylist.get(key) instanceof JSONObject) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    Iterator<?> keys = jObject.keys();
//
//    while( keys.hasNext() ) {
//        String key = (String)keys.next();
//        if ( jObject.get(key) instanceof JSONObject ) {
//
//        }
//    }
}
