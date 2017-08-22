package com.woting.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by amine on 2017/6/7.
 */

public class TimeUtil {
    public static String formatterTime(long ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(ms);
        return hms;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间 HH:mm
     * @param s
     * @return
     */
    public static String stampToDateForH(String s) {
        String res = "00:00";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

}
