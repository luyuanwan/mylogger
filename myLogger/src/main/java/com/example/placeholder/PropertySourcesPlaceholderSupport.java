package com.example.placeholder;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 属性替换placehodler支持类类
 */
public abstract class PropertySourcesPlaceholderSupport {

    /** Default placeholder prefix: {@value} */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /** Default placeholder suffix: {@value} */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /** Default value separator: {@value} */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";


    /** Defaults to {@value #DEFAULT_PLACEHOLDER_PREFIX} */
    protected String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    /** Defaults to {@value #DEFAULT_PLACEHOLDER_SUFFIX} */
    protected String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

    /** Defaults to {@value #DEFAULT_VALUE_SEPARATOR} */
    protected String valueSeparator = DEFAULT_VALUE_SEPARATOR;

    protected boolean ignoreUnresolvablePlaceholders = false;


    /**
     * 很多属性源
     */
    private PropertySources propertySources;

    /**
     * 设置属性
     */
    public void setPropertySources(PropertySources propertySources) {
        this.propertySources = propertySources;
    }


    /**
     * 包含占位符的字符串，将《解析算法带入》
     */
    protected String replacePlaceholders(String value/**包含占位符的字符串*/, PlaceholderResolver placeholderResolver/**占位符解析器*/) {
        //判定字符串是否为空
        Assert.notNull(value, "'value' must not be null");

        //解析字符串
        return parseStringValue(value/**包含占位符的字符串*/, placeholderResolver/**占位符解析器*/, new HashSet<String>());
    }



    /**
     * 解析字符串
     *
     * 这种设计的好处：
     * 1、如果需要解析不同的《包含占位符的字符串》，则本函数可以复用
     * 2、如果解析占位符的算法发生变化，则本函数可以复用
     * 3、如果解析占位符的前后缀发生变化，则本函数可以复用，记得使用构造函数
     *
     * @param strVal                   包含占位符的字符串
     * @param placeholderResolver      占位符解析器
     * @param visitedPlaceholders
     * @return
     */
    protected String parseStringValue(
            String strVal/*包含占位符的字符串*/, PlaceholderResolver placeholderResolver/*占位符解析器*/,
            Set<String> visitedPlaceholders/*集合*/) {

        //将<包含占位符的字符串>生成string
        StringBuilder result = new StringBuilder(strVal);

        //查找前缀符的位置
        int startIndex = strVal.indexOf(this.placeholderPrefix);
        while (startIndex != -1) {
            //找到了前缀位置

            //找后缀位置
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if (endIndex != -1) {
                //找到了后缀位置

                //准备替换的部分，例如${xx}中的xx
                String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;

                //添加到集合中
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException(
                            "Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder/*准备替换的部分*/, placeholderResolver/*占位符解析器*/, visitedPlaceholders/*集合*/);
                // Now obtain the value for the fully resolved key...
                String propVal = placeholderResolver.resolvePlaceholder(placeholder);/*占位符解析器解析占位符*/
                //propVal==null 表示解析不成功
                if (propVal == null && this.valueSeparator != null) {
                    //解析不成功 且 有默认值的分隔符
                    int separatorIndex = placeholder.indexOf(this.valueSeparator);
                    if (separatorIndex != -1) {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);
                        String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
                        propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
                    result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                }
                else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                }
                else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" +
                            placeholder + "'" + " in string value \"" + strVal + "\"");
                }
                visitedPlaceholders.remove(originalPlaceholder);
            }
            else {
                startIndex = -1;
            }
        }

        return result.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                }
                else {
                    return index;
                }
            }
            else if (StringUtils.substringMatch(buf, index, this.placeholderPrefix)) {
                withinNestedPlaceholder++;
                index = index + this.placeholderPrefix.length();
            }
            else {
                index++;
            }
        }
        return -1;
    }


    /**
     * Strategy interface used to resolve replacement values for placeholders contained in Strings.
     */
    public static interface PlaceholderResolver {

        /**
         * Resolve the supplied placeholder name to the replacement value.
         * @param placeholderName the name of the placeholder to resolve
         * @return the replacement value, or {@code null} if no replacement is to be made
         */
        String resolvePlaceholder(String placeholderName);
    }
}
