package com.example.appender;

import com.example.ILoggingEvent;

/**
 * System.out
 */
public class SystemOutAppender implements Appender{

    @Override
    public void doAppend(ILoggingEvent event) {
        String msg = event.getMessage();
        System.out.print(msg);

        Throwable throwable = event.getThrowable();
        if(throwable != null){
            throwable.printStackTrace(System.out);
        }
    }
}
