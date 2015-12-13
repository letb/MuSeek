package com.letb.museek.Requests;

import com.letb.museek.Models.Track.Track;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

public class TrackRequest extends RetrofitSpiceRequest <Track, TrackInterface> {

    String token;
    String trackId;
    String method;

    public TrackRequest(String token, String method, String trackId) {
        super(Track.class, TrackInterface.class);
        this.token = token;
        this.method = method;
        this.trackId = trackId;
    }



    @Override
    public Track loadDataFromNetwork() throws Exception {
        Ln.d("Request track info");
        Track test = getService().getTrack(token, method, trackId);

        return test;
    }

}
