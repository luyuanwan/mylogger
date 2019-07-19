package com.example;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * MyLogServiceProvider
 */
public class MyLogServiceProvider implements SLF4JServiceProvider {

    public static String REQUESTED_API_VERSION = "1.8.0"; // !final

    @Override
    public ILoggerFactory getLoggerFactory() {
        return new LoggerContext();
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return null;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return new MyLogMDCAdapter();
    }

    @Override
    public String getRequesteApiVersion() {
        return REQUESTED_API_VERSION;
    }

    @Override
    public void initialize() {

    }
}
