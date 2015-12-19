package com.letb.museek.Models.Track;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Track implements Serializable{

    @SerializedName("url")
    private String url;

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private Data data;

    public Track() {
        data = new Data();
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return data.getArtist() + " – " + data.getTrack();
    }

    public void setTitle(String artist, String track) {
        data.setArtist(artist);
        data.setTrack(track);
    }
}
