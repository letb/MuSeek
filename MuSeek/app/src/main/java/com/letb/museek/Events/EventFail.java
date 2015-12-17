package com.letb.museek.Events;

/**
 * Created by eugene on 18.12.15.
 */
public class EventFail {
    private String exception;

    public EventFail(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }
}
