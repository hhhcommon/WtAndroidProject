package com.wotingfm.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.woting.commonplat.config.GlobalAddressConfig;
import com.woting.commonplat.manager.PhoneMsgManager;
import com.wotingfm.common.application.BSApplication;
import com.wotingfm.common.constant.StringConstant;

/**
 * 公共Util类
 *
 * @author 辛龙
 *         2016年8月5日
 */
public class CommonUtils {

    /**
     * 获取USERID，没有则返回imei
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        SharedPreferences sp = BSApplication.SharedPreferences;
        String UserId = sp.getString(StringConstant.USERID, "");
        if (UserId == null || UserId.equals("")) {
            return PhoneMsgManager.imei;
        } else {
            return UserId;
        }
    }

    /**
     * 获取USERID，没有则返回imei
     *
     * @param context
     * @return
     */
    public static String getUserIdNoImei(Context context) {
        SharedPreferences sp = BSApplication.SharedPreferences;
        String UserId = sp.getString(StringConstant.USERID, "");
        if (UserId == null || UserId.equals("") ) {
            return "";
        } else {
            return UserId;
        }
    }

    /**
     * =====专门为socket使用=====
     * 获取USERID，没有则返回null
     *
     * @return
     */

    public static String getSocketUserId() {
        try {
            SharedPreferences sp = BSApplication.SharedPreferences;
            String UserId = sp.getString(StringConstant.USERID, "");
            if (UserId == null || UserId.equals("") ) {
                return null;
            } else {
                return UserId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}