package com.example;

/**
 * 日志发出事件
 */
public interface ILoggingEvent {

    Object[] getArgumentArray();

    String getMessage();

    Throwable getThrowable();
}
