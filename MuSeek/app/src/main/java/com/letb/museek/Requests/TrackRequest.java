package com.letb.museek.Requests;

import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

public class TrackRequest extends RetrofitSpiceRequest <Track, TrackInterface> {

    String token;
    String trackId;
    String info_method;
    String url_method;
    String reason;

    public TrackRequest(String token, String trackId, String reason) {
        super(Track.class, TrackInterface.class);

        this.token = token;
        this.info_method = Track.getTrackInfoMethod;
        this.url_method = Track.getTrackUrlMethod;
        this.trackId = trackId;
        this.reason = reason;
    }

    @Override
    public Track loadDataFromNetwork() throws Exception {
        Ln.d("Request track info");
        Track result = getService().getTrack(token, info_method, trackId);
        Track url = getService().getTrackUrl(token, url_method, trackId, reason);
        result.setUrl(url.getUrl());
        return result;
    }

}
