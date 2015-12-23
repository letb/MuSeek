package com.letb.museek.Requests;

import com.google.gson.JsonElement;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import org.json.JSONObject;

import roboguice.util.temp.Ln;

/**
 * Created by dannie on 19.12.15.
 */
public class PlaylistRequest extends RetrofitSpiceRequest<JsonElement, PlaylistInterface> {

    private enum Period {
        WEEK, MONTH, QUARTER, HALFYEAR, YEAR;
    }

    static private String METHOD_GET_TOP_LIST = "get_top_list";
    static private String METHOD_TRACKS_SEARCH = "tracks_search";
    static private String ENG_LANG = "en";
    static private String RU_LANG = "ru";

    private String method;

    private String token;
    private int timePeriod;
    private int page;
    private String language;

    private String query;
    private int resultsOnPage;
    private String quality;


    public PlaylistRequest(String token, int timePeriod, int page, String language) {
        super(JsonElement.class, PlaylistInterface.class);

        this.method = METHOD_GET_TOP_LIST;

        this.token = token;
        this.timePeriod = timePeriod;
        this.page = page;
        this.language = language;

    }

    public PlaylistRequest(String token, String query, int resultsOnPage, String quality) {
        super(JsonElement.class, PlaylistInterface.class);

        this.method = METHOD_TRACKS_SEARCH;

        this.token = token;
        this.query = query;
        this.resultsOnPage = resultsOnPage;
        this.quality = quality;

    }


    @Override
    public JsonElement loadDataFromNetwork() throws Exception {
        Ln.d("Request top tracks list");
        if (method == METHOD_GET_TOP_LIST) {
            return getService().getTopTracks(token, method, timePeriod, page, language);
        } else { // (method == METHOD_TRACKS_SEARCH)
            return getService().getSearchResult(token, method, query, resultsOnPage, quality);
        }
    }

}
