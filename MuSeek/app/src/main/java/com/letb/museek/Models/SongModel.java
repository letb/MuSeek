package com.letb.museek.Models;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by eugene on 13.12.15.
 */
//TODO: Parcelable
public class SongModel implements Serializable {
    private String title;

    private String _url;

    public String getUrl() {
        return _url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void set_url(String _url) {
        this._url = _url;
    }
}