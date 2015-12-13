package com.letb.museek.Entities.Track;

import com.google.gson.annotations.SerializedName;
import com.letb.museek.Entities.Track.Data;

public class Track {

    public static String getTrackInfoMethod = "tracks_get_info";

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private Data data;

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
}