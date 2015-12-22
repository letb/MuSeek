package com.letb.museek.Events;

/**
 * Created by dannie on 22.12.15.
 */
public class TrackUrlEventSuccess {
    private String data;

    public TrackUrlEventSuccess(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }
}
