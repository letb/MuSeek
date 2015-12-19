package com.letb.museek.Requests;

import android.util.Log;

import com.letb.museek.Models.Playlist;
import com.letb.museek.Models.Token;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by dannie on 19.12.15.
 */
public class PlaylistRequest extends RetrofitSpiceRequest<Playlist, PlaylistInterface> {

    private enum Period {
        WEEK, MONTH, QUARTER, HALFYEAR, YEAR;
    }
    static private String methodGetTopList = "get_top_list ";
    static private String engLang = "en";
    static private String ruLang = "ru";

    private String token;
    private int timePeriod;
    private int page;
    private String language;


    public PlaylistRequest(String token, int timePeriod, int page, String language) {
        super(Playlist.class, PlaylistInterface.class);

        this.token = token;
        this.timePeriod = timePeriod;
        this.page = page;
        this.language = language;

    }


    @Override
    public Playlist loadDataFromNetwork() throws Exception {
        Ln.d("Request top tracks list");
        Playlist playlist = getService().getTopTracks(token, methodGetTopList, timePeriod, page, language);

        return playlist;
    }

}
