package com.letb.museek.Events;

import com.letb.museek.Models.Track.Track;

/**
 * Created by eugene on 18.12.15.
 */
public class TrackEventSuccess {
    private Track data;

    public TrackEventSuccess(Track data){
        this.data = data;
    }

    public Track getData(){
        return data;
    }
}
