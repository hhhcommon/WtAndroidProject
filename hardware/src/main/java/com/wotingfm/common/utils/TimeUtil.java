package com.wotingfm.common.utils;

import java.text.SimpleDateFormat;
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
}
