package com.zkys.pad.launcher.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mingpk on 2016/11/23.
 */

public class DateUtil {

    /**
     * 时间格式
     * @param s       long 1479106569 时间格式，转换 MM-dd
     * @param pattern
     * @return
     */
    public static String strLongToFormatTime(long s, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date server = new Date(s);
        String str = format.format(server);
        return str;
    }

    public static String strLongToFormatTime(String s, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date server = new Date(Long.valueOf(s));
            String str = format.format(server);
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 当前的时间，转换成long型 时间戳
     * java中Date类中的getTime()是获取时间戳的，java中生成的时间戳精确到毫秒级别，
     * 而unix中精确到秒级别，所以通过java生成的时间戳需要除以1000。
     *
     * @return
     */
    public static long dateTimeFormatLong() {
        Date date = new Date();
        return date.getTime() / 1000;
    }

    public static int getHourByTime(long time){
        Date date = new Date(time);
        return date.getHours();
    }
    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateString(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将字符yyyy-MM-dd 格式的日期，转换成Date
     *
     * @param formatStr
     * @return
     */
    public static Date stringToDate(String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(formatStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String dateToUnixTime(Date date) {
        return String.valueOf(date.getTime() / 1000);
    }

    /*
    *计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
    * 根据差值返回多长之间前或多长时间后
    * */
    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天"+hour + "小时"+min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时"+ min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒" ;
        return "0秒";
    }

    public static String getDistanceTime(long diff) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天"+hour + "小时"+min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时"+ min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒" ;
        return "0秒";
    }

    public static String getDateByXTime(long time, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String dateString = formatter.format(time);
            return dateString;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String getDateDDByTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(time);
        return dateString;
    }


    public static String getCurrDateTime() {
        return getCurrentDateString("yyyy-MM-dd HH:mm:ss");
    }


    /**
     *
     * @param time
     * @return
     */
    public static String getItemTime(long time) {
        // 当天时间就显示为时间 ，隔天则就显示为年月日
        String format = "yyyy/MM/dd";
        String timeStr = getDateByXTime(time, format);
        String currStr = getCurrentDateString(format);
        if (currStr.equals(timeStr)) {
            return getDateByXTime(time, "HH:mm");
        }
        return timeStr;
    }


    public static String getDate(long time){
        String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        return result;
    }


}
