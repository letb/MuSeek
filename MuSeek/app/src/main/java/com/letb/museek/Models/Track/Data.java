package com.letb.museek.Models.Track;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable{


    @SerializedName("track_id")
    private String mID;

    @SerializedName("artist")
    private String mArtist;

    @SerializedName("track")
    private String mTrack;

    @SerializedName("length")
    private int mLength;

    @SerializedName("bitrate")
    private int mBitrate;

    @SerializedName("size")
    private String mSize;

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getmTrack() {
        return mTrack;
    }

    public void setmTrack(String mTrack) {
        this.mTrack = mTrack;
    }

    public int getmBitrate() {
        return mBitrate;
    }

    public void setmBitrate(int mBitrate) {
        this.mBitrate = mBitrate;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public int getmLength() {
        return mLength;
    }

    public void setmLength(int mLength) {
        this.mLength = mLength;
    }

}

