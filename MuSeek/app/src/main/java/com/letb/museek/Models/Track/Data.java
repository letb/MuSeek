package com.letb.museek.Models.Track;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable{

    public Data() {};

    public Data(String id, String artist, String track, int length, int bitrate) {
        this.id = id;
        this.artist = artist;
        this.track = track;
        this.length = length;
        this.bitrate = bitrate;
    }

    @SerializedName("track_id")
    private String id;

    @SerializedName("position")
    private String position;

    @SerializedName("artist")
    private String artist;

    @SerializedName("track")
    private String track;

    @SerializedName("lenght")
    private int length;

    @SerializedName("bitrate")
    private int bitrate;

    @SerializedName("size")
    private String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

