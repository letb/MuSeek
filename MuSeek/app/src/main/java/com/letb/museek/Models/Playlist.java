package com.letb.museek.Models;

import com.google.gson.annotations.SerializedName;
import com.letb.museek.Models.Track.Data;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dannie on 19.12.15.
 */
public class Playlist implements Serializable{

    public static String methodGetTopList = "get_top_list";

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private ArrayList<Data> tracks;

    public Playlist() {
        tracks = new ArrayList<Data>();
    }

    public Playlist(JSONObject playlistResponse) {

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Data> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Data> tracks) {
        this.tracks = tracks;
    }
}
