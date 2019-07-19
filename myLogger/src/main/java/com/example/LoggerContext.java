package com.example;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 *
 */
public class LoggerContext  implements ILoggerFactory {

    @Override
    public Logger getLogger(String s) {
        return new com.example.Logger();
    }
}
