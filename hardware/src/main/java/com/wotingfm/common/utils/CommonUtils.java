package com.wotingfm.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
     * @return
     */
    public static String getUserId() {
        SharedPreferences sp = BSApplication.SharedPreferences;
        String UserId = sp.getString(StringConstant.USER_ID, "");
        if (UserId == null || UserId.equals("")) {
            return PhoneMsgManager.imei;
        } else {
            return UserId;
        }
    }

    /**
     * 获取USERID，没有则返回imei
     *
     * @return
     */
    public static String getUserIdNoImei() {
        SharedPreferences sp = BSApplication.SharedPreferences;
        String UserId = sp.getString(StringConstant.USER_ID, "");
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
            String UserId = sp.getString(StringConstant.USER_ID, "");
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

    public static boolean isLogin() {
        String login = BSApplication.SharedPreferences.getString(StringConstant.IS_LOGIN, "false");// 是否登录
        if (!login.trim().equals("") && login.equals("true")) {
            return true;
        } else {
            return false;
        }
    }
}
