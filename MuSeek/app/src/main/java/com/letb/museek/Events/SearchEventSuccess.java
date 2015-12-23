package com.letb.museek.Events;

import com.google.gson.JsonElement;

/**
 * Created by dannie on 23.12.15.
 */
public class SearchEventSuccess {
    private JsonElement data;

    public SearchEventSuccess(JsonElement data) {
        this.data = data;
    }

    public JsonElement getData() {
        return data;
    }
}
