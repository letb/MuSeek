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
    static private String ENG_LANG = "en";
    static private String RU_LANG = "ru";

    private String token;
    private int timePeriod;
    private int page;
    private String language;


    public PlaylistRequest(String token, int timePeriod, int page, String language) {
        super(JsonElement.class, PlaylistInterface.class);

        this.token = token;
        this.timePeriod = timePeriod;
        this.page = page;
        this.language = language;

    }


    @Override
    public JsonElement loadDataFromNetwork() throws Exception {
        Ln.d("Request top tracks list");
        return getService().getTopTracks(token, METHOD_GET_TOP_LIST, timePeriod, page, language);
    }

}
