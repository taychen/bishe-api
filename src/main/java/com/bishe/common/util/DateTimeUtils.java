package com.bishe.common.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间辅助类
 *
 * @author chentay
 * @date 2019/06/04
 */
public class DateTimeUtils {

    private DateTimeUtils(){}

    private static class HolderClass {
        private final static DateTimeUtils INSTANCE = new DateTimeUtils();
        private final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    }

    public static DateTimeUtils getInstance(){
        return HolderClass.INSTANCE;
    }

    /**
     * 格式化时间
     *
     * @param localDateTime 时间
     * @return {@link String}
     */
    public String format(LocalDateTime localDateTime){
        return format(HolderClass.DEFAULT_PATTERN,localDateTime);
    }

    /**
     * 格式化时间
     *
     * @param pattern 时间格式
     * @param localDateTime 时间
     * @return {@link String}
     */
    public String format(String pattern, LocalDateTime localDateTime){
        if (StringUtils.isEmpty(pattern)){
            pattern = HolderClass.DEFAULT_PATTERN;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }
}
