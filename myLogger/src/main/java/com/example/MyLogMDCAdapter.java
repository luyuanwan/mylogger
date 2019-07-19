package com.example;

import org.slf4j.spi.MDCAdapter;

import java.util.Map;

/**
 * MyLogMDCAdapter
 */
public class MyLogMDCAdapter implements MDCAdapter {

    @Override
    public void put(String s, String s1) {

    }

    @Override
    public String get(String s) {
        return null;
    }

    @Override
    public void remove(String s) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        return null;
    }

    @Override
    public void setContextMap(Map<String, String> map) {

    }
}
