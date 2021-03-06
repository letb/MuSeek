package com.letb.museek.Requests.AsynchronousRequests;

import com.google.gson.JsonElement;
import com.letb.museek.Requests.TrackUrlInterface;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by dannie on 22.12.15.
 */
public class TrackUrlRequest extends RetrofitSpiceRequest<String, TrackUrlInterface> {

    public static final String METHOD_GET_TRACK_URL = "tracks_get_download_link";
    private String token;
    private String trackId;
    private String reason;

    public TrackUrlRequest(String token, String trackId, String reason) {
        super(String.class, TrackUrlInterface.class);

        this.token = token;
        this.trackId = trackId;
        this.reason = reason;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        JsonElement jsonUrl = getService().getTrackUrl(token, METHOD_GET_TRACK_URL, trackId, reason);
        Ln.d(jsonUrl + "TRACK_URL");
        return jsonUrl.getAsJsonObject().get("url").getAsString();
    }
}
