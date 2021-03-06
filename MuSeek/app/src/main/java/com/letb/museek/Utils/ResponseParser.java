package com.letb.museek.Utils;

import com.google.gson.JsonElement;
import com.letb.museek.Models.Artist;
import com.letb.museek.Models.Track.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dannie on 20.12.15.
 */
public class ResponseParser {
    static private Track jsonToTrack(JSONObject jsonTrack) throws JSONException {
        String id = jsonTrack.getString("id");
        String artist = jsonTrack.getString("artist");
        String track = jsonTrack.getString("track");
        int length = jsonTrack.getInt("lenght");
        String bitrate = jsonTrack.getString("bitrate");
        // TODO: вынести константы

        return new Track(id, artist, track, length, bitrate);
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

    public static ArrayList<Artist> parseArtistInfoResponse (ArrayList<JsonElement> artistsAsJson) {
        ArrayList<Artist> artists = new ArrayList<>();
        for (JsonElement jsonArtist : artistsAsJson) {
            JSONObject artistObject = null;
            try {
                artistObject = new JSONObject(jsonArtist.toString());
                // FIXME: 28.12.15 WTF?
                artistObject = artistObject.getJSONObject("artist");
                List<String> images = getImages(artistObject.getJSONArray("image"));
                Artist artistFromJson = new Artist(artistObject.getString("name"));
                artistFromJson.setImagesSizesAsc(images);
                artists.add(artistFromJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return artists;
    }

    public static ArrayList<Track> parseTrackInfoResponse (ArrayList<JsonElement> tracksAsJson, ArrayList<Track> trackList)  {

            int i = 0;
            for (JsonElement jsonTrack : tracksAsJson) {
                JSONObject trackObject = null;
                try {
                    trackObject = new JSONObject(jsonTrack.toString());
                    trackObject = trackObject.getJSONObject("track");
                    List<String> images = getImages(trackObject.getJSONObject("album").getJSONArray("image"));
                    trackList.get(i).setImagesSizesAsc(images);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ++i;
            }
        return trackList;
    }

    private static List<String> getImages (JSONArray jsonImages) throws JSONException {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < jsonImages.length(); ++i) {
            images.add(jsonImages.getJSONObject(i).getString("#text"));
        }
        return images;
    }



}
