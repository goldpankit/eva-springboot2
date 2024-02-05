package com.eva.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期工具
 */
public final class DateUtil {

    /**
     * 获取日期的开始时间
     *
     * @param date 日期
     * @return java.util.Date
     */
    public java.util.Date getStart (java.util.Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期的结束时间
     *
     * @param date 日期
     * @return java.util.Date
     */
    public java.util.Date getEnd (java.util.Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 格式化
     *
     * @param date 日期
     * @return String
     */
    public String format (java.util.Date date) {
        if (date == null) {
            return null;
        }
        return this.format(date, "yyyy/MM/dd HH:mm:ss:sss");
    }

    /**
     * 格式化
     *
     * @param date 日期
     * @param format 格式
     * @return String
     */
    public String format (java.util.Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
