package com.example.appender;

import com.example.ILoggingEvent;

/**
 * 日志的去所
 */
public interface Appender {

    /**
     * 写入日志数据
     * @param event
     */
    void doAppend(ILoggingEvent event);
}
