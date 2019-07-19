package com.example.placeholder;

import java.util.Properties;

/**
 * 占位符替换帮助类，对外暴露使用方法
 */
public class PropertySourcesPlaceholderHelper extends PropertySourcesPlaceholderSupport{

    /**
     * 从properties中获取替换的值
     */
    public String replacePlaceholders(String value, Properties properties) {
        if(properties == null){
            properties = new Properties();
        }
        final Properties p = properties;
        return replacePlaceholders(value, new PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                //获取键值对中的值
                return p.getProperty(placeholderName);
            }
        });
    }
}
