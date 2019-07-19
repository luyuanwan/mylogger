package com.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试类
 */
public class TestClass {

    static Logger logger = LoggerFactory.getLogger(TestClass.class);

    public static void main(String[] args) {
        logger.error("hello world");
    }
}
