package com.letb.museek.Requests;

import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

public class TrackRequest extends RetrofitSpiceRequest <Track, TrackInterface> {

    static String methodGetTrackInfo = "tracks_get_info";
    static String methodGetTrackUrl = "tracks_get_download_link";
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
        Track result = getService().getTrack(token, methodGetTrackInfo, trackId);
        Track url = getService().getTrackUrl(token, methodGetTrackUrl, trackId, reason);
        result.setUrl(url.getUrl());
        return result;
    }

}
