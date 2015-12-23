package com.letb.museek.Requests;

import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

public class TrackRequest extends RetrofitSpiceRequest <Track, TrackInterface> {

    static final String METHOD_GET_TRACK_INFO = "tracks_get_info";
    static final String METHOD_GET_TRACK_URL = "tracks_get_download_link";
    private String token;
    private String trackId;
    private String reason;

    public TrackRequest(String token, String trackId, String reason) {
        super(Track.class, TrackInterface.class);

        this.token = token;
        this.trackId = trackId;
        this.reason = reason;
    }

    @Override
    public Track loadDataFromNetwork() throws Exception {
        Ln.d("Request track info");
        Track result = getService().getTrack(token, METHOD_GET_TRACK_INFO, trackId);
        Track url = getService().getTrackUrl(token, METHOD_GET_TRACK_URL, trackId, reason);
        result.setUrl(url.getUrl());
        return result;
    }

}
