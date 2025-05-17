package com.example.hx_api.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期格式化为字符串
     *
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String format(Date date) {
        return format(date, DEFAULT_PATTERN);
    }
    
    /**
     * 日期格式化为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 字符串解析为日期
     *
     * @param dateStr 日期字符串
     * @return 解析后的日期
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_PATTERN);
    }
    
    /**
     * 字符串解析为日期
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 解析后的日期
     */
    public static Date parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期解析异常", e);
        }
    }
} 