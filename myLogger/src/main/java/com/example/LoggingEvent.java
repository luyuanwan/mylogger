package com.example;

import org.slf4j.Marker;

/**
 * 日志发出事件
 */
public class LoggingEvent implements ILoggingEvent {

    private Marker marker;

    private Throwable throwable;

    private Object[] argumentArray;

    public LoggingEvent(Marker marker, Throwable throwable, Object[] argumentArray, String message) {
        this.marker = marker;
        this.throwable = throwable;
        this.argumentArray = argumentArray;
        this.message = message;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    private String message;

    @Override
    public Object[] getArgumentArray() {
        return argumentArray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
