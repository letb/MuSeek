package com.letb.museek.Models;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by eugene on 13.12.15.
 */
//TODO: Parcelable
public class TrackModel implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
