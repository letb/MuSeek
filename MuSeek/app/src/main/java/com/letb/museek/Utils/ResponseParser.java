package com.letb.museek.Utils;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.letb.museek.Entities.TokenHolder;
import com.letb.museek.Models.Track.Track;
import com.letb.museek.RequestProcessor.RequestProcessorService;
import com.letb.museek.Requests.TrackRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dannie on 20.12.15.
 */
public class ResponseParser {

    private JsonElement response;

    static private Track jsonToTrack(JSONObject jsonTrack) throws JSONException {
        String id = jsonTrack.getString("id");
        String artist = jsonTrack.getString("artist");
        String track = jsonTrack.getString("track");
        int length = jsonTrack.getInt("lenght");
        String bitrate = jsonTrack.getString("bitrate");
        // TODO: вынести константы

        Track resultTrack = new Track(id, artist, track, length, bitrate);

        return resultTrack;
    }

    static public ArrayList<Track> parsePlaylistResponse(JsonElement jsonElementPlaylist) throws JSONException {
        JSONObject jsonObjectPlaylist  = new JSONObject(jsonElementPlaylist.toString());
        JSONObject jsonTracks = jsonObjectPlaylist.getJSONObject("tracks").getJSONObject("data");
        Iterator<?> keys = jsonTracks.keys();
        ArrayList<Track> trackList = new ArrayList<Track>();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                if (jsonTracks.get(key) instanceof JSONObject) {
                    JSONObject jsonTrack = (JSONObject) jsonTracks.get(key);

                    trackList.add(jsonToTrack(jsonTrack));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trackList;
    }


    static public ArrayList<Track> parseSearchResponse(JsonElement jsonElementPlaylist) throws JSONException {
        JSONObject jsonObjectPlaylist  = new JSONObject(jsonElementPlaylist.toString());
        JSONObject jsonTracks = jsonObjectPlaylist.getJSONObject("tracks");
        Iterator<?> keys = jsonTracks.keys();
        ArrayList<Track> trackList = new ArrayList<Track>();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                if (jsonTracks.get(key) instanceof JSONObject) {
                    JSONObject jsonTrack = (JSONObject) jsonTracks.get(key);

                    trackList.add(jsonToTrack(jsonTrack));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trackList;
    }



}
