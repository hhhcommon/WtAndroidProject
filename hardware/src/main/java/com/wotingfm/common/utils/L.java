package com.wotingfm.common.utils;

import android.util.Log;

import com.wotingfm.common.net.RetrofitService;


/**
 * Created by Amine on 2015/4/30.
 */
public class L {
    private static final String TAG = "nanzhu";

    /**
     * i
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (RetrofitService.isLOG) {
            Log.i(tag, msg);
        }
    }

    /**
     * i
     * default tag o2bra
     *
     * @param msg
     */
    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * e
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (RetrofitService.isLOG) {
            Log.e(tag, msg);
        }
    }

    /**
     * e
     * default tag o2bra
     *
     * @param msg
     */
    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * d
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (RetrofitService.isLOG) {
            Log.d(tag, msg);
        }
    }

    /**
     * d
     *
     * @param msg
     */
    public static void d(String msg) {
        d(TAG, msg);
    }
}
